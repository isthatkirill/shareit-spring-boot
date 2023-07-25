package isthatkirill.shareit.user.service;

import isthatkirill.shareit.user.dto.UserDto;
import isthatkirill.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, Long id);

    UserDto getById(Long id);

    List<UserDto> getAll();

    void delete(Long id);

    User checkUserExistentAndGet(Long id);
}
