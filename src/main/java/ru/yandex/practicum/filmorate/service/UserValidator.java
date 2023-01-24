package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@Service
public class UserValidator {
    private final String AT = "@";
    private final String SPACE = " ";

    public void validation(User user) throws ValidationException, NotFoundException {
        if (user.getId() < 0) {
            log.error("Неверный id={}", user.getId());
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
        if (user.getName() == null || user.getName().isEmpty()) {
            log.info("Используется логин в качестве имени id={}", user.getId());
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Неверный формат ввода дня рождения id={}", user.getId());
            throw new ValidationException("Неверная дата рождения");
        }
    }

    public void checkExistId(Integer id, Map<Integer, User> users) {
        if (!users.containsKey(id)) {
            log.error("Несуществует id={}", id);
            throw new NotFoundException("id несуществует");
        }
    }

    public void checkExistEmailUser(User user, Map<Integer, User> users) {
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            log.info("Пользователь уже существует " + user.getEmail());
            throw new ExistException(user.getEmail());
        }
    }
}
