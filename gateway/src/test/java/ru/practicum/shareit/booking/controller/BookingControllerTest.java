package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDtoRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    private BookingDtoRequest bookingDtoRequest;
    private BookingDtoResponse bookingDtoResponse;

    @BeforeEach
    void buildBooking() {
        bookingDtoResponse = BookingDtoResponse.builder()
                .id(1L)
                .booker(UserDto.builder().id(1L).name("username").email("test@email.ru").build())
                .item(ItemDtoRequest.builder().id(1L).name("itemname").description("desc").build())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(Status.WAITING)
                .build();

        bookingDtoRequest = BookingDtoRequest.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
    }

    @Test
    @SneakyThrows
    void createTest() {
        when(bookingService.create(any(), anyLong())).thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDtoResponse.getId()))
                .andExpect(jsonPath("$.booker").value(bookingDtoResponse.getBooker()))
                .andExpect(jsonPath("$.item").value(bookingDtoResponse.getItem()))
                .andExpect(jsonPath("$.start").hasJsonPath())
                .andExpect(jsonPath("$.end").hasJsonPath())
                .andExpect(jsonPath("$.status").value(bookingDtoResponse.getStatus().name()));
    }

    @Test
    @SneakyThrows
    void createNullStartTest() {
        bookingDtoRequest.setStart(null);
        when(bookingService.create(any(), anyLong())).thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("Start of booking cannot be null"));

        verify(bookingService, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createNullEndTest() {
        bookingDtoRequest.setEnd(null);
        when(bookingService.create(any(), anyLong())).thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("End of booking cannot be null"));

        verify(bookingService, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createStartInPastTest() {
        bookingDtoRequest.setStart(LocalDateTime.now().minusDays(1));
        when(bookingService.create(any(), anyLong())).thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("Start of booking cannot be in past"));

        verify(bookingService, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createEndInPastTest() {
        bookingDtoRequest.setEnd(LocalDateTime.now().minusDays(1));
        when(bookingService.create(any(), anyLong())).thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("End of booking cannot be in past"));

        verify(bookingService, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createEndEarlierThanStartTest() {
        bookingDtoRequest.setEnd(LocalDateTime.now().plusDays(1));
        bookingDtoRequest.setStart(LocalDateTime.now().plusDays(2));
        when(bookingService.create(any(), anyLong())).thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("End of booking cannot be earlier than start"));

        verify(bookingService, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createEndEqualsStartTest() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);

        bookingDtoRequest.setEnd(time);
        bookingDtoRequest.setStart(time);
        when(bookingService.create(any(), anyLong())).thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("End of booking cannot equals start"));

        verify(bookingService, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createWithMissingHeaderTest() {
        when(bookingService.create(any(), anyLong())).thenReturn(bookingDtoResponse);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(bookingService, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void approveTest() {
        Long bookingId = 1L;
        boolean approved = true;

        when(bookingService.approve(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(patch("/bookings/{bookingId}?approved={approved}", bookingId, approved)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDtoResponse.getId()))
                .andExpect(jsonPath("$.booker").value(bookingDtoResponse.getBooker()))
                .andExpect(jsonPath("$.item").value(bookingDtoResponse.getItem()))
                .andExpect(jsonPath("$.start").hasJsonPath())
                .andExpect(jsonPath("$.end").hasJsonPath())
                .andExpect(jsonPath("$.status").value(bookingDtoResponse.getStatus().name()));

        verify(bookingService, times(1)).approve(1L, bookingId, approved);
    }

    @Test
    @SneakyThrows
    void approveMissingRequestParamTest() {
        Long bookingId = 1L;

        when(bookingService.approve(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request parameter"));

        verify(bookingService, never()).approve(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    @SneakyThrows
    void approveMissingRequestHeaderTest() {
        Long bookingId = 1L;
        boolean approved = true;

        when(bookingService.approve(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDtoResponse);

        mvc.perform(patch("/bookings/{bookingId}?approved={approved}", bookingId, approved)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(bookingService, never()).approve(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    @SneakyThrows
    void getByIdTest() {
        Long bookingId = 1L;

        when(bookingService.getById(anyLong(), anyLong())).thenReturn(bookingDtoResponse);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDtoResponse.getId()))
                .andExpect(jsonPath("$.booker").value(bookingDtoResponse.getBooker()))
                .andExpect(jsonPath("$.item").value(bookingDtoResponse.getItem()))
                .andExpect(jsonPath("$.start").hasJsonPath())
                .andExpect(jsonPath("$.end").hasJsonPath())
                .andExpect(jsonPath("$.status").value(bookingDtoResponse.getStatus().name()));

        verify(bookingService, times(1)).getById(bookingId, 1L);
    }

    @Test
    @SneakyThrows
    void getByIdMissingRequestHeaderTest() {
        Long bookingId = 1L;

        when(bookingService.getById(anyLong(), anyLong())).thenReturn(bookingDtoResponse);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(bookingService, never()).getById(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getByBookerIdTest() {
        String state = "FUTURE";
        int from = 1, size = 4;

        when(bookingService.getByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings?state={state}&from={from}&size={size}", state, from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDtoResponse.getId()))
                .andExpect(jsonPath("$[0].booker").value(bookingDtoResponse.getBooker()))
                .andExpect(jsonPath("$[0].item").value(bookingDtoResponse.getItem()))
                .andExpect(jsonPath("$[0].start").hasJsonPath())
                .andExpect(jsonPath("$[0].end").hasJsonPath())
                .andExpect(jsonPath("$[0].status").value(bookingDtoResponse.getStatus().name()));

        verify(bookingService, times(1)).getByBookerId(1L, state, from, size);
    }

    @Test
    @SneakyThrows
    void getByBookerIdDefaultParamsTest() {
        when(bookingService.getByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDtoResponse.getId()))
                .andExpect(jsonPath("$[0].booker").value(bookingDtoResponse.getBooker()))
                .andExpect(jsonPath("$[0].item").value(bookingDtoResponse.getItem()))
                .andExpect(jsonPath("$[0].start").hasJsonPath())
                .andExpect(jsonPath("$[0].end").hasJsonPath())
                .andExpect(jsonPath("$[0].status").value(bookingDtoResponse.getStatus().name()));

        verify(bookingService, times(1)).getByBookerId(1L, "ALL", 0, 10);
    }

    @Test
    @SneakyThrows
    void getByBookerIdInvalidStartTest() {
        int from = -1;

        when(bookingService.getByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings?from={from}", from)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"));

        verify(bookingService, never()).getByBookerId(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getByBookerIdMissingRequestHeaderTest() {
        when(bookingService.getByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(bookingService, never()).getByBookerId(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getByOwnerIdTest() {
        String state = "FUTURE";
        int from = 1, size = 4;

        when(bookingService.getByOwnerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings/owner?state={state}&from={from}&size={size}", state, from, size)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDtoResponse.getId()))
                .andExpect(jsonPath("$[0].booker").value(bookingDtoResponse.getBooker()))
                .andExpect(jsonPath("$[0].item").value(bookingDtoResponse.getItem()))
                .andExpect(jsonPath("$[0].start").hasJsonPath())
                .andExpect(jsonPath("$[0].end").hasJsonPath())
                .andExpect(jsonPath("$[0].status").value(bookingDtoResponse.getStatus().name()));

        verify(bookingService, times(1)).getByOwnerId(1L, state, from, size);
    }

    @Test
    @SneakyThrows
    void getByOwnerIdDefaultParamsTest() {
        when(bookingService.getByOwnerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDtoResponse.getId()))
                .andExpect(jsonPath("$[0].booker").value(bookingDtoResponse.getBooker()))
                .andExpect(jsonPath("$[0].item").value(bookingDtoResponse.getItem()))
                .andExpect(jsonPath("$[0].start").hasJsonPath())
                .andExpect(jsonPath("$[0].end").hasJsonPath())
                .andExpect(jsonPath("$[0].status").value(bookingDtoResponse.getStatus().name()));

        verify(bookingService, times(1)).getByOwnerId(1L, "ALL", 0, 10);
    }

    @Test
    @SneakyThrows
    void getByOwnerIdInvalidStartTest() {
        int from = -1;

        when(bookingService.getByOwnerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings/owner?from={from}", from)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"));

        verify(bookingService, never()).getByOwnerId(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getByOwnerIdMissingRequestHeaderTest() {
        when(bookingService.getByOwnerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDtoResponse));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(bookingService, never()).getByOwnerId(anyLong(), anyString(), anyInt(), anyInt());
    }

}