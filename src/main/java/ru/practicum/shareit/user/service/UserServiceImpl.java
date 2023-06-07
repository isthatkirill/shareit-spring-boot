package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.util.exception.NotFoundException;
import ru.practicum.shareit.util.exception.UniqueEmailException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        checkIfEmailValid(user);
        log.info("User created: {}", userDto.getEmail());
        return UserMapper.toUserDto(userRepository.create(user));
    }

    @Override
    public UserDto update(UserDto userDto, Long id) {
        User oldUser = checkUserExistent(id);
        User newUser = UserMapper.toUser(userDto);
        newUser.setId(id);
        if (userDto.getName() != null) newUser.setName(userDto.getName());
        if (userDto.getEmail() != null) newUser.setEmail(userDto.getEmail());
        checkIfEmailValid(newUser);
        if (userDto.getName() != null) oldUser.setName(userDto.getName());
        if (userDto.getEmail() != null) oldUser.setEmail(userDto.getEmail());
        log.info("User updated: {}", id);
        return UserMapper.toUserDto(userRepository.update(oldUser));
    }

    @Override
    public UserDto getById(Long id) {
        log.info("Get user by id = {}", id);
        return UserMapper.toUserDto(checkUserExistent(id));
    }

    @Override
    public List<UserDto> getAll() {
        log.info("Get all users");
        return userRepository.getAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        userRepository.delete(id);
    }

    private void checkIfEmailValid(User user) {
        List<User> users = userRepository.getAll();
        if (users.stream().anyMatch(u -> u.getEmail().equals(user.getEmail())
                && !u.getId().equals(user.getId()))) {
            throw new UniqueEmailException("This email is already taken");
        }
    }

    @Override
    public User checkUserExistent(Long id) {
        return userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(User.class, "Id: " + id));
    }

}
