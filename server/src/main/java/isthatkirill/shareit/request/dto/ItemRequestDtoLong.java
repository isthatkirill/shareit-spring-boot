package isthatkirill.shareit.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import isthatkirill.shareit.item.dto.ItemDtoRequest;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDtoLong {

    Long id;
    String description;
    LocalDateTime created;
    List<ItemDtoRequest> items;

}
