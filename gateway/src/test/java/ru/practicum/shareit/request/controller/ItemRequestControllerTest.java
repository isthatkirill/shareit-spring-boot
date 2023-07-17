package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoLong;
import ru.practicum.shareit.request.service.ItemRequestService;

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
    private ItemRequestService itemRequestService;

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

        when(itemRequestService.create(any(), anyLong()))
                .thenReturn(dtoLong);

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

        verify(itemRequestService, times(1)).create(dto, userId);
    }

    @Test
    @SneakyThrows
    void createWithBlankFiledTest() {
        Long userId = 1L;
        dto.setDescription("");

        when(itemRequestService.create(any(), anyLong()))
                .thenReturn(dtoLong);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("Description cannot be null"));

        verify(itemRequestService, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createWithoutHeaderTest() {
        when(itemRequestService.create(any(), anyLong()))
                .thenReturn(dtoLong);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(dto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(itemRequestService, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void getAllTest() {
        int from = 0, size = 5;
        Long userId = 1L;

        when(itemRequestService.getAll(anyInt(), anyInt(), anyLong()))
                .thenReturn(List.of(dtoLong));

        mvc.perform(get("/requests/all?from={from}&size={size}", from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dtoLong.getId()))
                .andExpect(jsonPath("$[0].created").hasJsonPath())
                .andExpect(jsonPath("$[0].description").value(dtoLong.getDescription()));

        verify(itemRequestService, times(1)).getAll(from, size, userId);
    }

    @Test
    @SneakyThrows
    void getAllWithBadRequestParamsTest() {
        int from = -10, size = -15;
        Long userId = 1L;

        when(itemRequestService.getAll(anyInt(), anyInt(), anyLong()))
                .thenReturn(List.of(dtoLong));

        mvc.perform(get("/requests/all?from={from}&size={size}", from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"));

        verify(itemRequestService, never()).getAll(anyInt(), anyInt(), anyLong());
    }

    @Test
    @SneakyThrows
    void getAllWithMissingHeaderTest() {
        int from = 0, size = 10;

        when(itemRequestService.getAll(anyInt(), anyInt(), anyLong()))
                .thenReturn(List.of(dtoLong));

        mvc.perform(get("/requests/all?from={from}&size={size}", from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(itemRequestService, never()).getAll(anyInt(), anyInt(), anyLong());
    }

    @Test
    @SneakyThrows
    void getOwnTest() {
        Long userId = 1L;

        when(itemRequestService.getOwn(anyLong()))
                .thenReturn(List.of(dtoLong));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(dtoLong.getId()))
                .andExpect(jsonPath("$[0].created").hasJsonPath())
                .andExpect(jsonPath("$[0].description").value(dtoLong.getDescription()));

        verify(itemRequestService, times(1)).getOwn(userId);
    }

    @Test
    @SneakyThrows
    void getOwnWithMissingHeaderTest() {
        when(itemRequestService.getOwn(anyLong()))
                .thenReturn(List.of(dtoLong));

        mvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(itemRequestService, never()).getOwn(anyLong());
    }

    @Test
    @SneakyThrows
    void getByIdTest() {
        Long userId = 1L;
        Long requestId = 2L;

        when(itemRequestService.getById(anyLong(), anyLong()))
                .thenReturn(dtoLong);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dtoLong.getId()))
                .andExpect(jsonPath("$.created").hasJsonPath())
                .andExpect(jsonPath("$.description").value(dtoLong.getDescription()));

        verify(itemRequestService, times(1)).getById(requestId, userId);
    }

    @Test
    @SneakyThrows
    void getByIdWithMissingRequestHeaderTest() {
        Long requestId = 2L;

        when(itemRequestService.getById(anyLong(), anyLong()))
                .thenReturn(dtoLong);

        mvc.perform(get("/requests/{requestId}", requestId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(itemRequestService, never()).getById(anyLong(), anyLong());
    }

}