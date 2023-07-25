package isthatkirill.shareit.item.comment.mapper;

import isthatkirill.shareit.item.comment.dto.CommentDtoResponse;
import isthatkirill.shareit.item.comment.model.Comment;
import isthatkirill.shareit.user.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import isthatkirill.shareit.item.model.Item;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {

    private final CommentMapper mapper = Mappers.getMapper(CommentMapper.class);

    @Test
    void toComment() {
        String text = "commentTestText";

        Item item = Item.builder()
                .id(1L)
                .name("testName")
                .description("testDescription")
                .available(true)
                .build();

        User author = User.builder()
                .id(1L)
                .name("userName")
                .email("useremail@yandex.ru")
                .build();

        Comment comment = mapper.toComment(text, item, author);

        assertThat(comment).isNotNull()
                .hasFieldOrPropertyWithValue("text", text)
                .hasFieldOrPropertyWithValue("item.id", item.getId())
                .hasFieldOrPropertyWithValue("item.name", item.getName())
                .hasFieldOrPropertyWithValue("item.description", item.getDescription())
                .hasFieldOrPropertyWithValue("item.available", item.isAvailable())
                .hasFieldOrPropertyWithValue("author.id", author.getId())
                .hasFieldOrPropertyWithValue("author.name", author.getName())
                .hasFieldOrPropertyWithValue("author.email", author.getEmail());
    }

    @Test
    void toCommentDtoResponse() {
        Comment comment = Comment.builder()
                .id(3L)
                .text("testText")
                .author(new User(1L, "authorName", "authorEmail@yahoo.com"))
                .created(LocalDateTime.now())
                .build();

        CommentDtoResponse commentDto = mapper.toCommentDtoResponse(comment);

        assertThat(commentDto).isNotNull()
                .hasFieldOrPropertyWithValue("id", comment.getId())
                .hasFieldOrPropertyWithValue("text", comment.getText())
                .hasFieldOrPropertyWithValue("authorName", comment.getAuthor().getName())
                .hasFieldOrPropertyWithValue("created", comment.getCreated());
    }

}