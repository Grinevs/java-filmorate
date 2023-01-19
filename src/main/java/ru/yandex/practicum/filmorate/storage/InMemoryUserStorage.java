package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final UserIdGenerator userIdGenerator;
    private final Map<Integer, User> users = new HashMap<>();

    public InMemoryUserStorage() {
        userIdGenerator = new UserIdGenerator();
    }

    @Override
    public List<User> getUserList() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        checkExistEmailUser(user);
        user.setId(userIdGenerator.generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User patchUser(User user) throws NotFoundException {
        checkIdUser(user);
        users.put(user.getId(), user);
        return user;
    }

    private void checkIdUser(User user) throws NotFoundException {
        if (!users.containsKey(user.getId())) {
            log.error("Несуществует id={}", user.getId());
            throw new NotFoundException("id несуществует");
        }
    }

    private void checkExistEmailUser(User user) {
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.info("Пользователь уже существует " + user.getEmail());
            throw new ExistException(user.getEmail());
        }
    }
}
