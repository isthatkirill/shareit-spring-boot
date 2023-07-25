package isthatkirill.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoRequest {

    Long id;
    String name;
    String description;
    Boolean available;
    Long requestId;

}
