package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingShortImpl implements BookingShort {

    private Long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long id;

}
