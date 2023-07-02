package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    BookingDtoResponse create(BookingDtoRequest bookingDtoRequest, Long userId);

    Booking checkBookingExistentAndGet(Long id);

    BookingDtoResponse approve(Long userId, Long bookingId, boolean isApproved);

    BookingDtoResponse getById(Long bookingId, Long userId);

    List<BookingDtoResponse> getByOwnerId(Long ownerId, String state, Integer from, Integer size);

    List<BookingDtoResponse> getByBookerId(Long bookerId, String state, Integer from, Integer size);

}
