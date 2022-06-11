package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

@RestController
public class FilmController {
    @PostMapping(value = "/films")
    public Film addFilm(@RequestBody Film film) {

        return film;
    }

    @PatchMapping(value = "/films")
    public Film patchFilm(@RequestBody Film film) {

        return film;
    }

    @GetMapping("/films")
    public void getAllFilms() {

    }
}
