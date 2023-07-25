package isthatkirill.shareit.item.dto;

import isthatkirill.shareit.booking.dto.BookingShort;
import lombok.*;
import lombok.experimental.FieldDefaults;
import isthatkirill.shareit.item.comment.dto.CommentDtoResponse;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoResponse {

    Long id;
    String name;
    String description;
    Boolean available;
    BookingShort nextBooking;
    BookingShort lastBooking;
    List<CommentDtoResponse> comments;

}
