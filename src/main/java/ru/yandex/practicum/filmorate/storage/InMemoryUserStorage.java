package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;
import ru.yandex.practicum.filmorate.service.UserValidator;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final UserIdGenerator userIdGenerator;
    private final UserValidator userValidator;
    private final Map<Integer, User> users = new HashMap<>();

    public InMemoryUserStorage(UserIdGenerator userIdGenerator, UserValidator userValidator) {
        this.userValidator = userValidator;
        this.userIdGenerator = userIdGenerator;
    }

    @Override
    public List<User> getUserList() {
        log.debug("Запрос на получение данных пользовталей");
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        log.debug("Запрос на добавление пользователя id={}, Email={}", user.getId(), user.getEmail());
        userValidator.validation(user);
        userValidator.checkExistEmailUser(user, users);
        user.setId(userIdGenerator.generateId());
        user.setFriendList(new HashSet<>());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен id={}", user.getId());
        return user;
    }

    @Override
    public User getUserById(Integer id) {
        log.debug("Запрос на получение данных пользовталя id={}", id);
        userValidator.checkExistId(id, users);
        return users.get(id);
    }

    @Override
    public User patchUser(User user) {
        log.debug("Запрос на изменения пользователья id={}, Email={}", user.getId(), user.getEmail());
        userValidator.validation(user);
        checkIdUser(user.getId());
        user.setFriendList(users.get(user.getId()).getFriendList());
        users.put(user.getId(), user);
        log.info("Пользователь изменен id={}", user.getId());
        return user;
    }

    @Override
    public void checkIdUser(Integer id) {
        log.debug("Запрос на существование id={}", id);
        userValidator.checkExistId(id, users);
    }

    @Override
    public void removeUser(Integer id) {
        log.debug("Запрос на удаление id={}", id);
        checkIdUser(id);
        users.remove(id);
    }
}
