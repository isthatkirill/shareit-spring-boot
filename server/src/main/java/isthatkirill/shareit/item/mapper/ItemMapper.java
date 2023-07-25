package isthatkirill.shareit.item.mapper;

import isthatkirill.shareit.booking.model.BookingShort;
import isthatkirill.shareit.item.dto.ItemDtoRequest;
import isthatkirill.shareit.item.dto.ItemDtoResponse;
import isthatkirill.shareit.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import isthatkirill.shareit.item.comment.dto.CommentDtoResponse;
import isthatkirill.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "id", source = "itemId")
    @Mapping(target = "name", source = "itemDtoRequest.name")
    @Mapping(target = "requestId", source = "itemDtoRequest.requestId")
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
