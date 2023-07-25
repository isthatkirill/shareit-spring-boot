package isthatkirill.shareit.booking.model;

import java.time.LocalDateTime;

public interface BookingShort {

    Long getBookerId();

    LocalDateTime getStart();

    LocalDateTime getEnd();

    Long getId();

}
