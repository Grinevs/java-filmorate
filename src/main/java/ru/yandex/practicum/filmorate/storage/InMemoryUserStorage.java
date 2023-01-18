package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private final UserIdGenerator userIdGenerator;
    private final Map<Integer, User> users = new HashMap<>();

    public InMemoryUserStorage(UserIdGenerator userIdGenerator) {
        this.userIdGenerator = userIdGenerator;
    }

    public void addUser(User user) {

        users.put(user.getId(), user);
    }

    public User checkUser(User user) {
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.info("Пользователь уже существует " + user.getEmail());
            throw new ExistException(user.getEmail());
        }

        return user;
    }

        public Map<Integer, User> getUsers() {
        return users;
    }
}
