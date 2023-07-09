package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    private ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("testDesc")
            .build();

    @Test
    @SneakyThrows
    void itemRequestDtoTest() {
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result)
                .hasJsonPathStringValue("$.description", itemRequestDto.getDescription());
    }

    @Test
    @SneakyThrows
    void itemRequestDtoWithNullFieldsTest() {
        itemRequestDto.setDescription(null);
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).hasEmptyJsonPathValue("$.description");
    }

}