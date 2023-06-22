package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.annotation.ValidBookingDate;

import java.time.LocalDateTime;

@Data
@Builder
@ValidBookingDate
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoRequest {

    LocalDateTime start;
    LocalDateTime end;
    Long itemId;

}
