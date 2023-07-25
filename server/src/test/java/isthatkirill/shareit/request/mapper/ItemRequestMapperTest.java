package isthatkirill.shareit.request.mapper;

import isthatkirill.shareit.request.dto.ItemRequestDtoLong;
import isthatkirill.shareit.request.model.ItemRequest;
import isthatkirill.shareit.user.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import isthatkirill.shareit.item.model.Item;
import isthatkirill.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestMapperTest {

    private final ItemRequestMapper itemRequestMapper = Mappers.getMapper(ItemRequestMapper.class);

    @Test
    void toItemRequest() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("testDesc")
                .build();

        User user = User.builder()
                .id(1L)
                .name("testName")
                .email("test@email.ru")
                .build();

        ItemRequest itemRequest = itemRequestMapper.toItemRequest(user, itemRequestDto);

        assertThat(itemRequest).isNotNull()
                .hasFieldOrPropertyWithValue("description", itemRequestDto.getDescription())
                .hasFieldOrPropertyWithValue("requester", user)
                .hasFieldOrProperty("created");
    }

    @Test
    void toItemRequestDtoResponseTest() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("testDesc")
                .created(LocalDateTime.now())
                .requester(new User(1L, "testName", "test@email.com"))
                .items(Collections.singletonList(Item.builder().id(1L).build()))
                .build();

        ItemRequestDtoLong itemRequestDtoLong = itemRequestMapper.toItemRequestDtoLong(itemRequest);

        assertThat(itemRequestDtoLong).isNotNull()
                .hasFieldOrPropertyWithValue("id", itemRequest.getId())
                .hasFieldOrPropertyWithValue("description", itemRequest.getDescription())
                .hasFieldOrPropertyWithValue("created", itemRequest.getCreated())
                .hasFieldOrProperty("items");
    }

}