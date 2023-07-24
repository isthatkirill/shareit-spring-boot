package ru.practicum.shareit.item.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDtoResponse {

    Long id;
    String text;
    String authorName;

    @JsonFormat
    LocalDateTime created;

}
