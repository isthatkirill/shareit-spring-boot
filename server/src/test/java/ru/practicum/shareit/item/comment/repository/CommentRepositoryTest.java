package ru.practicum.shareit.item.comment.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.comment.model.Comment;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase
@Sql(value = {"/test-schema.sql", "/test-users.sql", "/test-items.sql", "/test-comments-2.sql"})
class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;

    @Test
    void findByItemIdTest() {
        List<Comment> comments = commentRepository.findByItemId(2L);

        assertThat(comments).hasSize(2);
        assertThat(comments.get(0))
                .hasFieldOrPropertyWithValue("text", "comment1");
        assertThat(comments.get(1))
                .hasFieldOrPropertyWithValue("text", "comment3");
    }

    @Test
    void findByItemIdEmptyTest() {
        List<Comment> comments = commentRepository.findByItemId(3L);

        assertThat(comments).isEmpty();
    }

}