package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@Sql(value = {"/test-schema.sql", "/test-users.sql", "/test-items.sql"})
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void searchTest() {
        List<Item> items = itemRepository.search("oo", PageRequest.of(0, 10));

        assertThat(items).hasSize(2)
                .extracting(Item::getDescription)
                .containsExactlyInAnyOrder("google", "facebook");
    }

    @Test
    void findAllByOwnerIdOrderById() {
        List<Item> userOneItems = itemRepository.findAllByOwnerIdOrderById(1L, PageRequest.of(0, 10));
        List<Item> userTwoItems = itemRepository.findAllByOwnerIdOrderById(2L, PageRequest.of(0, 10));
        List<Item> userThreeItems = itemRepository.findAllByOwnerIdOrderById(3L, PageRequest.of(0, 10));

        assertThat(userOneItems)
                .hasSize(2)
                .extracting(Item::getDescription)
                .containsExactlyInAnyOrder("yandex", "google");


        assertThat(userTwoItems)
                .hasSize(2)
                .extracting(Item::getDescription)
                .containsExactlyInAnyOrder("twitter", "facebook");


        assertThat(userThreeItems)
                .hasSize(1)
                .extracting(Item::getDescription)
                .containsExactlyInAnyOrder("amazon");

    }

}