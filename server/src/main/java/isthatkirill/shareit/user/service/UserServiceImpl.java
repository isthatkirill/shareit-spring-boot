package isthatkirill.shareit.user.service;

import isthatkirill.shareit.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import isthatkirill.shareit.user.dto.UserDto;
import isthatkirill.shareit.user.model.User;
import isthatkirill.shareit.user.repository.UserRepository;
import isthatkirill.shareit.util.exception.NotFoundException;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        log.info("User created: {}", userDto.getEmail());
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto, Long id) {
        User oldUser = checkUserExistentAndGet(id);
        if (userDto.getName() != null) oldUser.setName(userDto.getName());
        if (userDto.getEmail() != null) oldUser.setEmail(userDto.getEmail());
        log.info("User updated: {}", id);
        return userMapper.toUserDto(userRepository.save(oldUser));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        log.info("Get user by id = {}", id);
        return userMapper.toUserDto(checkUserExistentAndGet(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAll() {
        log.info("Get all users");
        return userMapper.toUserDto(userRepository.findAll());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Delete user id={}", id);
        userRepository.deleteById(id);
    }

    @Override
    public User checkUserExistentAndGet(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(User.class, "Id=" + id));
    }

}
