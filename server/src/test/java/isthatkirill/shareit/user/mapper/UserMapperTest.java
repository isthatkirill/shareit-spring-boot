package isthatkirill.shareit.user.mapper;

import isthatkirill.shareit.user.dto.UserDto;
import isthatkirill.shareit.user.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toUserTest() {
        UserDto userDto = UserDto.builder()
                .name("testName")
                .email("testEmail@yahoo.com")
                .build();

        User user = mapper.toUser(userDto);

        assertThat(user)
                .hasFieldOrPropertyWithValue("name", userDto.getName())
                .hasFieldOrPropertyWithValue("email", userDto.getEmail());
    }

    @Test
    void toUserDtoTest() {
        User user = User.builder()
                .id(1L)
                .name("testName")
                .email("testEmail@yandex.ru")
                .build();

        UserDto userDto = mapper.toUserDto(user);

        assertThat(userDto)
                .hasFieldOrPropertyWithValue("id", user.getId())
                .hasFieldOrPropertyWithValue("name", user.getName())
                .hasFieldOrPropertyWithValue("email", user.getEmail());
    }

    @Test
    void toUserDtoListTest() {
        List<User> users = generate(5);
        List<UserDto> userDtos = mapper.toUserDto(users);

        assertThat(userDtos).hasSize(5);
        assertThat(userDtos.get(0)).isNotNull()
                .hasFieldOrPropertyWithValue("id", 0L)
                .hasFieldOrPropertyWithValue("name", "name0")
                .hasFieldOrPropertyWithValue("email", "email0@yandex.ru");
    }

    private List<User> generate(Integer count) {
        List<User> users = new ArrayList<>();
        for (long i = 0; i < count; i++) {
            users.add(new User(i, "name" + i, "email" + i + "@yandex.ru"));
        }
        return users;
    }
}