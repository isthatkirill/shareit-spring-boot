package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoLong;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceMockTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    @Mock
    private UserService userService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Test
    void createTest() {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        Long userId = 1L;
        User user = new User();
        ItemRequest itemRequest = new ItemRequest();
        ItemRequestDtoLong expectedDtoLong = new ItemRequestDtoLong();

        when(userService.checkUserExistentAndGet(anyLong()))
                .thenReturn(user);
        when(itemRequestMapper.toItemRequest(any(), any()))
                .thenReturn(itemRequest);
        when(itemRequestRepository.save(any()))
                .thenReturn(itemRequest);
        when(itemRequestMapper.toItemRequestDtoLong(any(ItemRequest.class)))
                .thenReturn(expectedDtoLong);

        itemRequestService.create(itemRequestDto, userId);

        verify(userService, times(1)).checkUserExistentAndGet(userId);
        verify(itemRequestMapper, times(1)).toItemRequest(user, itemRequestDto);
        verify(itemRequestRepository, times(1)).save(itemRequest);
        verify(itemRequestMapper, times(1)).toItemRequestDtoLong(itemRequest);
        verifyNoMoreInteractions(userService, itemRequestMapper, itemRequestRepository);
    }

    @Test
    void getAllTest() {
        Integer from = 0;
        Integer size = 10;
        Long userId = 1L;
        User user = new User();
        List<ItemRequest> itemRequests = new ArrayList<>();
        List<ItemRequestDtoLong> dtos = new ArrayList<>();

        when(userService.checkUserExistentAndGet(anyLong()))
                .thenReturn(user);
        when(itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(
                anyLong(), any())).thenReturn(itemRequests);
        when(itemRequestMapper.toItemRequestDtoLong(anyList()))
                .thenReturn(dtos);

        itemRequestService.getAll(from, size, userId);

        verify(userService, times(1)).checkUserExistentAndGet(userId);
        verify(itemRequestRepository, times(1))
                .findAllByRequesterIdNotOrderByCreatedDesc(userId, PageRequest.of(from > 0 ? from / size : 0, size));
        verify(itemRequestMapper, times(1)).toItemRequestDtoLong(itemRequests);
        verifyNoMoreInteractions(userService, itemRequestRepository, itemRequestMapper);
    }

    @Test
    void getOwn() {
        Long userId = 1L;
        User user = new User();
        List<ItemRequest> itemRequests = new ArrayList<>();
        List<ItemRequestDtoLong> dtos = new ArrayList<>();

        when(userService.checkUserExistentAndGet(anyLong()))
                .thenReturn(user);
        when(itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(anyLong()))
                .thenReturn(itemRequests);
        when(itemRequestMapper.toItemRequestDtoLong(anyList()))
                .thenReturn(dtos);

        itemRequestService.getOwn(userId);

        verify(userService, times(1)).checkUserExistentAndGet(userId);
        verify(itemRequestRepository, times(1))
                .findAllByRequesterIdOrderByCreatedDesc(userId);
        verify(itemRequestMapper, times(1)).toItemRequestDtoLong(itemRequests);
        verifyNoMoreInteractions(userService, itemRequestRepository, itemRequestMapper);
    }

    @Test
    void getById() {
        Long requestId = 1L;
        Long userId = 1L;
        User user = new User();
        ItemRequest itemRequest = new ItemRequest();
        ItemRequestDtoLong expectedDtoLong = new ItemRequestDtoLong();

        when(userService.checkUserExistentAndGet(anyLong()))
                .thenReturn(user);
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));
        when(itemRequestMapper.toItemRequestDtoLong(any(ItemRequest.class)))
                .thenReturn(expectedDtoLong);

        itemRequestService.getById(requestId, userId);

        verify(userService, times(1)).checkUserExistentAndGet(userId);
        verify(itemRequestRepository, times(1)).findById(requestId);
        verify(itemRequestMapper, times(1)).toItemRequestDtoLong(itemRequest);
        verifyNoMoreInteractions(userService, itemRequestRepository, itemRequestMapper);
    }
}