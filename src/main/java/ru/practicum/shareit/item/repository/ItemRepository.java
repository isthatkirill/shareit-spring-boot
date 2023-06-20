package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "SELECT * FROM items " +
            "WHERE (UPPER(description) LIKE UPPER(CONCAT('%', :searchTerm, '%')) OR " +
            "UPPER(name) LIKE UPPER(CONCAT('%', :searchTerm, '%'))) AND " +
            "is_available = true", nativeQuery = true)
    List<Item> search(@Param("searchTerm") String searchTerm);

    List<Item> findAllByOwnerId(Long id);

    Optional<Item> findItemByIdEqualsAndAvailableIsTrue(Long id);

}
