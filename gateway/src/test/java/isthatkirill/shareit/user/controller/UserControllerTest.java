package isthatkirill.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import isthatkirill.shareit.user.UserClient;
import isthatkirill.shareit.user.UserController;
import isthatkirill.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    private UserClient userClient;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private UserDto userDto;

    @BeforeEach
    void buildUser() {
        userDto = UserDto.builder()
                .id(1L)
                .name("testName")
                .email("testemail@yahoo.com")
                .build();
    }

    @Test
    @SneakyThrows
    void createNewUserTest() {
        when(userClient.create(any())).thenReturn(new ResponseEntity<>(userDto, HttpStatus.OK));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));

        verify(userClient, times(1)).create(userDto);
    }

    @Test
    @SneakyThrows
    void createNewUserWithInvalidEmailTest() {
        userDto.setEmail("testInvalidEmail");
        when(userClient.create(any())).thenReturn(new ResponseEntity<>(userDto, HttpStatus.BAD_REQUEST));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("Email must satisfy pattern"));

        verify(userClient, never()).create(any());
    }

    @Test
    @SneakyThrows
    void createNewUserWithNullNameTest() {
        userDto.setName(null);
        when(userClient.create(any())).thenReturn(new ResponseEntity<>(userDto, HttpStatus.BAD_REQUEST));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("Name cannot be empty or null"));

        verify(userClient, never()).create(any());
    }

    @Test
    @SneakyThrows
    void updateUserTest() {
        userDto.setName("newTestName");
        when(userClient.update(any(), anyLong())).thenReturn(new ResponseEntity<>(userDto, HttpStatus.OK));

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));

        verify(userClient, times(1)).update(userDto, 1L);
    }

    @Test
    @SneakyThrows
    void deleteUserTest() {
        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        verify(userClient, times(1)).delete(anyLong());
    }

    @Test
    @SneakyThrows
    void getUserByIdTest() {
        when(userClient.getById(anyLong())).thenReturn(new ResponseEntity<>(userDto, HttpStatus.OK));

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));

        verify(userClient, times(1)).getById(anyLong());
    }

    @Test
    @SneakyThrows
    void getAllUsersTest() {
        UserDto userDtoSecond = UserDto.builder()
                .id(2L)
                .name("testName2")
                .email("testemail2@yahoo.com")
                .build();
        when(userClient.getAll())
                .thenReturn(new ResponseEntity<>(List.of(userDto, userDtoSecond), HttpStatus.OK));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(userDto.getId()))
                .andExpect(jsonPath("$[0].name").value(userDto.getName()))
                .andExpect(jsonPath("$[0].email").value(userDto.getEmail()))
                .andExpect(jsonPath("$[1].id").value(userDtoSecond.getId()))
                .andExpect(jsonPath("$[1].name").value(userDtoSecond.getName()))
                .andExpect(jsonPath("$[1].email").value(userDtoSecond.getEmail()));

        verify(userClient, times(1)).getAll();
    }

}