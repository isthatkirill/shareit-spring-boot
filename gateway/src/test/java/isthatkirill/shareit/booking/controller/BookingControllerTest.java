package isthatkirill.shareit.booking.controller;

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
import isthatkirill.shareit.booking.BookingClient;
import isthatkirill.shareit.booking.BookingController;
import isthatkirill.shareit.booking.dto.BookingDtoRequest;
import isthatkirill.shareit.booking.dto.BookingDtoResponse;
import isthatkirill.shareit.booking.dto.Status;
import isthatkirill.shareit.item.dto.ItemDtoRequest;
import isthatkirill.shareit.user.dto.UserDto;

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
    private BookingClient bookingClient;

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
        when(bookingClient.create(any(), anyLong())).thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.OK));

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
        when(bookingClient.create(any(), anyLong())).thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.BAD_REQUEST));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("Start of booking cannot be null"));

        verify(bookingClient, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createNullEndTest() {
        bookingDtoRequest.setEnd(null);
        when(bookingClient.create(any(), anyLong())).thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.BAD_REQUEST));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("End of booking cannot be null"));

        verify(bookingClient, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createStartInPastTest() {
        bookingDtoRequest.setStart(LocalDateTime.now().minusDays(1));
        when(bookingClient.create(any(), anyLong())).thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.BAD_REQUEST));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("Start of booking cannot be in past"));

        verify(bookingClient, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createEndInPastTest() {
        bookingDtoRequest.setEnd(LocalDateTime.now().minusDays(1));
        when(bookingClient.create(any(), anyLong())).thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.BAD_REQUEST));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("End of booking cannot be in past"));

        verify(bookingClient, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createEndEarlierThanStartTest() {
        bookingDtoRequest.setEnd(LocalDateTime.now().plusDays(1));
        bookingDtoRequest.setStart(LocalDateTime.now().plusDays(2));
        when(bookingClient.create(any(), anyLong())).thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.BAD_REQUEST));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("End of booking cannot be earlier than start"));

        verify(bookingClient, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createEndEqualsStartTest() {
        LocalDateTime time = LocalDateTime.now().plusDays(1);

        bookingDtoRequest.setEnd(time);
        bookingDtoRequest.setStart(time);
        when(bookingClient.create(any(), anyLong())).thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.BAD_REQUEST));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"))
                .andExpect(jsonPath("$.description").value("End of booking cannot equals start"));

        verify(bookingClient, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void createWithMissingHeaderTest() {
        when(bookingClient.create(any(), anyLong())).thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.BAD_REQUEST));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(bookingClient, never()).create(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void approveTest() {
        Long bookingId = 1L;
        boolean approved = true;

        when(bookingClient.approve(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.OK));

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

        verify(bookingClient, times(1)).approve(1L, bookingId, approved);
    }

    @Test
    @SneakyThrows
    void approveMissingRequestParamTest() {
        Long bookingId = 1L;

        when(bookingClient.approve(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.BAD_REQUEST));

        mvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request parameter"));

        verify(bookingClient, never()).approve(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    @SneakyThrows
    void approveMissingRequestHeaderTest() {
        Long bookingId = 1L;
        boolean approved = true;

        when(bookingClient.approve(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.BAD_REQUEST));

        mvc.perform(patch("/bookings/{bookingId}?approved={approved}", bookingId, approved)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(bookingClient, never()).approve(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    @SneakyThrows
    void getByIdTest() {
        Long bookingId = 1L;

        when(bookingClient.getById(anyLong(), anyLong())).thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.OK));

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

        verify(bookingClient, times(1)).getById(bookingId, 1L);
    }

    @Test
    @SneakyThrows
    void getByIdMissingRequestHeaderTest() {
        Long bookingId = 1L;

        when(bookingClient.getById(anyLong(), anyLong())).thenReturn(new ResponseEntity<>(bookingDtoResponse, HttpStatus.BAD_REQUEST));

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(bookingClient, never()).getById(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getByBookerIdTest() {
        String state = "FUTURE";
        int from = 1, size = 4;

        when(bookingClient.getByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(List.of(bookingDtoResponse), HttpStatus.OK));

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

        verify(bookingClient, times(1)).getByBookerId(1L, state, from, size);
    }

    @Test
    @SneakyThrows
    void getByBookerIdDefaultParamsTest() {
        when(bookingClient.getByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(List.of(bookingDtoResponse), HttpStatus.OK));

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

        verify(bookingClient, times(1)).getByBookerId(1L, "ALL", 0, 10);
    }

    @Test
    @SneakyThrows
    void getByBookerIdInvalidStartTest() {
        int from = -1;

        when(bookingClient.getByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(List.of(bookingDtoResponse), HttpStatus.BAD_REQUEST));

        mvc.perform(get("/bookings?from={from}", from)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"));

        verify(bookingClient, never()).getByBookerId(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getByBookerIdMissingRequestHeaderTest() {
        when(bookingClient.getByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(List.of(bookingDtoResponse), HttpStatus.BAD_REQUEST));

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(bookingClient, never()).getByBookerId(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getByOwnerIdTest() {
        String state = "FUTURE";
        int from = 1, size = 4;

        when(bookingClient.getByOwnerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(List.of(bookingDtoResponse), HttpStatus.OK));

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

        verify(bookingClient, times(1)).getByOwnerId(1L, state, from, size);
    }

    @Test
    @SneakyThrows
    void getByOwnerIdDefaultParamsTest() {
        when(bookingClient.getByOwnerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(List.of(bookingDtoResponse), HttpStatus.OK));

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

        verify(bookingClient, times(1)).getByOwnerId(1L, "ALL", 0, 10);
    }

    @Test
    @SneakyThrows
    void getByOwnerIdInvalidStartTest() {
        int from = -1;

        when(bookingClient.getByOwnerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(List.of(bookingDtoResponse), HttpStatus.BAD_REQUEST));

        mvc.perform(get("/bookings/owner?from={from}", from)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation error"));

        verify(bookingClient, never()).getByOwnerId(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getByOwnerIdMissingRequestHeaderTest() {
        when(bookingClient.getByOwnerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>(List.of(bookingDtoResponse), HttpStatus.BAD_REQUEST));

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Missing request header"));

        verify(bookingClient, never()).getByOwnerId(anyLong(), anyString(), anyInt(), anyInt());
    }

}