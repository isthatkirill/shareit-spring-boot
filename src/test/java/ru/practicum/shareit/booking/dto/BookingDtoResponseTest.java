package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoResponseTest {

    @Autowired
    private JacksonTester<BookingDtoResponse> json;

    private final BookingDtoResponse bookingDtoResponse = BookingDtoResponse.builder()
            .id(1L)
            .item(ItemDtoRequest.builder()
                    .id(1L)
                    .name("item_name")
                    .description("item_desc")
                    .requestId(1L)
                    .available(true)
                    .build())
            .status(Status.WAITING)
            .booker(UserDto.builder()
                    .id(1L)
                    .name("user_name")
                    .email("test@email.ru")
                    .build())
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(1))
            .build();

    @Test
    @SneakyThrows
    void bookingDtoResponseTest() {
        JsonContent<BookingDtoResponse> result = json.write(bookingDtoResponse);

        assertThat(result)
                .hasJsonPathNumberValue("$.id", bookingDtoResponse.getId())
                .hasJsonPathStringValue("$.status", bookingDtoResponse.getStatus().name())
                .hasJsonPathStringValue("$.start", bookingDtoResponse.getStart())
                .hasJsonPathStringValue("$.end", bookingDtoResponse.getEnd())
                .hasJsonPathNumberValue("$.booker.id", bookingDtoResponse.getBooker().getId())
                .hasJsonPathStringValue("$.booker.name", bookingDtoResponse.getBooker().getName())
                .hasJsonPathNumberValue("$.item.id", bookingDtoResponse.getItem().getId())
                .hasJsonPathStringValue("$.item.name", bookingDtoResponse.getItem().getName())
                .hasJsonPathStringValue("$.item.description", bookingDtoResponse.getItem().getDescription());
    }

}