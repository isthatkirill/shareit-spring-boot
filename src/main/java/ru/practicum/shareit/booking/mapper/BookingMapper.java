package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "booker", source = "user")
    @Mapping(target = "id", ignore = true)
    Booking toBooking(BookingDto bookingDto, User user, Item item);

    BookingDto toBookingDto(Booking booking);

}
