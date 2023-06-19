package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "id", source = "itemId")
    @Mapping(target = "name", source = "itemDto.name")
    Item toItem(ItemDto itemDto, User owner, Long itemId);

    ItemDto toItemDto(Item item);

    List<ItemDto> toItemDto(List<Item> items);
}
