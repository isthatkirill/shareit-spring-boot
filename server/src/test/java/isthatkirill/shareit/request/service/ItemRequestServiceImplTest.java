package isthatkirill.shareit.request.service;

import isthatkirill.shareit.item.dto.ItemDtoRequest;
import isthatkirill.shareit.item.service.ItemService;
import isthatkirill.shareit.request.dto.ItemRequestDtoLong;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import isthatkirill.shareit.request.dto.ItemRequestDto;
import isthatkirill.shareit.util.exception.NotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemRequestServiceImplTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private ItemService itemService;

    @Test
    @Order(1)
    @Sql(value = {"/test-schema.sql", "/test-users.sql"})
    void createTest() {
        ItemRequestDto dtoIn = ItemRequestDto.builder()
                .description("req-1 user-2")
                .build();

        ItemRequestDtoLong dtoOut = itemRequestService.create(dtoIn, 2L);

        assertThat(dtoOut).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("description", "req-1 user-2")
                .hasFieldOrProperty("items").hasFieldOrProperty("created");
    }

    @Test
    @Order(2)
    void createByNonExistentUserTest() {
        ItemRequestDto dtoIn = ItemRequestDto.builder()
                .description("req-2 user-1000")
                .build();

        Throwable e = assertThrows(NotFoundException.class, () -> {
            itemRequestService.create(dtoIn, 100L);
        });

        assertThat(e).hasMessage("Entity User not found. Id=100");
    }

    @Test
    @Order(3)
    void getAllTest() {
        createTestData();
        List<ItemRequestDtoLong> requests = itemRequestService.getAll(0, 10, 3L);

        assertThat(requests).hasSize(3)
                .extracting(ItemRequestDtoLong::getDescription)
                .containsExactly("req-5 user-4", "req-2 user-2", "req-1 user-2");
    }

    @Test
    @Order(4)
    void getAllWithPaginationTest() {
        List<ItemRequestDtoLong> requests = itemRequestService.getAll(1, 1, 3L);

        assertThat(requests).hasSize(1)
                .extracting(ItemRequestDtoLong::getDescription)
                .containsExactly("req-2 user-2");
    }

    @Test
    @Order(5)
    void getAllByNonExistentUserTest() {
        Throwable e = assertThrows(NotFoundException.class, () -> {
            itemRequestService.getAll(0, 100, 100L);
        });
        assertThat(e).hasMessage("Entity User not found. Id=100");
    }

    @Test
    @Order(6)
    void getOwnTest() {
        List<ItemRequestDtoLong> requests = itemRequestService.getOwn(2L);

        assertThat(requests).hasSize(2)
                .extracting(ItemRequestDtoLong::getDescription)
                .containsExactly("req-2 user-2", "req-1 user-2");
    }

    @Test
    @Order(7)
    void getOwnByNonExistentUserTest() {
        Throwable e = assertThrows(NotFoundException.class, () -> {
            itemRequestService.getOwn(100L);
        });
        assertThat(e).hasMessage("Entity User not found. Id=100");
    }

    @Test
    @Order(8)
    void getByIdTest() {
        ItemRequestDtoLong dto = itemRequestService.getById(2L, 4L);

        assertThat(dto).isNotNull()
                .hasFieldOrPropertyWithValue("description", "req-2 user-2")
                .hasFieldOrPropertyWithValue("id", 2L)
                .hasFieldOrProperty("created").hasFieldOrProperty("items");
    }

    @Test
    @Order(9)
    void getByNonExistentIdTest() {
        Throwable e = assertThrows(NotFoundException.class, () -> {
            itemRequestService.getById(100L, 4L);
        });

        assertThat(e).hasMessage("Entity ItemRequest not found. Id=100");
    }

    @Test
    @Order(10)
    void getByNonExistentUserTest() {
        Throwable e = assertThrows(NotFoundException.class, () -> {
            itemRequestService.getById(2L, 100L);
        });

        assertThat(e).hasMessage("Entity User not found. Id=100");
    }

    @Test
    @Order(11)
    void userAnswerWithItemToTheRequestTest() {
        ItemDtoRequest itemDto = ItemDtoRequest.builder()
                .name("testName")
                .description("answer for req-2")
                .requestId(2L)
                .available(true)
                .build();

        itemService.create(itemDto, 1L);
        ItemRequestDtoLong dto = itemRequestService.getById(2L, 2L);

        Assertions.assertThat(dto.getItems()).hasSize(1)
                .extracting(ItemDtoRequest::getDescription)
                .containsExactly("answer for req-2");
    }

    private void createTestData() {
        ItemRequestDto dtoIn1 = ItemRequestDto.builder()
                .description("req-2 user-2")
                .build();
        ItemRequestDto dtoIn2 = ItemRequestDto.builder()
                .description("req-3 user-3")
                .build();
        ItemRequestDto dtoIn3 = ItemRequestDto.builder()
                .description("req-4 user-3")
                .build();
        ItemRequestDto dtoIn4 = ItemRequestDto.builder()
                .description("req-5 user-4")
                .build();

        itemRequestService.create(dtoIn1, 2L);
        itemRequestService.create(dtoIn2, 3L);
        itemRequestService.create(dtoIn3, 3L);
        itemRequestService.create(dtoIn4, 4L);
    }
}