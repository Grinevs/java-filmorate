package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

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
}
