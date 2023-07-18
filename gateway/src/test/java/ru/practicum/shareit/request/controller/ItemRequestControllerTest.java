package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoLong;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @MockBean
    private RequestClient requestClient;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private final ItemRequestDtoLong dtoLong = ItemRequestDtoLong.builder()
            .id(1L)
            .description("testDesc")
            .created(LocalDateTime.now())
            .build();

    private final ItemRequestDto dto = new ItemRequestDto("testDesc");

    @Test
    @SneakyThrows
    void createTest() {
        Long userId = 1L;

        when(requestClient.create(any(), anyLong()))
                .thenReturn(new ResponseEntity<>(dtoLong, HttpStatus.OK));

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dtoLong.getId()))
                .andExpect(jsonPath("$.created").hasJsonPath())
                .andExpect(jsonPath("$.description").value(dtoLong.getDescription()));

        verify(requestClient, times(1)).create(dto, userId);
    }

    @Test
    @SneakyThrows
    void createWithBlankFiledTest() {
        Long userId = 1L;
        dto.setDescription("");

        when(requestClient.create(any(), anyLong()))
                .thenReturn(new ResponseEntity<>(dtoLong, HttpStatus.BAD_REQUEST));

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("Description cannot be null"));

        verify(requestClient, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createWithoutHeaderTest() {
        when(requestClient.create(any(), anyLong()))
                .thenReturn(new ResponseEntity<>(dtoLong, HttpStatus.BAD_REQUEST));

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(requestClient, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void getAllTest() {
        int from = 0, size = 5;
        Long userId = 1L;

        when(requestClient.getAll(anyInt(), anyInt(), anyLong()))
                .thenReturn(new ResponseEntity<>(List.of(dtoLong), HttpStatus.OK));

        mvc.perform(get("/requests/all?from={from}&size={size}", from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dtoLong.getId()))
                .andExpect(jsonPath("$[0].created").hasJsonPath())
                .andExpect(jsonPath("$[0].description").value(dtoLong.getDescription()));

        verify(requestClient, times(1)).getAll(from, size, userId);
    }

    @Test
    @SneakyThrows
    void getAllWithBadRequestParamsTest() {
        int from = -10, size = -15;
        Long userId = 1L;

        when(requestClient.getAll(anyInt(), anyInt(), anyLong()))
                .thenReturn(new ResponseEntity<>(List.of(dtoLong), HttpStatus.BAD_REQUEST));

        mvc.perform(get("/requests/all?from={from}&size={size}", from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"));

        verify(requestClient, never()).getAll(anyInt(), anyInt(), anyLong());
    }

    @Test
    @SneakyThrows
    void getAllWithMissingHeaderTest() {
        int from = 0, size = 10;

        when(requestClient.getAll(anyInt(), anyInt(), anyLong()))
                .thenReturn(new ResponseEntity<>(List.of(dtoLong), HttpStatus.BAD_REQUEST));

        mvc.perform(get("/requests/all?from={from}&size={size}", from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(requestClient, never()).getAll(anyInt(), anyInt(), anyLong());
    }

    @Test
    @SneakyThrows
    void getOwnTest() {
        Long userId = 1L;

        when(requestClient.getOwn(anyLong()))
                .thenReturn(new ResponseEntity<>(List.of(dtoLong), HttpStatus.OK));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dtoLong.getId()))
                .andExpect(jsonPath("$[0].created").hasJsonPath())
                .andExpect(jsonPath("$[0].description").value(dtoLong.getDescription()));

        verify(requestClient, times(1)).getOwn(userId);
    }

    @Test
    @SneakyThrows
    void getOwnWithMissingHeaderTest() {
        when(requestClient.getOwn(anyLong()))
                .thenReturn(new ResponseEntity<>(List.of(dtoLong), HttpStatus.BAD_REQUEST));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(requestClient, never()).getOwn(anyLong());
    }

    @Test
    @SneakyThrows
    void getByIdTest() {
        Long userId = 1L;
        Long requestId = 2L;

        when(requestClient.getById(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>(dtoLong, HttpStatus.OK));

        mvc.perform(get("/requests/{requestId}", requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dtoLong.getId()))
                .andExpect(jsonPath("$.created").hasJsonPath())
                .andExpect(jsonPath("$.description").value(dtoLong.getDescription()));

        verify(requestClient, times(1)).getById(requestId, userId);
    }

    @Test
    @SneakyThrows
    void getByIdWithMissingRequestHeaderTest() {
        Long requestId = 2L;

        when(requestClient.getById(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>(dtoLong, HttpStatus.BAD_REQUEST));

        mvc.perform(get("/requests/{requestId}", requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(requestClient, never()).getById(anyLong(), anyLong());
    }

}