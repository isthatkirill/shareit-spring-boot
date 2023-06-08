package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final HashMap<Long, User> users = new HashMap<>();
    private Long id = 0L;

    @Override
    public User create(User user) {
        user.setId(tempGenerateId());
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(Long id) {
        return users.values().stream().filter(u -> u.getId().equals(id)).findAny();
    }

    @Override
    public void delete(Long id) {
        users.remove(id);
    }

    private Long tempGenerateId() {
        return ++id;
    }
}
