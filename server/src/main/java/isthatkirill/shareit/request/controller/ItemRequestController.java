package isthatkirill.shareit.request.controller;

import isthatkirill.shareit.request.service.ItemRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import isthatkirill.shareit.request.dto.ItemRequestDto;
import isthatkirill.shareit.request.dto.ItemRequestDtoLong;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoLong create(@RequestBody ItemRequestDto itemRequestDto,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoLong> getAll(@RequestParam Integer from,
                                           @RequestParam Integer size,
                                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAll(from, size, userId);
    }

    @GetMapping
    public List<ItemRequestDtoLong> getOwn(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getOwn(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoLong getById(@PathVariable Long requestId,
                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getById(requestId, userId);
    }

}
