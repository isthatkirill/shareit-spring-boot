package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.IncorrectOwnerException;
import ru.practicum.shareit.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;

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
        getItemIfExists(itemId);
        Item item = getItemIfHaveCorrectOwner(itemMapper.toItem(itemDto, userService.checkUserExistentAndGet(ownerId), itemId));
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        log.info("Item updated: name = {}, id = {}", itemDto.getName(), itemId);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getById(Long id) {
        return itemMapper.toItemDto(getItemIfExists(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getByOwner(Long ownerId) {
        return itemMapper.toItemDto(itemRepository.findAllByOwner_Id(ownerId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> search(String text) {
        if (text.equals("")) return new ArrayList<>();
        log.info("search for an item on request '{}'", text);
        return itemMapper.toItemDto(itemRepository
                .findItemsByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(text, text));
    }

    private Item getItemIfExists(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(User.class, "Id:" + id));

    }

    private Item getItemIfHaveCorrectOwner(Item item) {
        return itemRepository.findById(item.getId())
                .filter(i -> i.getOwner().equals(item.getOwner()))
                .orElseThrow(() -> new IncorrectOwnerException("You are not allowed to edit this item"));
    }
}
