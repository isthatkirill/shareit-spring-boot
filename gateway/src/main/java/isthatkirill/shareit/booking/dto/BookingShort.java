package isthatkirill.shareit.booking.dto;

import java.time.LocalDateTime;

public interface BookingShort {

    Long getBookerId();

    LocalDateTime getStart();

    LocalDateTime getEnd();

    Long getId();

}
