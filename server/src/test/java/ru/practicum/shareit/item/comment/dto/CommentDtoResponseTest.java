package ru.practicum.shareit.item.comment.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoResponseTest {

    @Autowired
    JacksonTester<CommentDtoResponse> json;

    private final CommentDtoResponse commentDto = CommentDtoResponse.builder()
            .id(1L)
            .text("testText")
            .authorName("testAuthor")
            .created(LocalDateTime.now())
            .build();


    @Test
    @SneakyThrows
    void testCommentDtoResponse() {
        JsonContent<CommentDtoResponse> result = json.write(commentDto);

        assertThat(result)
                .hasJsonPathNumberValue("$.id", commentDto.getId())
                .hasJsonPathStringValue("$.text", commentDto.getText())
                .hasJsonPathStringValue("$.authorName", commentDto.getAuthorName())
                .hasJsonPathStringValue("$.created", commentDto.getCreated());
    }

    @Test
    @SneakyThrows
    void testCommentDtoResponseWithNullFields() {
        commentDto.setText(null);
        commentDto.setAuthorName(null);
        JsonContent<CommentDtoResponse> result = json.write(commentDto);

        assertThat(result)
                .hasJsonPathNumberValue("$.id", commentDto.getId())
                .hasJsonPathStringValue("$.created", commentDto.getCreated())
                .hasEmptyJsonPathValue("$.text")
                .hasEmptyJsonPathValue("$.authorName");
    }

}
