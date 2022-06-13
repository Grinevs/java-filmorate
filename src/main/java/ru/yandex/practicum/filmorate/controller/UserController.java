package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class UserController {
    public final String AT = "@";
    public final String SPACE = " ";

    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) throws ValidationException, ExistException {
        log.debug("Запрос на добавление пользователья");
        validation(user);
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.info("Пользователь уже существует " + user.getEmail());
            throw new ExistException(user.getEmail());
        }
        users.put(user.getId(), user);
        log.info("User added");
        return user;
    }

    @PatchMapping(value = "/users")
    public User patchUser(@RequestBody User user) throws ValidationException, NotFoundException {
        log.debug("Запрос на изменения пользователья");
        validation(user);
        if (users.values().stream().noneMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.info("Пользователь не найден " + user.getEmail());
            throw new NotFoundException(user.getEmail());
        }
        log.info("User patched");
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.debug("Запрос на получение данных пользовталей");
        return new ArrayList<>(users.values());
    }

    public void validation(User user) throws ValidationException {
        if (user.getEmail().isEmpty() || !user.getEmail().contains(AT)) {
            log.error("Неверный формат ввода Email");
            throw new ValidationException("Неверный почтовый адрес");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(SPACE)) {
            log.error("Неверный формат ввода логина");
            throw new ValidationException("Неправильно введен логин");
        }
        if (user.getName().isEmpty()) {
            log.info("Используется логин в качестве имени");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Неверный формат ввода дня рождения");
            throw new ValidationException("Неверная дата рождения");
        }
    }
}
