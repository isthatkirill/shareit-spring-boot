package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.dto.CommentDtoRequest;
import ru.practicum.shareit.item.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private ItemDtoRequest itemDtoReq;
    private ItemDtoResponse itemDtoResp;

    @BeforeEach
    void buildItem() {
        itemDtoReq = ItemDtoRequest.builder()
                .id(1L)
                .name("testName")
                .description("testDescription")
                .requestId(1L)
                .available(false)
                .build();
        itemDtoResp = ItemDtoResponse.builder()
                .id(1L)
                .name("testName")
                .description("testDescription")
                .build();
    }

    @Test
    @SneakyThrows
    void createItemTest() {
        when(itemService.create(any(), anyLong()))
                .thenReturn(itemDtoReq);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoReq))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDtoReq.getId()))
                .andExpect(jsonPath("$.name").value(itemDtoReq.getName()))
                .andExpect(jsonPath("$.description").value(itemDtoReq.getDescription()))
                .andExpect(jsonPath("$.requestId").value(itemDtoReq.getRequestId()))
                .andExpect(jsonPath("$.available").value(itemDtoReq.getAvailable()));

        verify(itemService, times(1)).create(itemDtoReq, 1L);
    }

    @Test
    @SneakyThrows
    void createItemMissingHeaderTest() {
        when(itemService.create(any(), anyLong()))
                .thenReturn(itemDtoReq);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoReq))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(itemService, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createItemWithNullFieldsTest() {
        itemDtoReq.setAvailable(null);
        when(itemService.create(any(), anyLong()))
                .thenReturn(itemDtoReq);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoReq))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("Must equals true or false"));

        verify(itemService, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createItemWithBlankFieldsTest() {
        itemDtoReq.setName("");
        when(itemService.create(any(), anyLong()))
                .thenReturn(itemDtoReq);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoReq))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("Name cannot be empty or null"));

        verify(itemService, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void updateItemTest() {
        itemDtoReq.setName("newName");
        when(itemService.update(any(), anyLong(), anyLong()))
                .thenReturn(itemDtoReq);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDtoReq))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDtoReq.getId()))
                .andExpect(jsonPath("$.name").value(itemDtoReq.getName()))
                .andExpect(jsonPath("$.description").value(itemDtoReq.getDescription()))
                .andExpect(jsonPath("$.requestId").value(itemDtoReq.getRequestId()))
                .andExpect(jsonPath("$.available").value(itemDtoReq.getAvailable()));

        verify(itemService, times(1)).update(itemDtoReq, 1L, 1L);
    }

    @Test
    @SneakyThrows
    void updateItemWithNullFieldsShouldBeOkTest() {
        ItemDtoRequest nullFields = ItemDtoRequest.builder().description("testDescription").build();
        when(itemService.update(any(), anyLong(), anyLong()))
                .thenReturn(nullFields);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(nullFields))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(nullFields.getDescription()));

        verify(itemService, times(1)).update(nullFields, 1L, 1L);
    }

    @Test
    @SneakyThrows
    void updateItemMissingHeaderTest() {
        itemDtoReq.setDescription("newDescription");
        when(itemService.update(any(), anyLong(), anyLong()))
                .thenReturn(itemDtoReq);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDtoReq))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(itemService, never()).update(any(), anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getByIdTest() {
        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemDtoResp);

        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDtoResp.getId()))
                .andExpect(jsonPath("$.name").value(itemDtoResp.getName()));

        verify(itemService, times(1)).getById(1L, 1L);
    }

    @Test
    @SneakyThrows
    void getByIdMissingHeaderTest() {
        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemDtoResp);

        mvc.perform(get("/items/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(itemService, never()).getById(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getByOwnerTest() {
        int from = 3, size = 2;
        when(itemService.getByOwner(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDtoResp));

        mvc.perform(get("/items?from={from}&size={size}", from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDtoResp.getId()))
                .andExpect(jsonPath("$[0].description").value(itemDtoResp.getDescription()))
                .andExpect(jsonPath("$[0].name").value(itemDtoResp.getName()));

        verify(itemService, times(1)).getByOwner(1L, from, size);
    }

    @Test
    @SneakyThrows
    void getByOwnerWithInvalidRequestParamsTest() {
        int from = -3, size = 2;
        when(itemService.getByOwner(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDtoResp));

        mvc.perform(get("/items?from={from}&size={size}", from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"));

        verify(itemService, never()).getByOwner(anyLong(),anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getByOwnerWithDefaultRequestParamsTest() {
        when(itemService.getByOwner(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDtoResp));

        mvc.perform(get("/items")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDtoResp.getId()))
                .andExpect(jsonPath("$[0].description").value(itemDtoResp.getDescription()))
                .andExpect(jsonPath("$[0].name").value(itemDtoResp.getName()));

        verify(itemService, times(1)).getByOwner(1L, 0, 10);
    }

    @Test
    @SneakyThrows
    void searchTest() {
        String text = "text";
        int from = 3, size = 2;

        when(itemService.search(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDtoReq));

        mvc.perform(get("/items/search?text={text}&from={from}&size={size}",
                        text, from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDtoReq.getId()))
                .andExpect(jsonPath("$[0].description").value(itemDtoReq.getDescription()))
                .andExpect(jsonPath("$[0].name").value(itemDtoReq.getName()));

        verify(itemService, times(1)).search(text, from, size);
    }

    @Test
    @SneakyThrows
    void searchWithMissingTextParamTest() {
        when(itemService.search(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDtoReq));

        mvc.perform(get("/items/search")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request parameter"));

        verify(itemService, never()).search(anyString(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void createCommentTest() {
        CommentDtoRequest dtoRequest = CommentDtoRequest.builder()
                .text("testText")
                .build();

        CommentDtoResponse dtoResponse = CommentDtoResponse.builder()
                .id(1L)
                .authorName("testName")
                .text("testText")
                .created(LocalDateTime.now())
                .build();

        String pattern = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+";

        when(itemService.createComment(anyLong(), anyLong(),
                any(CommentDtoRequest.class))).thenReturn(dtoResponse);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(dtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dtoResponse.getId()))
                .andExpect(jsonPath("$.authorName").value(dtoResponse.getAuthorName()))
                .andExpect(jsonPath("$.text").value(dtoResponse.getText()))
                .andExpect(jsonPath("$.created", matchesPattern(pattern)));

        verify(itemService, times(1)).createComment(1L, 1L, dtoRequest);
    }

    @Test
    @SneakyThrows
    void createEmptyCommentTest() {
        CommentDtoRequest dtoRequest = CommentDtoRequest.builder()
                .text("")
                .build();

        CommentDtoResponse dtoResponse = CommentDtoResponse.builder()
                .id(1L)
                .authorName("testName")
                .text("")
                .created(LocalDateTime.now())
                .build();

        when(itemService.createComment(anyLong(), anyLong(),
                any(CommentDtoRequest.class))).thenReturn(dtoResponse);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(dtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"));

        verify(itemService, never()).createComment(anyLong(), anyLong(), any());
    }

    @Test
    @SneakyThrows
    void createCommentWithMissingHeaderTest() {
        CommentDtoRequest dtoRequest = CommentDtoRequest.builder()
                .text("testText")
                .build();

        CommentDtoResponse dtoResponse = CommentDtoResponse.builder()
                .id(1L)
                .authorName("testName")
                .text("testText")
                .created(LocalDateTime.now())
                .build();

        when(itemService.createComment(anyLong(), anyLong(),
                any(CommentDtoRequest.class))).thenReturn(dtoResponse);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(dtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(itemService, never()).createComment(anyLong(), anyLong(), any());
    }

}