package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoLongTest {

    @Autowired
    private JacksonTester<ItemRequestDtoLong> json;

    private final ItemRequestDtoLong itemRequestDtoLong = ItemRequestDtoLong.builder()
            .id(1L)
            .description("testDesc")
            .created(LocalDateTime.now())
            .items(List.of(ItemDtoRequest.builder()
                    .id(1L)
                    .name("testName")
                    .description("testDescription")
                    .available(true)
                    .requestId(2L)
                    .build()))
            .build();

    @Test
    @SneakyThrows
    void itemRequestDtoResponseTest() {
        JsonContent<ItemRequestDtoLong> result = json.write(itemRequestDtoLong);

        assertThat(result)
                .hasJsonPathNumberValue("$.id", itemRequestDtoLong.getId())
                .hasJsonPathStringValue("$.description", itemRequestDtoLong.getDescription())
                .hasJsonPathStringValue("$.created", itemRequestDtoLong.getCreated())
                .hasJsonPathArrayValue("$.items", itemRequestDtoLong.getItems());
    }

    @Test
    @SneakyThrows
    void itemRequestDtoResponseWithNullFieldsTest() {
        itemRequestDtoLong.setItems(null);
        JsonContent<ItemRequestDtoLong> result = json.write(itemRequestDtoLong);

        assertThat(result)
                .hasJsonPathNumberValue("$.id", itemRequestDtoLong.getId())
                .hasJsonPathStringValue("$.description", itemRequestDtoLong.getDescription())
                .hasJsonPathStringValue("$.created", itemRequestDtoLong.getCreated())
                .hasEmptyJsonPathValue("$.items");
    }

}