package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoRequestTest {

    @Autowired
    JacksonTester<ItemDtoRequest> json;

    private final ItemDtoRequest itemDtoRequest = ItemDtoRequest.builder()
            .id(1L)
            .name("testName")
            .description("testDescription")
            .available(true)
            .requestId(3L)
            .build();

    @Test
    @SneakyThrows
    void itemDtoRequestTest() {
        JsonContent<ItemDtoRequest> result = json.write(itemDtoRequest);

        assertThat(result)
                .hasJsonPathNumberValue("id", itemDtoRequest.getId())
                .hasJsonPathStringValue("name", itemDtoRequest.getName())
                .hasJsonPathStringValue("description", itemDtoRequest.getDescription())
                .hasJsonPathBooleanValue("available", itemDtoRequest.getAvailable())
                .hasJsonPathNumberValue("requestId", itemDtoRequest.getRequestId());
    }

}