package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    private final Map<Integer, Integer> topFilms = new HashMap<>();

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) {
        filmStorage.addFilm(film);
        updateLikesCount(film);
        return film;
    }

    public Film patchFilm(Film film) {
        filmStorage.patchFilm(film);
        updateLikesCount(film);
        return film;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }


    public Film addlike(Film film, Integer userId) {
        log.info("Запос пользователя id={} на лайк фильму filmId={}", userId, film.getId());
        film.getListLikesId().add(userId);
        film.setRate(film.getRate() + film.getListLikesId().size());
        filmStorage.patchFilm(film);
        updateLikesCount(film);
        return film;
    }

    public Film removelike(Film film, Integer userId) {
        log.info("Запос пользователя id={} на удаление лайка фильму filmId={}", userId, film.getId());
        if (!film.getListLikesId().contains(userId)) {
            log.error("Несуществует id={}", userId);
            throw new NotFoundException("id несуществует");
        }
        film.getListLikesId().remove(userId);
        film.setRate(film.getRate() - film.getListLikesId().size());
        filmStorage.patchFilm(film);
        updateLikesCount(film);
        return film;
    }

    public void updateLikesCount(Film film) {
        topFilms.put(film.getId(), film.getRate());
    }

    public void removeFilm(Integer id) {
        filmStorage.removeFilm(id);
    }

    public List<Film> showTopTen(Integer count) {
        log.info("Запрос на топ count={} фильмов", count);
        return  filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingLong(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film GetFilmById(Integer id) throws NotFoundException {
        return filmStorage.getFilmById(id);
    }
}
