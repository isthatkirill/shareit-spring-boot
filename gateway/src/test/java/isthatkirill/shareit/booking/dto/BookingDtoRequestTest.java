package isthatkirill.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoRequestTest {

    @Autowired
    private JacksonTester<BookingDtoRequest> json;

    private final BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder()
            .itemId(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusDays(1))
            .build();

    @Test
    @SneakyThrows
    void bookingDtoRequestTest() {
        JsonContent<BookingDtoRequest> result = json.write(bookingDtoRequest);

        assertThat(result)
                .hasJsonPathNumberValue("$.itemId", bookingDtoRequest.getItemId())
                .hasJsonPathStringValue("$.start", bookingDtoRequest.getStart())
                .hasJsonPathStringValue("$.end", bookingDtoRequest.getEnd());
    }

}