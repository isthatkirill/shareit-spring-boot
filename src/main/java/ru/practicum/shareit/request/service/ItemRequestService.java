package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDtoResponse create(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDtoResponse> getAll(Integer from, Integer size, Long userId);

    List<ItemRequestDtoResponse> getOwn(Long userId);

    ItemRequestDtoResponse getById(Long requestId, Long userId);

}
