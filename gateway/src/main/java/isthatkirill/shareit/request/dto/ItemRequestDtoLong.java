package isthatkirill.shareit.request.dto;

import isthatkirill.shareit.item.dto.ItemDtoRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
