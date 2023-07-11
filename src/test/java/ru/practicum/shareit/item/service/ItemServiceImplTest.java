package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.comment.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.util.exception.CommentingDeniedException;
import ru.practicum.shareit.util.exception.IncorrectOwnerException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Test
    @Order(1)
    @Sql(value = {"/test-schema.sql", "/test-users.sql"})
    void createItemTest() {
        ItemDtoRequest dtoIn = ItemDtoRequest.builder()
                .name("name_1")
                .description("description_1")
                .available(false)
                .build();

        ItemDtoRequest dtoOut = itemService.create(dtoIn, 1L);

        assertThat(dtoOut).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", dtoIn.getName())
                .hasFieldOrPropertyWithValue("description", dtoIn.getDescription())
                .hasFieldOrPropertyWithValue("available", dtoIn.getAvailable());
    }

    @Test
    @Order(2)
    void updateItemNameTest() {
        ItemDtoRequest dtoIn = ItemDtoRequest.builder()
                .name("name_updated_1")
                .build();

        ItemDtoRequest dtoOut = itemService.update(dtoIn, 1L, 1L);

        assertThat(dtoOut).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", dtoIn.getName())
                .hasFieldOrPropertyWithValue("available", false)
                .hasFieldOrPropertyWithValue("description", "description_1");
    }

    @Test
    @Order(3)
    void updateItemAvailableTest() {
        ItemDtoRequest dtoIn = ItemDtoRequest.builder()
                .available(true)
                .build();

        ItemDtoRequest dtoOut = itemService.update(dtoIn, 1L, 1L);

        assertThat(dtoOut).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("available", dtoIn.getAvailable())
                .hasFieldOrPropertyWithValue("name", "name_updated_1")
                .hasFieldOrPropertyWithValue("description", "description_1");
    }

    @Test
    @Order(4)
    void updateItemDescriptionTest() {
        ItemDtoRequest dtoIn = ItemDtoRequest.builder()
                .description("description_updated_1")
                .build();

        ItemDtoRequest dtoOut = itemService.update(dtoIn, 1L, 1L);

        assertThat(dtoOut).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("name", "name_updated_1")
                .hasFieldOrPropertyWithValue("description", dtoIn.getDescription());
    }

    @Test
    @Order(5)
    void updateItemWithNonExistentUserTest() {
        ItemDtoRequest dtoIn = ItemDtoRequest.builder()
                .description("description_non_existent_user")
                .build();

        Throwable e = assertThrows(NotFoundException.class, () -> {
            itemService.update(dtoIn, 999L, 1L);
        });

        assertThat(e).hasMessage("Entity User not found. Id=999");
    }

    @Test
    @Order(6)
    void updateItemWithWrongOwnerTest() {
        ItemDtoRequest dtoIn = ItemDtoRequest.builder()
                .description("description_wrong_owner")
                .build();

        Throwable e = assertThrows(IncorrectOwnerException.class, () -> {
            itemService.update(dtoIn, 2L, 1L);
        });

        assertThat(e).hasMessage("You are not allowed to edit this item");
    }

    @Test
    @Order(7)
    void updateNonExistentItemTest() {
        ItemDtoRequest dtoIn = ItemDtoRequest.builder()
                .description("description_for_item_which_not_exists")
                .build();

        Throwable e = assertThrows(NotFoundException.class, () -> {
            itemService.update(dtoIn, 1L, 100L);
        });

        assertThat(e).hasMessage("Entity Item not found. Id=100");
    }


    @Test
    @Order(8)
    @Sql(value = {"/test-comments-1.sql", "/test-bookings.sql"})
    void getItemByIdByOwnerTest() {
        ItemDtoResponse itemDto = itemService.getById(1L, 1L);

        assertThat(itemDto).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "name_updated_1")
                .hasFieldOrPropertyWithValue("description", "description_updated_1")
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("lastBooking.id", 1L)
                .hasFieldOrPropertyWithValue("lastBooking.bookerId", 2L)
                .hasFieldOrPropertyWithValue("nextBooking.id", 2L)
                .hasFieldOrPropertyWithValue("nextBooking.bookerId", 3L)
                .satisfies(i -> assertThat(i.getComments()).hasSize(2));
    }

    @Test
    @Order(9)
    void getItemByIdByNotOwnerTest() {
        ItemDtoResponse itemDto = itemService.getById(1L, 2L);

        assertThat(itemDto).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "name_updated_1")
                .hasFieldOrPropertyWithValue("description", "description_updated_1")
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("lastBooking", null)
                .hasFieldOrPropertyWithValue("nextBooking", null)
                .satisfies(i -> assertThat(i.getComments()).hasSize(2));
    }

    @Test
    @Order(10)
    void getNonExistentItemByIdTest() {
        Throwable e = assertThrows(NotFoundException.class, () -> {
            itemService.getById(200L, 2L);
        });

        assertThat(e).hasMessage("Entity Item not found. Id=200");
    }

    @Test
    @Order(11)
    void getByOwnerTest() {
        ItemDtoRequest dto1 = ItemDtoRequest.builder()
                .name("name_2")
                .description("description_2")
                .available(true)
                .build();

       itemService.create(dto1, 1L);

        ItemDtoRequest dto2 = ItemDtoRequest.builder()
                .name("name_3")
                .description("description_3")
                .available(true)
                .build();

        itemService.create(dto2, 1L);

        List<ItemDtoResponse> items = itemService.getByOwner(1L, 0, 10);

        assertThat(items).hasSize(3)
                .extracting(ItemDtoResponse::getName)
                .containsExactlyInAnyOrder("name_3", "name_2", "name_updated_1");
    }

    @Test
    @Order(12)
    void getByOwnerWithPaginationTest() {
        List<ItemDtoResponse> items = itemService.getByOwner(1L, 1, 2);

        assertThat(items).hasSize(2)
                .extracting(ItemDtoResponse::getName)
                .containsExactlyInAnyOrder("name_updated_1", "name_2");
    }

    @Test
    @Order(13)
    void getByOwnerOfZeroItemsTest() {
        List<ItemDtoResponse> items = itemService.getByOwner(2L, 0, 10);

        assertThat(items).isEmpty();
    }

    @Test
    @Order(14)
    void searchTest() {
        List<ItemDtoRequest> items = itemService.search("updated", 0, 10);

        assertThat(items).hasSize(1)
                .extracting(ItemDtoRequest::getName)
                .containsExactlyInAnyOrder("name_updated_1");
    }

    @Test
    @Order(15)
    void searchWithPaginationTest() {
        List<ItemDtoRequest> items = itemService.search("name_", 1, 1);

        assertThat(items).hasSize(1)
                .extracting(ItemDtoRequest::getName)
                .containsExactlyInAnyOrder("name_2");
    }

    @Test
    @Order(16)
    void searchWithBlankKeywordTest() {
        List<ItemDtoRequest> items = itemService.search("", 1, 1);

        assertThat(items).isEmpty();
    }

    @Test
    @Order(17)
    void createCommentByNonExistentUserTest() {
        Throwable e = assertThrows(NotFoundException.class, () -> {
            itemService.createComment(2L, 5L, new CommentDtoRequest("comment"));
        });

        assertThat(e).hasMessage("Entity User not found. Id=5");
    }

    @Test
    @Order(18)
    void createCommentOnNonExistentItemTest() {
        Throwable e = assertThrows(NotFoundException.class, () -> {
            itemService.createComment(10L, 2L, new CommentDtoRequest("comment"));
        });

        assertThat(e).hasMessage("Entity Item not found. Id=10");
    }

    @Test
    @Order(19)
    void createCommentWithoutBookingTest() {
        Throwable e = assertThrows(CommentingDeniedException.class, () -> {
            itemService.createComment(2L, 4L, new CommentDtoRequest("comment"));
        });

        assertThat(e).hasMessage("You didn't book this item.");
    }

    @Test
    @Order(20)
    void createCommentTest() {
        itemService.createComment(1L, 2L, new CommentDtoRequest("comment"));

        assertThat(itemService.getById(1L, 1L))
                .extracting(ItemDtoResponse::getComments)
                .asList().hasSize(3);
    }


}