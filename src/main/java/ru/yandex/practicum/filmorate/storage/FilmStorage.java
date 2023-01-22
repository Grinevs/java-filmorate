package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film) throws ValidationException, NotFoundException;

    Film patchFilm(Film film) throws NotFoundException, ValidationException;

    List<Film> getAllFilms();

    Film getFilmById(Integer id) throws NotFoundException;

    void checkIdFilm(Integer id) throws NotFoundException;
}
