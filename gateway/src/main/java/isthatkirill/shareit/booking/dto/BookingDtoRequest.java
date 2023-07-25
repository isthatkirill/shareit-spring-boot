package isthatkirill.shareit.booking.dto;

import isthatkirill.shareit.booking.annotation.ValidBookingDate;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ValidBookingDate
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoRequest {

    LocalDateTime start;
    LocalDateTime end;
    Long itemId;

}
