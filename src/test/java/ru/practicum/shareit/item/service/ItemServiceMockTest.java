package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.booking.model.BookingShortImpl;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.comment.dto.CommentDtoRequest;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.comment.mapper.CommentMapper;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceMockTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private CommentRepository commentRepository;

    @Test
    void createTest() {
        User user = new User();
        Item item = new Item();
        ItemDtoRequest itemDtoRequest = new ItemDtoRequest();

        when(userService.checkUserExistentAndGet(anyLong())).thenReturn(user);
        when(itemMapper.toItem(any(ItemDtoRequest.class), any(User.class), isNull())).thenReturn(item);
        when(itemMapper.toItemDtoRequest(any(Item.class))).thenReturn(itemDtoRequest);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        itemService.create(itemDtoRequest, 1L);

        verify(userService, times(1)).checkUserExistentAndGet(1L);
        verify(itemMapper, times(1)).toItem(itemDtoRequest, user, null);
        verify(itemRepository, times(1)).save(item);
        verify(itemMapper, times(1)).toItemDtoRequest(item);
        verifyNoMoreInteractions(itemRepository, itemMapper, userService);
    }

    @Test
    void updateTest() {
        Long ownerId = 1L;
        Long itemId = 2L;

        User user = User.builder()
                .id(ownerId).build();
        Item item = Item.builder()
                .owner(user)
                .id(itemId).build();
        ItemDtoRequest itemDtoRequest = new ItemDtoRequest();

        when(userService.checkUserExistentAndGet(anyLong())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemMapper.toItem(any(), any(), anyLong())).thenReturn(item);
        when(itemRepository.save(any())).thenReturn(item);
        when(itemMapper.toItemDtoRequest(any(Item.class))).thenReturn(itemDtoRequest);

        itemService.update(itemDtoRequest, ownerId, itemId);

        verify(userService, times(1)).checkUserExistentAndGet(ownerId);
        verify(itemMapper, times(1)).toItem(itemDtoRequest, user, itemId);
        verify(itemRepository, times(1)).save(item);
        verify(itemRepository, times(2)).findById(itemId);
        verify(itemMapper, times(1)).toItemDtoRequest(item);
        verifyNoMoreInteractions(itemRepository, itemMapper, userService);
    }

    @Test
    void getById() {
        Long itemId = 1L;
        Long ownerId = 2L;

        User user = User.builder()
                .id(ownerId)
                .build();
        Item item = Item.builder()
                .owner(user)
                .build();
        List<BookingShort> nextBookings = List.of(new BookingShortImpl());
        List<BookingShort> lastBookings = List.of(new BookingShortImpl());
        List<Comment> comments = List.of(new Comment());
        ItemDtoResponse itemDtoResponse = new ItemDtoResponse();

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findNextBooking(anyLong())).thenReturn(nextBookings);
        when(bookingRepository.findLastBooking(anyLong())).thenReturn(lastBookings);
        when(commentRepository.findByItemId(anyLong())).thenReturn(comments);
        when(itemMapper.toItemDtoResponse(any(), any(), any(), any()))
                .thenReturn(itemDtoResponse);

        itemService.getById(itemId, ownerId);

        verify(itemRepository, times(1)).findById(itemId);
        verify(bookingRepository, times(1)).findNextBooking(itemId);
        verify(bookingRepository, times(1)).findLastBooking(itemId);
        verify(commentRepository, times(1)).findByItemId(itemId);
        verify(itemMapper, times(1)).toItemDtoResponse(item,
                nextBookings.get(0), lastBookings.get(0),
                commentMapper.toCommentDtoResponse(comments));
        verifyNoMoreInteractions(itemRepository, itemMapper, userService, bookingRepository, commentRepository);
    }

    @Test
    void getByOwner() {
        Long ownerId = 1L;
        int from = 0, size = 10;

        Item item = Item.builder()
                .id(1L).build();
        List<Item> items = List.of(item);
        ItemDtoResponse itemDtoResponse = new ItemDtoResponse();

        when(itemRepository.findAllByOwnerIdOrderById(anyLong(), any())).thenReturn(items);
        when(bookingRepository.findNextBooking(anyLong())).thenReturn(Collections.emptyList());
        when(bookingRepository.findLastBooking(anyLong())).thenReturn(Collections.emptyList());
        when(commentRepository.findByItemId(anyLong())).thenReturn(Collections.emptyList());
        when(itemMapper.toItemDtoResponse(any(), any(), any(), any()))
                .thenReturn(itemDtoResponse);

        itemService.getByOwner(ownerId, from, size);

        verify(itemRepository, times(1))
                .findAllByOwnerIdOrderById(ownerId, PageRequest.of(from, size));
        verify(bookingRepository, times(1)).findNextBooking(item.getId());
        verify(bookingRepository, times(1)).findLastBooking(item.getId());
        verify(commentRepository, times(1)).findByItemId(item.getId());
        verify(itemMapper, times(1))
                .toItemDtoResponse(eq(item), any(), any(), any());
    }

    @Test
    void search() {
        String text = "text";
        int from = 0, size = 10;

        List<Item> items = List.of(new Item());
        List<ItemDtoRequest> itemDtos = List.of(new ItemDtoRequest());

        when(itemRepository.search(any(), any())).thenReturn(items);
        when(itemMapper.toItemDtoRequest(anyList())).thenReturn(itemDtos);

        itemService.search(text, from, size);

        verify(itemRepository, times(1))
                .search(text, PageRequest.of(from, size));
        verify(itemMapper, times(1)).toItemDtoRequest(items);
        verifyNoMoreInteractions(itemRepository, itemMapper);
    }

    @Test
    void createComment() {
        Long itemId = 1L;
        Long userId = 1L;

        Comment comment = new Comment();
        CommentDtoRequest commentDtoRequest = new CommentDtoRequest();
        CommentDtoResponse commentDtoResponse = new CommentDtoResponse();
        User user = User.builder()
                .id(userId).build();
        Item item = Item.builder()
                .id(userId).build();

        when(userService.checkUserExistentAndGet(anyLong())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.checkIfUserBookedItem(anyLong(), anyLong())).thenReturn(true);
        when(commentMapper.toComment(any(), any(), any())).thenReturn(comment);
        when(commentRepository.save(any())).thenReturn(comment);
        when(commentMapper.toCommentDtoResponse(any(Comment.class))).thenReturn(commentDtoResponse);

        itemService.createComment(itemId, userId, commentDtoRequest);

        verify(userService, times(1)).checkUserExistentAndGet(userId);
        verify(bookingRepository, times(1)).checkIfUserBookedItem(itemId, userId);
        verify(commentMapper, times(1)).toComment(commentDtoRequest.getText(), item, user);
        verify(commentRepository, times(1)).save(comment);
        verify(commentMapper, times(1)).toCommentDtoResponse(comment);
        verifyNoMoreInteractions(itemRepository, userService, commentRepository, commentMapper, bookingRepository);
    }

}