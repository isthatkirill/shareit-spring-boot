package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoLong;
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
    public ItemRequestDtoLong create(ItemRequestDto itemRequestDto, Long userId) {
        ItemRequest itemRequest = itemRequestMapper
                .toItemRequest(userService.checkUserExistentAndGet(userId), itemRequestDto);
        log.info("New item request created by user id={} with description={}", userId, itemRequestDto.getDescription());
        return itemRequestMapper.toItemRequestDtoLong(itemRequestRepository.save(itemRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDtoLong> getAll(Integer from, Integer size, Long userId) {
        userService.checkUserExistentAndGet(userId);
        log.info("User with id={} requested {} items from item {}", userId, size, from);
        return itemRequestMapper.toItemRequestDtoLong(itemRequestRepository
                .findAllByRequesterIdNotOrderByCreatedDesc(userId,
                        PageRequest.of(from > 0 ? from / size : 0,  size)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDtoLong> getOwn(Long userId) {
        userService.checkUserExistentAndGet(userId);
        log.info("User with id={} requested his item requests list", userId);
        return itemRequestMapper.toItemRequestDtoLong(itemRequestRepository
                .findAllByRequesterIdOrderByCreatedDesc(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDtoLong getById(Long requestId, Long userId) {
        userService.checkUserExistentAndGet(userId);
        return itemRequestMapper.toItemRequestDtoLong(checkItemRequestExistentAndGet(requestId));
    }

    private ItemRequest checkItemRequestExistentAndGet(Long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(ItemRequest.class, "Id=" + requestId));
    }

}
