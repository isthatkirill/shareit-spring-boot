package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingDto create(BookingDto bookingDto, Long userId) {
        User user = userService.checkUserExistentAndGet(userId);
        Long itemId = bookingDto.getItemId();
        itemService.checkItemExistentAndGet(itemId);
        Item item = itemService.checkItemAvailabilityAndGet(itemId);
        Booking booking = bookingMapper.toBooking(bookingDto, user, item);
        booking.setStatus(Status.WAITING);
        log.info("Booking created: itemId={}, userId={}", itemId, userId);
        return bookingMapper.toBookingDto(bookingRepository.save(booking));
    }
}
