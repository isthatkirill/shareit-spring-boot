package isthatkirill.shareit.user.service;

import isthatkirill.shareit.user.dto.UserDto;
import isthatkirill.shareit.user.mapper.UserMapper;
import isthatkirill.shareit.user.model.User;
import isthatkirill.shareit.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceMockTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private final UserDto userDto = new UserDto();

    private final User user = new User();

    @Test
    void createUserTest() {
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toUser(any())).thenReturn(user);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        userService.create(userDto);

        verify(userMapper, times(1)).toUser(userDto);
        verify(userMapper, times(1)).toUserDto(user);
        verify(userRepository, times(1)).save(user);
        verifyNoMoreInteractions(userMapper, userRepository);
    }

    @Test
    void updateUserTest() {
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        userService.update(userDto, 1L);

        verify(userMapper, times(1)).toUserDto(user);
        verify(userRepository, times(1)).save(user);
        verify(userRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(userMapper, userRepository);
    }

    @Test
    void getUserByIdTest() {
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        userService.getById(1L);

        verify(userMapper, times(1)).toUserDto(user);
        verify(userRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(userMapper, userRepository);
    }

    @Test
    void getAllUsersTest() {
        when(userMapper.toUserDto(anyList())).thenReturn(List.of(userDto));
        when(userRepository.findAll()).thenReturn(List.of(user));

        userService.getAll();

        verify(userMapper, times(1)).toUserDto(List.of(user));
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userMapper, userRepository);
    }

    @Test
    void deleteUserByIdTest() {
        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(userMapper, userRepository);
    }

}