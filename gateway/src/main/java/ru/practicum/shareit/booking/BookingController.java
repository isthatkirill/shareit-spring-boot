package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid BookingDtoRequest bookingDtoRequest,
                                         @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.create(bookingDtoRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long bookingId,
                                          @RequestParam(name = "approved") boolean isApproved) {
        return bookingClient.approve(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@PathVariable Long bookingId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingClient.getById(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getByBookerId(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                @RequestParam(defaultValue = "ALL") String state,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(defaultValue = "10") @Positive Integer size) {
        return bookingClient.getByBookerId(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(defaultValue = "10") @Positive Integer size) {
        return bookingClient.getByOwnerId(ownerId, state, from, size);
    }

}
