package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.exception.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    void createTest() {
        UserDto userDto = UserDto.builder()
                .name("testName")
                .email("testemail@yahoo.com")
                .build();
        userDto = userService.create(userDto);

        assertThat(userService.create(userDto))
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "testName")
                .hasFieldOrPropertyWithValue("email", "testemail@yahoo.com");
    }

    @Test
    @Order(2)
    void updateTest() {
        UserDto userDto = UserDto.builder()
                .name("newTestName")
                .build();
        userDto = userService.update(userDto, 1L);

        assertThat(userDto)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "newTestName")
                .hasFieldOrPropertyWithValue("email", "testemail@yahoo.com");
    }

    @Test
    @Order(3)
    void updateNonExistentTest() {
        UserDto userDto = UserDto.builder()
                .name("newTestName")
                .build();

        Throwable e = assertThrows(NotFoundException.class, () -> {
            userService.update(userDto, 999L);
        });

        assertThat(e).hasMessage("Entity User not found. Id=999");
    }

    @Test
    @Order(4)
    void getById() {
        assertThat(userService.getById(1L))
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "newTestName")
                .hasFieldOrPropertyWithValue("email", "testemail@yahoo.com");
    }

    @Test
    @Order(5)
    void getByInvalidId() {
        Throwable e = assertThrows(NotFoundException.class, () -> {
            userService.getById(999L);
        });

        assertThat(e).hasMessage("Entity User not found. Id=999");
    }

    @Test
    @Order(6)
    void delete() {
        userService.delete(1L);

        assertThat(userService.getAll())
                .isEmpty();
    }

    @Test
    @Order(7)
    void getAll() {
        UserDto userDto1 = UserDto.builder()
                .name("firstTestName")
                .email("firsttestemail@yahoo.com")
                .build();
        userService.create(userDto1);

        UserDto userDto2 = UserDto.builder()
                .name("secondTestName")
                .email("secondtestemail@yahoo.com")
                .build();
        userService.create(userDto2);

        userDto1.setId(2L);
        userDto2.setId(3L);

        assertThat(userService.getAll())
                .hasSize(2)
                .containsExactlyInAnyOrder(userDto1, userDto2);
    }

}