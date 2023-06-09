package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "owner", source = "ownerId")
    @Mapping(target = "id", source = "itemId")
    Item toItem(ItemDto itemDto, Long ownerId, Long itemId);

    ItemDto toItemDto(Item item);
}
