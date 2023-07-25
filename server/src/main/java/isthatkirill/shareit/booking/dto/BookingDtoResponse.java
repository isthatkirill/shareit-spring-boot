package isthatkirill.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import isthatkirill.shareit.booking.model.Status;
import isthatkirill.shareit.item.dto.ItemDtoRequest;
import isthatkirill.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoResponse {

    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Status status;
    UserDto booker;
    ItemDtoRequest item;

}
