package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingShort;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND CURRENT_TIMESTAMP < b.start " +
            "ORDER BY b.start DESC"
    )
    List<Booking> findFutureBookingsByBooker(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND CURRENT_TIMESTAMP > b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookingsByBooker(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND CURRENT_TIMESTAMP < b.end " +
            "AND CURRENT_TIMESTAMP > b.start " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentBookingsByBooker(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status = 'WAITING' " +
            "ORDER BY b.start DESC")
    List<Booking> findWaitingBookingsByBooker(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = ?1 " +
            "AND b.status = 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> findRejectedBookingsByBooker(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND CURRENT_TIMESTAMP < b.start " +
            "ORDER BY b.start DESC"
    )
    List<Booking> findFutureBookingsByOwner(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND CURRENT_TIMESTAMP > b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookingsByOwner(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND CURRENT_TIMESTAMP < b.end " +
            "AND CURRENT_TIMESTAMP > b.start " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentBookingsByOwner(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = 'WAITING' " +
            "ORDER BY b.start DESC")
    List<Booking> findWaitingBookingsByOwner(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "AND b.status = 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> findRejectedBookingsByOwner(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = ?1 " +
            "ORDER BY b.start DESC")
    List<Booking> findAllBookingsByOwner(Long id);

    @Query("SELECT b.booker.id as bookerId, b.start as start, b.end as end, b.id as id FROM Booking b " +
            "WHERE b.item.id = ?1 " +
            "AND b.start < CURRENT_TIMESTAMP " +
            "AND b.status = 'APPROVED' " +
            "ORDER BY b.start DESC ")
    List<BookingShort> findLastBooking(Long itemId, Pageable pageable);

    @Query("SELECT b.booker.id as bookerId, b.start as start, b.end as end, b.id as id FROM Booking b " +
            "WHERE b.item.id = ?1 " +
            "AND b.start > CURRENT_TIMESTAMP " +
            "AND b.status = 'APPROVED' " +
            "ORDER BY b.start ASC ")
    List<BookingShort> findNextBooking(Long itemId, Pageable pageable);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.item.id = ?1 AND " +
            "b.booker.id = ?2 AND " +
            "b.status = 'APPROVED' AND " +
            "b.end < CURRENT_TIMESTAMP")
    boolean checkIfUserBookedItem(Long itemId, Long userId);


}
