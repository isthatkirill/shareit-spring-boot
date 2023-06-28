package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "id", source = "itemId")
    @Mapping(target = "name", source = "itemDtoRequest.name")
    Item toItem(ItemDtoRequest itemDtoRequest, User owner, Long itemId);

    ItemDtoRequest toItemDtoRequest(Item item);

    List<ItemDtoRequest> toItemDtoRequest(List<Item> items);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "nextBooking", source = "nextBooking")
    @Mapping(target = "lastBooking", source = "lastBooking")
    @Mapping(target = "comments", source = "comments")
    ItemDtoResponse toItemDtoResponse(Item item, BookingShort nextBooking,
                                      BookingShort lastBooking, List<CommentDtoResponse> comments);
}
