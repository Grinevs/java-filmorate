package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
@Service
public class FilmValidator {
    private final int MAX_DESCRIPTION_LENGTH = 200;
    private final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public void validation(Film film) throws ValidationException, NotFoundException {
        if (film.getId() < 0) {
            log.error("Неверный id={}", film.getId());
            throw new NotFoundException("Неверный id");
        }
        if (film.getName().isEmpty()) {
            log.error("Пустое имя id={}", film.getId());
            throw new ValidationException("Пустое имя");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("Описание длинной свыше " + MAX_DESCRIPTION_LENGTH + " id={}", film.getId());
            throw new ValidationException("Описание свыше 200 сомволов");
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            log.error("Дата выхода раньше " + RELEASE_DATE + " id={}", film.getId());
            throw new ValidationException("Фильм должен быть новее " + RELEASE_DATE);
        }
        if (film.getDuration() < 0) {
            log.error("Неверная продолжительность фильма id={}", film.getId());
            throw new ValidationException("Продолжительсность фильма не задана");
        }
    }
}
