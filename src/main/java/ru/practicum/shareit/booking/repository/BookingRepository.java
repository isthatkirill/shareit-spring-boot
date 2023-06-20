package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long id);

    @Query("SELECT b FROM Booking b " +
            "WHERE (b.booker.id = ?1) " +
            "AND CURRENT_TIMESTAMP < b.start " +
            "ORDER BY b.start DESC"
    )
    List<Booking> findFutureBookings(Long id);

}
