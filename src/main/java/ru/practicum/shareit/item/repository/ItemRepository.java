package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findItemsByDescriptionContainingIgnoreCaseOrNameContainingIgnoreCase(String desc,
                                                                                    String name);

    List<Item> findAllByOwner_Id(Long id);

}
