package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.BookingShort;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.IncorrectOwnerException;
import ru.practicum.shareit.util.exception.ItemNotAvailableException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public ItemDto create(ItemDto itemDto, Long ownerId) {
        userService.checkUserExistentAndGet(ownerId);
        Item item = itemMapper.toItem(itemDto, userService.checkUserExistentAndGet(ownerId), null);
        log.info("Item created: {} by owner id = {}", itemDto.getName(), ownerId);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(ItemDto itemDto, Long ownerId, Long itemId) {
        userService.checkUserExistentAndGet(ownerId);
        checkItemExistentAndGet(itemId);
        Item item = getItemIfHaveCorrectOwner(itemMapper.toItem(itemDto, userService.checkUserExistentAndGet(ownerId), itemId));
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        log.info("Item updated: name = {}, id = {}", itemDto.getName(), itemId);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDtoExtended getById(Long itemId, Long ownerId) {
        Item item = checkItemExistentAndGet(itemId);
        BookingShort nextBooking = null;
        BookingShort lastBooking = null;
        if (item.getOwner().getId() == ownerId) {
            nextBooking = bookingRepository
                    .findNextBooking(itemId, PageRequest.of(0, 1))
                    .stream().findFirst().orElse(null);
            lastBooking = bookingRepository
                    .findLastBooking(itemId, PageRequest.of(0, 1))
                    .stream().findFirst().orElse(null);
        }
        log.info("Get item id={}", itemId);
        return itemMapper.toItemDtoExtended(item, nextBooking, lastBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDtoExtended> getByOwner(Long ownerId) {
        List<Item> items = itemRepository.findAllByOwnerIdOrderById(ownerId);
        List<ItemDtoExtended> itemDto = items.stream()
                .map(i -> getById(i.getId(), ownerId))
                .collect(Collectors.toList());
        log.info("Owner id={} requested list of his items", ownerId);
        return itemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(String text) {
        if (text.equals("")) return new ArrayList<>();
        log.info("search for an item on request '{}'", text);
        return itemMapper.toItemDto(itemRepository
                .search(text));
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
