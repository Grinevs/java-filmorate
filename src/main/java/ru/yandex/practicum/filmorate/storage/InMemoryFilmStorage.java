package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmIdGenerator;
import ru.yandex.practicum.filmorate.service.FilmValidator;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final FilmIdGenerator filmIdGenerator;
    private final FilmValidator filmValidator;
    private final Map<Integer, Film> films = new HashMap<>();

    public InMemoryFilmStorage() {
        this.filmIdGenerator = new FilmIdGenerator();
        this.filmValidator = new FilmValidator();
    }

    @Override
    public Film addFilm(Film film) {
        log.debug("Запрос на добавление фильма id={}, Name={}", film.getId(), film.getName());
        filmValidator.validation(film);
        filmValidator.checkSameFilm(film, films);
        film.setId(filmIdGenerator.generateId());
        film.setListLikesId(new HashSet<>());
        films.put(film.getId(), film);
        log.info("Фильм добавлен id={}, Name={}", film.getId(), film.getName());
        return film;
    }

    @Override
    public Film patchFilm(Film film) {
        log.debug("Запрос на изменения фильма id={}, Name={}", film.getId(), film.getName());
        checkIdFilm(film.getId());
        filmValidator.validation(film);
        filmValidator.checkExistId(film, films);
        film.setListLikesId(films.get(film.getId()).getListLikesId());
        films.put(film.getId(), film);
        log.info("Фильм изменен id={}, Name={}", film.getId(), film.getName());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.debug("Запрос на получение данных всех фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer id) {
        log.debug("Запрос на существование id={}", id);
        checkIdFilm(id);
        return films.get(id);
    }

    @Override
    public void removeFilm(Integer id) {
        log.debug("Запрос на удаление фильма id={}", id);
        checkIdFilm(id);
        films.remove(id);
    }

    @Override
    public void checkIdFilm(Integer id) {
        if (!films.containsKey(id)) {
            log.error("Несуществует id={}", id);
            throw new NotFoundException("id несуществует");
        }
    }
}
