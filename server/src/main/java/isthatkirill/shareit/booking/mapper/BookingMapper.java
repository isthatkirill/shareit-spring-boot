package isthatkirill.shareit.booking.mapper;

import isthatkirill.shareit.booking.dto.BookingDtoRequest;
import isthatkirill.shareit.booking.dto.BookingDtoResponse;
import isthatkirill.shareit.booking.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import isthatkirill.shareit.item.model.Item;
import isthatkirill.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "booker", source = "user")
    @Mapping(target = "id", ignore = true)
    Booking toBooking(BookingDtoRequest bookingDtoRequest, User user, Item item);

    BookingDtoResponse toBookingDtoResponse(Booking booking);

    List<BookingDtoResponse> toBookingDtoResponse(List<Booking> bookings);

}
