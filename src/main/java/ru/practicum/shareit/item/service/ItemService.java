package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(ItemDto itemDto, Long id);

    ItemDto update(ItemDto itemDto, Long ownerId, Long itemId);

    ItemDto getById(Long id);

    List<ItemDto> getByOwner(Long ownerId);

    List<ItemDto> search(String text);

}
