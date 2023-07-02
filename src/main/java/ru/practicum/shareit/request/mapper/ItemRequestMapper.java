package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(target = "description", source = "itemRequestDto.description")
    @Mapping(target = "requester", source = "requester")
    ItemRequest toItemRequest(User requester, ItemRequestDto itemRequestDto);

    @Mapping(target = "items", source = "itemRequest.items")
    ItemRequestDtoResponse toItemRequestDtoResponse(ItemRequest itemRequest);

    List<ItemRequestDtoResponse> toItemRequestDtoResponse(List<ItemRequest> itemRequest);

}
