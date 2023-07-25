package isthatkirill.shareit.request.mapper;

import isthatkirill.shareit.request.model.ItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import isthatkirill.shareit.request.dto.ItemRequestDto;
import isthatkirill.shareit.request.dto.ItemRequestDtoLong;
import isthatkirill.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(target = "description", source = "itemRequestDto.description")
    @Mapping(target = "requester", source = "requester")
    ItemRequest toItemRequest(User requester, ItemRequestDto itemRequestDto);

    @Mapping(target = "items", source = "itemRequest.items")
    ItemRequestDtoLong toItemRequestDtoLong(ItemRequest itemRequest);

    List<ItemRequestDtoLong> toItemRequestDtoLong(List<ItemRequest> itemRequest);

}
