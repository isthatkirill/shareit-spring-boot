package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.model.BookingShortImpl;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    private final ItemMapper mapper = Mappers.getMapper(ItemMapper.class);

    @Test
    void toItem() {
        ItemDtoRequest itemDto = ItemDtoRequest.builder()
                .description("testDescription")
                .name("testName")
                .available(true)
                .requestId(2L)
                .build();

        User user = User.builder()
                .id(1L)
                .name("testName")
                .email("testEmail@yandex.ru")
                .build();

        Item item = mapper.toItem(itemDto, user, 1L);

        assertThat(item).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "testName")
                .hasFieldOrPropertyWithValue("description", "testDescription")
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("owner.name", "testName")
                .hasFieldOrPropertyWithValue("owner.email", "testEmail@yandex.ru")
                .hasFieldOrPropertyWithValue("owner.id", 1L);
    }

    @Test
    void toItemDtoRequest() {
        Item item = Item.builder()
                .id(4L)
                .name("testItem")
                .description("testItemDesc")
                .owner(new User(3L, "itemOwner", "itemOwner@google.com"))
                .available(true)
                .requestId(11L)
                .build();

        ItemDtoRequest itemDtoRequest = mapper.toItemDtoRequest(item);

        assertThat(itemDtoRequest).isNotNull()
                .hasFieldOrPropertyWithValue("id", 4L)
                .hasFieldOrPropertyWithValue("name", "testItem")
                .hasFieldOrPropertyWithValue("description", "testItemDesc")
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("requestId", 11L);
    }

    @Test
    void toItemDtoResponse() {
        Item item = Item.builder()
                .id(4L)
                .name("testItem")
                .description("testItemDesc")
                .owner(new User(3L, "itemOwner", "itemOwner@google.com"))
                .available(true)
                .requestId(11L)
                .build();

        BookingShortImpl nextBooking = new BookingShortImpl(2L,
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(3),
                3L);

        BookingShortImpl lastBooking = new BookingShortImpl(9L,
                LocalDateTime.now().minusDays(3),
                LocalDateTime.now().minusDays(2),
                4L);

        List<CommentDtoResponse> comments =
                List.of(new CommentDtoResponse(1L, "text1", "author1", LocalDateTime.now()),
                        new CommentDtoResponse(2L, "text2", "author2", LocalDateTime.now().plusDays(1)));

        ItemDtoResponse itemDtoResponse = mapper.toItemDtoResponse(item, nextBooking, lastBooking, comments);

        assertThat(itemDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("id", 4L)
                .hasFieldOrPropertyWithValue("name", "testItem")
                .hasFieldOrPropertyWithValue("description", "testItemDesc")
                .hasFieldOrPropertyWithValue("available", true)
                .hasFieldOrPropertyWithValue("nextBooking", nextBooking)
                .hasFieldOrPropertyWithValue("lastBooking", lastBooking)
                .hasFieldOrPropertyWithValue("comments", comments);
    }
}