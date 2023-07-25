package isthatkirill.shareit.request.repository;

import isthatkirill.shareit.request.model.ItemRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@Sql(value = {"/test-schema.sql","/test-users.sql", "/test-item-req.sql"})
class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void findAllByRequesterIdNotOrderByCreatedDescTest() {
        List<ItemRequest> itemRequests = itemRequestRepository
                .findAllByRequesterIdNotOrderByCreatedDesc(4L, PageRequest.of(0, 10));

        assertThat(itemRequests).hasSize(3)
                .extracting(ItemRequest::getDescription)
                .containsExactly("user 3 request", "user 3 request second", "user 2 request");
    }

    @Test
    void findAllByRequesterIdNotOrderByCreatedDescWithPageableTest() {
        List<ItemRequest> itemRequests = itemRequestRepository
                .findAllByRequesterIdNotOrderByCreatedDesc(4L, PageRequest.of(0, 2));

        assertThat(itemRequests).hasSize(2)
                .extracting(ItemRequest::getDescription)
                .containsExactly("user 3 request", "user 3 request second");
    }

    @Test
    void findAllByRequesterIdOrderByCreatedDesc() {
        List<ItemRequest> itemRequests = itemRequestRepository
                .findAllByRequesterIdOrderByCreatedDesc(3L);

        assertThat(itemRequests).hasSize(2)
                .extracting(ItemRequest::getDescription)
                .containsExactly("user 3 request", "user 3 request second");
    }
}