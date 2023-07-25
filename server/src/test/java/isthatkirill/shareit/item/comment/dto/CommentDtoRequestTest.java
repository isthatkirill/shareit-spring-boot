package isthatkirill.shareit.item.comment.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoRequestTest {

    @Autowired
    JacksonTester<CommentDtoRequest> json;

    private final CommentDtoRequest commentDto = CommentDtoRequest.builder()
            .text("text")
            .build();

    @Test
    @SneakyThrows
    void testCommentDtoRequest() {
        JsonContent<CommentDtoRequest> result = json.write(commentDto);

        assertThat(result)
                .hasJsonPathStringValue("$.text", commentDto.getText());
    }

    @Test
    @SneakyThrows
    void testCommentDtoRequestWithNullFields() {
        commentDto.setText(null);
        JsonContent<CommentDtoRequest> result = json.write(commentDto);

        assertThat(result)
                .hasEmptyJsonPathValue("$.text");
    }

}
