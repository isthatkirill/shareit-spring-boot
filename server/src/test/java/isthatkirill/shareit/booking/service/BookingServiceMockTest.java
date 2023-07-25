package isthatkirill.shareit.booking.service;

import isthatkirill.shareit.booking.dto.BookingDtoRequest;
import isthatkirill.shareit.booking.dto.BookingDtoResponse;
import isthatkirill.shareit.booking.mapper.BookingMapper;
import isthatkirill.shareit.booking.model.Booking;
import isthatkirill.shareit.booking.repository.BookingRepository;
import isthatkirill.shareit.item.service.ItemService;
import isthatkirill.shareit.user.model.User;
import isthatkirill.shareit.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import isthatkirill.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceMockTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BookingRepository bookingRepository;

    @Test
    void createTest() {
        Long ownerId = 1L;
        Long bookerId = 2L;
        Long itemId = 1L;
        BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder().itemId(itemId).build();
        User owner = User.builder().id(ownerId).build();
        User booker = User.builder().id(bookerId).build();
        Item item = Item.builder().id(itemId).available(true).owner(owner).build();
        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();
        Booking booking = new Booking();


        when(userService.checkUserExistentAndGet(anyLong())).thenReturn(booker);
        when(itemService.checkItemExistentAndGet(anyLong())).thenReturn(item);
        when(bookingMapper.toBooking(any(), any(), any())).thenReturn(booking);
        when(bookingRepository.save(any())).thenReturn(booking);
        when(bookingMapper.toBookingDtoResponse(any(Booking.class))).thenReturn(bookingDtoResponse);

        bookingService.create(bookingDtoRequest, bookerId);

        verify(userService, times(1)).checkUserExistentAndGet(bookerId);
        verify(itemService, times(1)).checkItemExistentAndGet(itemId);
        verify(bookingMapper, times(1)).toBooking(bookingDtoRequest, booker, item);
        verify(bookingRepository, times(1)).save(booking);
        verify(bookingMapper, times(1)).toBookingDtoResponse(booking);
        verifyNoMoreInteractions(userService, itemService, bookingMapper, bookingRepository);
    }

    @Test
    void getByBookerId() {
        Long bookerId = 1L;
        String state = "ALL";
        int from = 0, size = 10;
        User user = new User();
        List<BookingDtoResponse> bookingsDto = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, size);

        when(userService.checkUserExistentAndGet(anyLong())).thenReturn(user);
        when(bookingRepository.findAllByBookerIdOrderByStartDesc(anyLong(), any())).thenReturn(bookings);
        when(bookingMapper.toBookingDtoResponse(anyList())).thenReturn(bookingsDto);

        bookingService.getByBookerId(bookerId, state, from, size);

        verify(userService, times(1)).checkUserExistentAndGet(bookerId);
        verify(bookingRepository, times(1)).findAllByBookerIdOrderByStartDesc(bookerId, pageable);
        verify(bookingMapper, times(1)).toBookingDtoResponse(bookings);
        verifyNoMoreInteractions(userService, bookingMapper, bookingRepository);
    }

    @Test
    void getByOwnerId() {
        Long bookerId = 1L;
        String state = "CURRENT";
        int from = 0, size = 10;
        User user = new User();
        List<BookingDtoResponse> bookingsDto = new ArrayList<>();
        List<Booking> bookings = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, size);

        when(userService.checkUserExistentAndGet(anyLong())).thenReturn(user);
        when(bookingRepository.findCurrentBookingsByOwner(anyLong(), any())).thenReturn(bookings);
        when(bookingMapper.toBookingDtoResponse(anyList())).thenReturn(bookingsDto);

        bookingService.getByOwnerId(bookerId, state, from, size);

        verify(userService, times(1)).checkUserExistentAndGet(bookerId);
        verify(bookingRepository, times(1)).findCurrentBookingsByOwner(bookerId, pageable);
        verify(bookingMapper, times(1)).toBookingDtoResponse(bookings);
        verifyNoMoreInteractions(userService, bookingMapper, bookingRepository);
    }

}