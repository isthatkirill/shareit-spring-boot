package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingShort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(value = {"/test-schema.sql", "/test-users-extended.sql", "/test-items-extended.sql", "/test-bookings-extended.sql"})
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;
    private final Pageable pageable = PageRequest.of(0, 10);

    @Test
    void findAllByBookerIdOrderByStartDescTest() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(6L, pageable);

        assertThat(bookings).hasSize(3)
                .extracting(Booking::getId)
                .containsExactly(8L, 7L, 6L);
    }

    @Test
    void findFutureBookingsByBookerTest() {
        List<Booking> bookings = bookingRepository.findFutureBookingsByBooker(3L, pageable);

        assertThat(bookings).hasSize(1)
                .extracting(Booking::getId)
                .containsExactly(4L);
    }

    @Test
    void findPastBookingsByBookerTest() {
        List<Booking> bookings = bookingRepository.findPastBookingsByBooker(3L, pageable);

        assertThat(bookings).hasSize(1)
                .extracting(Booking::getId)
                .containsExactly(5L);
    }

    @Test
    void findCurrentBookingsByBookerTest() {
        List<Booking> bookings = bookingRepository.findCurrentBookingsByBooker(5L, pageable);

        assertThat(bookings).hasSize(2)
                .extracting(Booking::getId)
                .containsExactly(9L, 10L);
    }

    @Test
    void findWaitingBookingsByBookerTest() {
        List<Booking> bookings = bookingRepository.findWaitingBookingsByBooker(4L, pageable);

        assertThat(bookings).hasSize(2)
                .extracting(Booking::getId)
                .containsExactly(1L, 2L);
    }

    @Test
    void findRejectedBookingsByBookerTest() {
        List<Booking> bookings = bookingRepository.findRejectedBookingsByBooker(6L, pageable);

        assertThat(bookings).hasSize(1)
                .extracting(Booking::getId)
                .containsExactly(6L);
    }

    @Test
    void findFutureBookingsByOwnerTest() {
        List<Booking> bookings = bookingRepository.findFutureBookingsByOwner(1L, pageable);

        assertThat(bookings).hasSize(3)
                .extracting(Booking::getId)
                .containsExactly(4L, 1L, 2L);
    }

    @Test
    void findPastBookingsByOwnerTest() {
        List<Booking> bookings = bookingRepository.findPastBookingsByOwner(1L, pageable);

        assertThat(bookings).hasSize(1)
                .extracting(Booking::getId)
                .containsExactly(3L);
    }

    @Test
    void findCurrentBookingsByOwnerTest() {
        List<Booking> bookings = bookingRepository.findCurrentBookingsByOwner(6L, pageable);

        assertThat(bookings).isEmpty();
    }

    @Test
    void findWaitingBookingsByOwnerTest() {
        List<Booking> bookings = bookingRepository.findWaitingBookingsByOwner(2L, pageable);

        assertThat(bookings).hasSize(1)
                .extracting(Booking::getId)
                .containsExactly(11L);
    }

    @Test
    void findRejectedBookingsByOwnerTest() {
        List<Booking> bookings = bookingRepository.findRejectedBookingsByOwner(2L, pageable);

        assertThat(bookings).hasSize(1)
                .extracting(Booking::getId)
                .containsExactly(6L);
    }

    @Test
    void findAllBookingsByOwnerTest() {
        List<Booking> bookings = bookingRepository.findAllBookingsByOwner(1L, pageable);

        assertThat(bookings).hasSize(6);
    }

    @Test
    void findLastBookingTest() {
        List<BookingShort> bookings = bookingRepository.findLastBooking(1L);

        assertThat(bookings).hasSize(1)
                .extracting(BookingShort::getId)
                .containsExactly(3L);
    }

    @Test
    void findNextBookingTest() {
        List<BookingShort> bookings = bookingRepository.findNextBooking(1L);

        assertThat(bookings).hasSize(1)
                .extracting(BookingShort::getId)
                .containsExactly(4L);
    }

    @Test
    void checkIfUserBookedItemTest() {
        assertThat(bookingRepository.checkIfUserBookedItem(6L, 3L)).isTrue();
    }
}