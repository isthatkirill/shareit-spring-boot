package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, Long id);

    ItemDto update(ItemDto itemDto, Long ownerId, Long itemId);

    ItemDtoExtended getById(Long itemId, Long userId);

    List<ItemDtoExtended> getByOwner(Long ownerId);

    List<ItemDto> search(String text);

    Item checkItemExistentAndGet(Long id);

    Item checkItemAvailabilityAndGet(Long id);

}
