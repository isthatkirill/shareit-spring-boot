package isthatkirill.shareit.item.service;

import isthatkirill.shareit.item.dto.ItemDtoRequest;
import isthatkirill.shareit.item.dto.ItemDtoResponse;
import isthatkirill.shareit.item.comment.dto.CommentDtoRequest;
import isthatkirill.shareit.item.comment.dto.CommentDtoResponse;
import isthatkirill.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDtoRequest create(ItemDtoRequest itemDtoRequest, Long id);

    ItemDtoRequest update(ItemDtoRequest itemDtoRequest, Long ownerId, Long itemId);

    ItemDtoResponse getById(Long itemId, Long userId);

    List<ItemDtoResponse> getByOwner(Long ownerId, Integer from, Integer size);

    List<ItemDtoRequest> search(String text, Integer from, Integer size);

    Item checkItemExistentAndGet(Long id);

    CommentDtoResponse createComment(Long itemId, Long userId, CommentDtoRequest commentDtoRequest);

}
