package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse create(@RequestBody BookingDtoRequest bookingDtoRequest,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.create(bookingDtoRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approve(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable Long bookingId,
                                      @RequestParam(name = "approved") boolean isApproved) {
        return bookingService.approve(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getById(@PathVariable Long bookingId,
                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDtoResponse> getByBookerId(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                  @RequestParam String state,
                                                  @RequestParam Integer from,
                                                  @RequestParam Integer size) {
        return bookingService.getByBookerId(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                 @RequestParam String state,
                                                 @RequestParam Integer from,
                                                 @RequestParam Integer size) {
        return bookingService.getByOwnerId(ownerId, state, from, size);
    }

}
