package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingShort;
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
import ru.practicum.shareit.util.exception.CommentingDeniedException;
import ru.practicum.shareit.util.exception.IncorrectOwnerException;
import ru.practicum.shareit.util.exception.ItemNotAvailableException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDtoRequest create(ItemDtoRequest itemDtoRequest, Long ownerId) {
        userService.checkUserExistentAndGet(ownerId);
        Item item = itemMapper.toItem(itemDtoRequest, userService.checkUserExistentAndGet(ownerId), null);
        log.info("Item created: {} by owner id = {}", itemDtoRequest.getName(), ownerId);
        return itemMapper.toItemDtoRequest(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDtoRequest update(ItemDtoRequest itemDtoRequest, Long ownerId, Long itemId) {
        userService.checkUserExistentAndGet(ownerId);
        checkItemExistentAndGet(itemId);
        Item item = getItemIfHaveCorrectOwner(itemMapper.toItem(itemDtoRequest, userService.checkUserExistentAndGet(ownerId), itemId));
        if (itemDtoRequest.getAvailable() != null) item.setAvailable(itemDtoRequest.getAvailable());
        if (itemDtoRequest.getName() != null) item.setName(itemDtoRequest.getName());
        if (itemDtoRequest.getDescription() != null) item.setDescription(itemDtoRequest.getDescription());
        log.info("Item updated: name = {}, id = {}", itemDtoRequest.getName(), itemId);
        return itemMapper.toItemDtoRequest(itemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDtoResponse getById(Long itemId, Long ownerId) {
        Item item = checkItemExistentAndGet(itemId);
        BookingShort nextBooking = null;
        BookingShort lastBooking = null;
        if (Objects.equals(item.getOwner().getId(), ownerId)) {
            nextBooking = bookingRepository
                    .findNextBooking(itemId, PageRequest.of(0, 1))
                    .stream().findFirst().orElse(null);
            lastBooking = bookingRepository
                    .findLastBooking(itemId, PageRequest.of(0, 1))
                    .stream().findFirst().orElse(null);
        }
        List<CommentDtoResponse> comments = commentMapper
                .toCommentDtoResponse(commentRepository.findByItemId(itemId));
        log.info("Get item id={}", itemId);
        return itemMapper.toItemDtoResponse(item, nextBooking, lastBooking, comments);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoResponse> getByOwner(Long ownerId) {
        List<Item> items = itemRepository.findAllByOwnerIdOrderById(ownerId);
        List<ItemDtoResponse> itemDto = items.stream()
                .map(i -> getById(i.getId(), ownerId))
                .collect(Collectors.toList());
        log.info("Owner id={} requested list of his items", ownerId);
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoRequest> search(String text) {
        if (text.equals("")) return new ArrayList<>();
        log.info("search for an item on request '{}'", text);
        return itemMapper.toItemDtoRequest(itemRepository
                .search(text));
    }

    @Override
    @Transactional
    public CommentDtoResponse createComment(Long itemId, Long userId, CommentDtoRequest commentDtoRequest) {
        if (!bookingRepository.checkIfUserBookedItem(itemId, userId)) {
            throw new CommentingDeniedException("You didn't book this item.");
        }
        User user = userService.checkUserExistentAndGet(userId);
        Item item = checkItemExistentAndGet(itemId);
        Comment comment = commentMapper.toComment(commentDtoRequest.getText(), item, user);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);
        return commentMapper.toCommentDtoResponse(comment);
    }

    @Override
    public Item checkItemExistentAndGet(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Item.class, "Id:" + id));

    }

    @Override
    public Item checkItemAvailabilityAndGet(Long id) {
        return itemRepository.findItemByIdEqualsAndAvailableIsTrue(id)
                .orElseThrow(() -> new ItemNotAvailableException("Item is not available or not found. Id:" + id));
    }

    private Item getItemIfHaveCorrectOwner(Item item) {
        return itemRepository.findById(item.getId())
                .filter(i -> i.getOwner().equals(item.getOwner()))
                .orElseThrow(() -> new IncorrectOwnerException("You are not allowed to edit this item"));
    }
}
