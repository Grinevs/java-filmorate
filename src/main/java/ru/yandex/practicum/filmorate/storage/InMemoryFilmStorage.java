package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmIdGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final FilmIdGenerator filmIdGenerator;
    private final Map<Integer, Film> films = new HashMap<>();

    public InMemoryFilmStorage() {
        this.filmIdGenerator = new FilmIdGenerator();
    }

    @Override
    public Film addFilm(Film film) {
        if (films.values().stream().anyMatch(f -> f.getName().equals(film.getName()))) {
            log.info("Фильм уже существует " + film.getName());
            throw new ExistException(film.getName());
        }
        film.setId(filmIdGenerator.generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film patchFilm(Film film) throws NotFoundException {
        if (!films.containsKey(film.getId())) {
            log.error("Несуществующий id={}", film.getId());
            throw new NotFoundException("Несуществующий id");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }
}
