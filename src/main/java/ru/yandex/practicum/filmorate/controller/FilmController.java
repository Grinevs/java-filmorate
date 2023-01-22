package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException, NotFoundException {
        filmService.addFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Film patchFilm(@RequestBody Film film) throws ValidationException, NotFoundException {
        filmService.patchFilm(film);
        return film;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film GetFilmById(@PathVariable Integer id) throws NotFoundException {
        return filmService.GetFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addRate(@PathVariable Integer id,
                        @PathVariable Integer userId) throws ValidationException, NotFoundException {
        filmService.addlike(filmService.GetFilmById(id), userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeRate(@PathVariable Integer id,
                           @PathVariable Integer userId) throws ValidationException, NotFoundException {
        filmService.removelike(filmService.GetFilmById(id), userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostRated(@RequestParam(defaultValue = "10") int count) {
        return filmService.showTopTen(count);
    }

}
