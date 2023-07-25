package isthatkirill.shareit.item.mapper;

import isthatkirill.shareit.booking.model.BookingShortImpl;
import isthatkirill.shareit.item.comment.dto.CommentDtoResponse;
import isthatkirill.shareit.item.dto.ItemDtoRequest;
import isthatkirill.shareit.item.dto.ItemDtoResponse;
import isthatkirill.shareit.user.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import isthatkirill.shareit.item.model.Item;

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
                .hasFieldOrPropertyWithValue("name", itemDto.getName())
                .hasFieldOrPropertyWithValue("description", itemDto.getDescription())
                .hasFieldOrPropertyWithValue("available", itemDto.getAvailable())
                .hasFieldOrPropertyWithValue("owner", user);
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
                .hasFieldOrPropertyWithValue("id", item.getId())
                .hasFieldOrPropertyWithValue("name", item.getName())
                .hasFieldOrPropertyWithValue("description", item.getDescription())
                .hasFieldOrPropertyWithValue("available", item.isAvailable())
                .hasFieldOrPropertyWithValue("requestId", item.getRequestId());
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
                .hasFieldOrPropertyWithValue("id", item.getId())
                .hasFieldOrPropertyWithValue("name", item.getName())
                .hasFieldOrPropertyWithValue("description", item.getDescription())
                .hasFieldOrPropertyWithValue("available", item.isAvailable())
                .hasFieldOrPropertyWithValue("nextBooking", nextBooking)
                .hasFieldOrPropertyWithValue("lastBooking", lastBooking)
                .hasFieldOrPropertyWithValue("comments", comments);
    }
}