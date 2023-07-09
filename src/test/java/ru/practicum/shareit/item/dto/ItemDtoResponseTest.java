package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingShortImpl;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoResponseTest {

    @Autowired
    private JacksonTester<ItemDtoResponse> json;

    private final ItemDtoResponse itemDtoResponse = ItemDtoResponse.builder()
            .id(1L)
            .name("testName")
            .description("testDescription")
            .available(true)
            .nextBooking(new BookingShortImpl(
                    1L,
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(2),
                    1L))
            .lastBooking(new BookingShortImpl(
                    2L,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1),
                    2L))
            .comments(List.of(new CommentDtoResponse(
                    3L,
                    "testText",
                    "testAuthorName",
                    LocalDateTime.now().plusHours(12)
            )))
            .build();

    @Test
    @SneakyThrows
    void itemDtoResponseTest() {
        JsonContent<ItemDtoResponse> result = json.write(itemDtoResponse);

        assertThat(result)
                .hasJsonPathNumberValue("$.id", itemDtoResponse.getId())
                .hasJsonPathStringValue("$.name", itemDtoResponse.getName())
                .hasJsonPathStringValue("$.description", itemDtoResponse.getDescription())
                .hasJsonPathBooleanValue("$.available", itemDtoResponse.getAvailable())
                .hasJsonPathNumberValue("$.nextBooking.bookerId", itemDtoResponse.getNextBooking().getBookerId())
                .hasJsonPathStringValue("$.nextBooking.start", itemDtoResponse.getNextBooking().getStart())
                .hasJsonPathStringValue("$.nextBooking.end", itemDtoResponse.getNextBooking().getEnd())
                .hasJsonPathNumberValue("$.nextBooking.id", itemDtoResponse.getNextBooking().getId())
                .hasJsonPathNumberValue("$.lastBooking.bookerId", itemDtoResponse.getLastBooking().getBookerId())
                .hasJsonPathStringValue("$.lastBooking.start", itemDtoResponse.getLastBooking().getStart())
                .hasJsonPathStringValue("$.lastBooking.end", itemDtoResponse.getLastBooking().getEnd())
                .hasJsonPathNumberValue("$.lastBooking.id", itemDtoResponse.getLastBooking().getId())
                .hasJsonPathArrayValue("$.comments", itemDtoResponse.getComments());
    }

}