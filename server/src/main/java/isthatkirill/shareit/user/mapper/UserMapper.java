package isthatkirill.shareit.user.mapper;

import org.mapstruct.Mapper;
import isthatkirill.shareit.user.dto.UserDto;
import isthatkirill.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserDto userDto);

    UserDto toUserDto(User user);

    List<UserDto> toUserDto(List<User> users);
}
