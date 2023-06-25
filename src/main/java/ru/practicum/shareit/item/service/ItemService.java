package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentDtoRequest;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDtoRequest create(ItemDtoRequest itemDtoRequest, Long id);

    ItemDtoRequest update(ItemDtoRequest itemDtoRequest, Long ownerId, Long itemId);

    ItemDtoResponse getById(Long itemId, Long userId);

    List<ItemDtoResponse> getByOwner(Long ownerId);

    List<ItemDtoRequest> search(String text);

    Item checkItemExistentAndGet(Long id);

    CommentDtoResponse createComment(Long itemId, Long userId, CommentDtoRequest commentDtoRequest);

}
