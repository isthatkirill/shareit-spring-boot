package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @Test
    void toBooking() {
        BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder()
                .itemId(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        User booker = User.builder()
                .id(1L)
                .name("user_name")
                .email("test@email.ru")
                .build();

        Item item = Item.builder()
                .id(1L)
                .name("item_name")
                .description("testDesc")
                .owner(new User())
                .available(true)
                .build();

        Booking booking = bookingMapper.toBooking(bookingDtoRequest, booker, item);

        assertThat(booking).isNotNull()
                .hasFieldOrPropertyWithValue("start", bookingDtoRequest.getStart())
                .hasFieldOrPropertyWithValue("end", bookingDtoRequest.getEnd())
                .hasFieldOrPropertyWithValue("item", item)
                .hasFieldOrPropertyWithValue("booker", booker);
    }

    @Test
    void toBookingDtoResponse() {
        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .item(Item.builder()
                        .id(1L)
                        .name("item_name")
                        .description("item_desc")
                        .build())
                .booker(User.builder()
                        .id(1L)
                        .name("user_name")
                        .email("test@email.ru")
                        .build())
                .status(Status.APPROVED)
                .build();

        BookingDtoResponse bookingDtoResponse = bookingMapper.toBookingDtoResponse(booking);

        assertThat(bookingDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("id", booking.getId())
                .hasFieldOrPropertyWithValue("start", booking.getStart())
                .hasFieldOrPropertyWithValue("end", booking.getEnd())
                .hasFieldOrPropertyWithValue("status", booking.getStatus())
                .hasFieldOrPropertyWithValue("booker", userMapper.toUserDto(booking.getBooker()))
                .hasFieldOrPropertyWithValue("item", itemMapper.toItemDtoRequest(booking.getItem()));
    }
}