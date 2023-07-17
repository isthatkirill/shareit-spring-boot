package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDtoRequest;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDtoRequest create(@RequestBody @Valid ItemDtoRequest itemDtoRequest, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.create(itemDtoRequest, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoRequest update(@RequestBody ItemDtoRequest itemDtoRequest,
                                 @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId) {
        return itemService.update(itemDtoRequest, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoResponse getById(@PathVariable Long itemId,
                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoResponse> getByOwner(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(defaultValue = "10") @Positive Integer size) {
        return itemService.getByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDtoRequest> search(@RequestParam("text") String text,
                                       @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                       @RequestParam(defaultValue = "10") @Positive Integer size) {
        return itemService.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse createComment(@PathVariable Long itemId,
                                            @RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestBody @Valid CommentDtoRequest commentDtoRequest) {
        return itemService.createComment(itemId, userId, commentDtoRequest);
    }

}
