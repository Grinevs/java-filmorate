package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Slf4j
@Service
public class FilmService {
    private FilmStorage filmStorage;

    private Map<Integer, Integer> topFilms = new HashMap<>();

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addFilm(Film film) throws ValidationException, NotFoundException {
        filmStorage.addFilm(film);
        updateLikesCount(film);
        return film;
    }

    public Film patchFilm(Film film) throws ValidationException, NotFoundException {
        filmStorage.patchFilm(film);
        updateLikesCount(film);
        return film;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }


    public Film addlike(Film film, Integer userId) throws NotFoundException, ValidationException {
        film.getListLikesId().add(userId);
        film.setRate(film.getRate() + film.getListLikesId().size());
        filmStorage.patchFilm(film);
        updateLikesCount(film);
        return film;
    }

    public Film removelike(Film film, Integer userId) throws NotFoundException, ValidationException {
        if (!film.getListLikesId().contains(userId)) {
            log.error("Несуществует id={}", userId);
            throw new NotFoundException("id несуществует");
        }
        film.getListLikesId().remove(userId); /// Если ид сузестввуент
        film.setRate(film.getRate() - film.getListLikesId().size());
        filmStorage.patchFilm(film);
        updateLikesCount(film);
        return film;
    }

    public void updateLikesCount(Film film) {
        topFilms.put(film.getId(), film.getRate());
    }

    public List<Film> showTopTen(Integer count) {
        List<Film> topSortedFilms = new ArrayList<>();
        topFilms.entrySet().stream().sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed());
        topFilms.keySet().stream().forEach(f -> {
            try {
                topSortedFilms.add(filmStorage.getFilmById(f));
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        Collections.reverse(topSortedFilms);
        List<Film> topTenFilms = new ArrayList<>();
        for (int i = 0; (i < topSortedFilms.size() && i < count); i++) {
            topTenFilms.add(topSortedFilms.get(i));
        }
        log.info("Фильм изменен size={}, ", topSortedFilms);
        return topTenFilms;
    }

    public Film GetFilmById(Integer id) throws NotFoundException {
        return filmStorage.getFilmById(id);
    }
}
