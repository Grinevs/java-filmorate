package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.List;

@Slf4j
@RestController
public class FilmController {
    private final InMemoryFilmStorage filmStorage;
    public FilmController(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }


    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException, NotFoundException {
        filmStorage.addFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Film patchFilm(@RequestBody Film film) throws NotFoundException, ValidationException {
        filmStorage.patchFilm(film);

        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }


}
