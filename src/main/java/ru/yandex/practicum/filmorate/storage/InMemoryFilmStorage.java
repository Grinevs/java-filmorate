package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmIdGenerator;
import ru.yandex.practicum.filmorate.service.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final FilmIdGenerator filmIdGenerator;
    private final FilmValidator filmValidator;
    private final Map<Integer, Film> films = new HashMap<>();

    public InMemoryFilmStorage() {
        this.filmValidator = new FilmValidator();
        this.filmIdGenerator = new FilmIdGenerator();
    }

    @Override
    public Film addFilm(Film film) throws ValidationException, NotFoundException {
        log.debug("Запрос на добавление фильма id={}, Name={}", film.getId(), film.getName());
        filmValidator.validation(film);
        if (films.values().stream().anyMatch(f -> f.getName().equals(film.getName()))) {
            log.info("Фильм уже существует " + film.getName());
            throw new ExistException(film.getName());
        }
        film.setId(filmIdGenerator.generateId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен id={}, Name={}", film.getId() ,film.getName());
        return film;
    }

    @Override
    public Film patchFilm(Film film) throws NotFoundException, ValidationException {
        log.debug("Запрос на изменения фильма id={}, Name={}",film.getId(), film.getName());
        filmValidator.validation(film);
        if (!films.containsKey(film.getId())) {
            log.error("Несуществующий id={}", film.getId());
            throw new NotFoundException("Несуществующий id");
        }
        films.put(film.getId(), film);
        log.info("Фильм изменен id={}, Name={}", film.getId() ,film.getName());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.debug("Запрос на получение данных всех фильмов");
        return new ArrayList<>(films.values());
    }
}
