package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;
    private Map<Film, Integer> topFilms = new HashMap<>();

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addlike(Film film, User user) throws NotFoundException {
        film.getListLikesId().add(user.getId());
        filmStorage.patchFilm(film);
        updateLikesCount(film);
        return film;
    }

    public Film removelike(Film film, User user) throws NotFoundException {
        film.getListLikesId().remove(user.getId());
        filmStorage.patchFilm(film);
        updateLikesCount(film);
        return film;
    }
     public void updateLikesCount(Film film) {
         topFilms.put(film, (int) film.getListLikesId().stream().count());
     }

     public void showTopTen() {
         Map<Film, Integer> sortedMap = topFilms.entrySet()
                 .stream()
                 .sorted(Map.Entry.comparingByValue())
                 .collect(Collectors
                         .toMap(Map.Entry::getKey,
                                 Map.Entry::getValue,
                                 (e1, e2) -> e1,
                                 LinkedHashMap::new));
     }
}
