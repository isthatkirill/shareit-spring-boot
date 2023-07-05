package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResponse;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestMapper itemRequestMapper;
    private final UserService userService;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemRequestDtoResponse create(ItemRequestDto itemRequestDto, Long userId) {
        ItemRequest itemRequest = itemRequestMapper
                .toItemRequest(userService.checkUserExistentAndGet(userId), itemRequestDto);
        log.info("New item request created by user id={} with description={}", userId, itemRequestDto.getDescription());
        return itemRequestMapper.toItemRequestDtoResponse(itemRequestRepository.save(itemRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDtoResponse> getAll(Integer from, Integer size, Long userId) {
        userService.checkUserExistentAndGet(userId);
        log.info("User with id={} requested {} items from item {}", userId, size, from);
        return itemRequestMapper.toItemRequestDtoResponse(itemRequestRepository
                .findAllByRequesterIdNotOrderByCreatedDesc(userId,
                        PageRequest.of(from > 0 ? from / size : 0,  size)));
    }

    @Override
    public List<ItemRequestDtoResponse> getOwn(Long userId) {
        userService.checkUserExistentAndGet(userId);
        log.info("User with id={} requested his item requests list", userId);
        return itemRequestMapper.toItemRequestDtoResponse(itemRequestRepository
                .findAllByRequesterIdOrderByCreatedDesc(userId));
    }

    @Override
    public ItemRequestDtoResponse getById(Long requestId, Long userId) {
        userService.checkUserExistentAndGet(userId);
        return itemRequestMapper.toItemRequestDtoResponse(checkItemRequestExistentAndGet(requestId));
    }

    private ItemRequest checkItemRequestExistentAndGet(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(ItemRequest.class, "Id=" + requestId));
    }

}
