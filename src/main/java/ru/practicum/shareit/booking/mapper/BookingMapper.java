package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "booker", source = "user")
    @Mapping(target = "id", ignore = true)
    Booking toBooking(BookingDtoRequest bookingDtoRequest, User user, Item item);

    BookingDtoRequest toBookingDtoRequest(Booking booking);

    BookingDtoResponse toBookingDtoResponse(Booking booking);

    List<BookingDtoResponse> toBookingDtoResponse(List<Booking> bookings);

}
