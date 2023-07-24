package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> json;

    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .email("testemail@yahoo.com")
            .name("testName")
            .build();

    @Test
    @SneakyThrows
    void testUserDto() {
        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result)
                .hasJsonPathNumberValue("$.id", userDto.getId())
                .hasJsonPathStringValue("$.email", userDto.getEmail())
                .hasJsonPathStringValue("$.name", userDto.getName());
    }

    @Test
    @SneakyThrows
    void testUserDtoWithNullFields() {
        userDto.setName(null);
        userDto.setEmail(null);
        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result)
                .hasJsonPathNumberValue("$.id", userDto.getId())
                .hasEmptyJsonPathValue("$.email")
                .hasEmptyJsonPathValue("$.name");
    }

}