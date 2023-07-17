package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.annotation.ValidBookingDate;

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
