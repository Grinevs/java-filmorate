package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film patchFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(Integer id);

    void removeFilm(Integer id);

    void checkIdFilm(Integer id);
}
