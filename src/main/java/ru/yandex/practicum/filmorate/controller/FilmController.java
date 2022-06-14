package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmIdGen;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private final int MAX_DESCRIPTION_LENGTH = 200;
    private final LocalDate RELEASE_DATE = LocalDate.of(1895, 12, 28);

    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException, ExistException, NotFoundException {
        log.debug("Запрос на добавление фильма");
        validation(film);
        if (films.values().stream().anyMatch(f -> f.getName().equals(film.getName()))) {
            log.info("Фильм уже существует " + film.getName());
            throw new ExistException(film.getName());
        }
        film.setId(FilmIdGen.genId());
        films.put(film.getId(), film);
        log.info("film added");
        return film;
    }

    @PutMapping("/films")
    public Film patchFilm(@RequestBody Film film) throws NotFoundException, ValidationException {
        log.debug("Запрос на изменения фильма");
        validation(film);
        films.put(film.getId(), film);
        log.info("Film patched");
        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        log.debug("Запрос на получение данных всех фильмов");
        return new ArrayList<>(films.values());
    }

    public void validation(Film film) throws ValidationException, NotFoundException {
        if (film.getId() < 0) {
            log.error("Неверный id");
            throw new NotFoundException("Неверный id");
        }
        if (film.getName().isEmpty()) {
            log.error("Пустое имя");
            throw new ValidationException("Пустое имя");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            log.error("Описание длинной свыше " + MAX_DESCRIPTION_LENGTH);
            throw new ValidationException("Описание свыше 200 сомволов");
        }
        if (film.getReleaseDate().isBefore(RELEASE_DATE)) {
            log.error("Дата выхода раньше " + RELEASE_DATE);
            throw new ValidationException("Фильм должен быть новее " + RELEASE_DATE);
        }
        if (film.getDuration() < 0) {
            log.error("Неверная продолжительность фильма");
            throw new ValidationException("Продолжительсность фильма не задана");
        }
    }
}