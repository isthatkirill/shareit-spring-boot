package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "SELECT * FROM items " +
            "WHERE (UPPER(description) LIKE UPPER(CONCAT('%', :searchTerm, '%')) OR " +
            "UPPER(name) LIKE UPPER(CONCAT('%', :searchTerm, '%'))) AND " +
            "is_available = true", nativeQuery = true)
    List<Item> search(@Param("searchTerm") String searchTerm, Pageable pageable);

    List<Item> findAllByOwnerIdOrderById(Long id, Pageable pageable);

}
