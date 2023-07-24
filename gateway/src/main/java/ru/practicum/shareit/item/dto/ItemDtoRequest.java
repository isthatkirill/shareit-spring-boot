package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoRequest {

    Long id;

    @NotBlank(message = "Name cannot be empty or null")
    String name;

    @NotBlank(message = "Description cannot be empty or null")
    String description;

    @NotNull(message = "Must equals true or false")
    Boolean available;

    Long requestId;

}
