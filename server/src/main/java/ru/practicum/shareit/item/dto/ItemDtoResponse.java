package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;

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
