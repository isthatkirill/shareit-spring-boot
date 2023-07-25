package isthatkirill.shareit.booking.model;

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
