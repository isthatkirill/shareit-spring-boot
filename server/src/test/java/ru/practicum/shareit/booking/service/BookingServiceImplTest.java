package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.util.exception.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    @Sql(value = {"/test-schema.sql", "/test-users-extended.sql", "/test-items-extended.sql"})
    void createTest() {
        BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder()
                .itemId(2L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        BookingDtoResponse bookingDtoResponse = bookingService.create(bookingDtoRequest, 3L);

        assertThat(bookingDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("start", bookingDtoRequest.getStart())
                .hasFieldOrPropertyWithValue("end", bookingDtoRequest.getEnd())
                .hasFieldOrPropertyWithValue("status", Status.WAITING)
                .hasFieldOrPropertyWithValue("booker", userService.getById(3L))
                .hasFieldOrPropertyWithValue("item.id", 2L)
                .hasFieldOrPropertyWithValue("item.name", "item2")
                .hasFieldOrPropertyWithValue("item.description", "google");
    }

    @Test
    @Order(2)
    void createNotAvailableTest() {
        BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        Throwable e = assertThrows(ItemNotAvailableException.class, () -> {
            bookingService.create(bookingDtoRequest, 3L);
        });

        assertThat(e).hasMessage("Item is not available. Id=1");
    }

    @Test
    @Order(3)
    void createBookYourOwnItemTest() {
        BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder()
                .itemId(5L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        Throwable e = assertThrows(BookYourOwnItemException.class, () -> {
            bookingService.create(bookingDtoRequest, 2L);
        });

        assertThat(e).hasMessage("Cannot book your own item");
    }

    @Test
    @Order(4)
    void approveByNotOwnerTest() {
        Throwable e = assertThrows(IncorrectOwnerException.class, () -> {
            bookingService.approve(2L, 1L, true);
        });

        assertThat(e).hasMessage("User id=2 is not owner of item id=2");
    }

    @Test
    @Order(5)
    void approveNonExistentBookingTest() {
        Throwable e = assertThrows(NotFoundException.class, () -> {
            bookingService.approve(2L, 100L, true);
        });

        assertThat(e).hasMessage("Entity Booking not found. Id=100");
    }

    @Test
    @Order(6)
    @Transactional
    void approveFalseTest() {
        BookingDtoResponse bookingDtoResponse = bookingService.approve(1L, 1L, false);

        assertThat(bookingDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("status", Status.REJECTED);
    }

    @Test
    @Order(7)
    void approveTrueTest() {
        BookingDtoResponse bookingDtoResponse = bookingService.approve(1L, 1L, true);

        assertThat(bookingDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("status", Status.APPROVED);
    }

    @Test
    @Order(8)
    void cannotChangeApprovedTest() {
        Throwable e = assertThrows(ChangeBookingStatusException.class, () -> {
            bookingService.approve(1L, 1L, false);
        });

        assertThat(e).hasMessage("Cannot change approved or rejected status");
    }


    @Test
    @Order(9)
    void getByIdTest() {
        BookingDtoResponse bookingDtoResponse = bookingService.getById(1L, 1L);

        assertThat(bookingDtoResponse).isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @Order(10)
    void getByIdNotBookerNotOwnerTest() {
        Throwable e = assertThrows(IncorrectOwnerException.class, () -> {
            bookingService.getById(1L, 2L);
        });

        assertThat(e).hasMessage("User id=2 is not booker or owner of item id=2");
    }

    @Test
    @Order(11)
    @Sql(value = "/test-bookings-extended.sql") //creating more test data
    void getByBookerIdTest() {
        Long bookerId = 6L;
        int from = 0, size = 1;

        List<BookingDtoResponse> bookings = bookingService.getByBookerId(bookerId, "ALL", from, size);
        assertThat(bookings).hasSize(1);

        size = 10;
        bookings = bookingService.getByBookerId(bookerId, "ALL", from, size);
        assertThat(bookings).hasSize(3);

        bookings = bookingService.getByBookerId(bookerId, "REJECTED", from, size);
        assertThat(bookings).hasSize(1);

        bookings = bookingService.getByBookerId(bookerId, "WAITING", from, size);
        assertThat(bookings).isEmpty();

        bookings = bookingService.getByBookerId(bookerId, "CURRENT", from, size);
        assertThat(bookings).isEmpty();

        bookings = bookingService.getByBookerId(bookerId, "PAST", from, size);
        assertThat(bookings).isEmpty();

        bookings = bookingService.getByBookerId(bookerId, "FUTURE", from, size);
        assertThat(bookings).hasSize(3);

        Throwable e = assertThrows(UnsupportedStatusException.class, () -> {
            bookingService.getByBookerId(bookerId, "INVALID", 0, 10);
        });

        assertThat(e).hasMessage("Unknown state: INVALID");
    }

    @Test
    @Order(12)
    void getByOwnerIdTest() {
        Long ownerId = 1L;
        int from = 0, size = 1;

        List<BookingDtoResponse> bookings = bookingService.getByOwnerId(ownerId, "ALL", from, size);
        assertThat(bookings).hasSize(1);

        size = 10;
        bookings = bookingService.getByOwnerId(ownerId, "ALL", from, size);
        assertThat(bookings).hasSize(7);

        bookings = bookingService.getByOwnerId(ownerId, "REJECTED", from, size);
        assertThat(bookings).isEmpty();

        bookings = bookingService.getByOwnerId(ownerId, "WAITING", from, size);
        assertThat(bookings).hasSize(3);

        bookings = bookingService.getByOwnerId(ownerId, "CURRENT", from, size);
        assertThat(bookings).hasSize(2);

        bookings = bookingService.getByOwnerId(ownerId, "PAST", from, size);
        assertThat(bookings).hasSize(1);

        bookings = bookingService.getByOwnerId(ownerId, "FUTURE", from, size);
        assertThat(bookings).hasSize(4);

        Throwable e = assertThrows(UnsupportedStatusException.class, () -> {
            bookingService.getByOwnerId(ownerId, "INVALID", 0, 10);
        });

        assertThat(e).hasMessage("Unknown state: INVALID");
    }

}