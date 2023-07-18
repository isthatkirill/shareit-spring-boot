package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.create(itemRequestDto, userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                         @RequestParam(defaultValue = "10") @Positive Integer size,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getAll(from, size, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwn(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getOwn(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@PathVariable Long requestId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getById(requestId, userId);
    }

}
