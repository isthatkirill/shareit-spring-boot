package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
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

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "nextBooking", source = "nextBooking")
    @Mapping(target = "lastBooking", source = "lastBooking")
    ItemDtoExtended toItemDtoExtended(Item item, BookingShort nextBooking, BookingShort lastBooking);
}
