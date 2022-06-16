package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class UserController {
    private final String AT = "@";
    private final String SPACE = " ";
    private final UserIdGenerator userIdGenerator;
    private final Map<Integer, User> users = new HashMap<>();

    public UserController(UserIdGenerator userIdGenerator) {
        this.userIdGenerator = userIdGenerator;
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) throws ValidationException, ExistException, NotFoundException {
        log.debug("Запрос на добавление пользователя id={}, Email={}",user.getId(), user.getEmail());
        validation(user);
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.info("Пользователь уже существует " + user.getEmail());
            throw new ExistException(user.getEmail());
        }
        user.setId(userIdGenerator.generateId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен id={}",user.getId());
        return user;
    }

    @PutMapping("/users")
    public User patchUser(@RequestBody User user) throws ValidationException, NotFoundException {
        log.debug("Запрос на изменения пользователья id={}, Email={}",user.getId(), user.getEmail());
        validation(user);
        users.put(user.getId(), user);
        log.info("Пользователь изменен id={}",user.getId());
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.debug("Запрос на получение данных пользовталей");
        return new ArrayList<>(users.values());
    }

    public void validation(User user) throws ValidationException, NotFoundException {
        if (user.getId() < 0) {
            log.error("Неверныйi id={}", user.getId());
            throw new NotFoundException("Неверный id");
        }
        if (user.getEmail().isEmpty() || !(user.getEmail().contains(AT))) {
            log.error("Неверный формат ввода Email " + user.getEmail());
            throw new ValidationException("Неверный почтовый адрес");
        }
        if (user.getLogin().isEmpty() || user.getLogin().contains(SPACE)) {
            log.error("Неверный формат ввода логина id={}", user.getId());
            throw new ValidationException("Неправильно введен логин");
        }
        if (user.getName().isEmpty()) {
            log.info("Используется логин в качестве имени id={}", user.getId());
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Неверный формат ввода дня рождения id={}", user.getId());
            throw new ValidationException("Неверная дата рождения");
        }
    }
}
