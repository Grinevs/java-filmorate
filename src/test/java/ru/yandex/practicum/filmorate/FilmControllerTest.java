package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {

    @Test
    public void createFilm() throws ValidationException, ExistException, NotFoundException {
        Film film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate((LocalDate.of(2020, 01, 01)));
        film.setDuration(120);
        FilmController filmController = new FilmController();
        filmController.addFilm(film);
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    public void invalidName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("description");
        film.setReleaseDate((LocalDate.of(2020, 01, 01)));
        film.setDuration(120);
        FilmController filmController = new FilmController();
        assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
    }

    @Test
    public void maxLengthDescriptionTest() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("description".repeat(100));
        film.setReleaseDate((LocalDate.of(2020, 01, 01)));
        film.setDuration(120);
        FilmController filmController = new FilmController();
        assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
    }

    @Test
    public void releaseDateTest() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate((LocalDate.of(1800, 01, 01)));
        film.setDuration(120);
        FilmController filmController = new FilmController();
        assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
    }

    @Test
    public void durationTest() {
        Film film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate((LocalDate.of(2020, 01, 01)));
        film.setDuration(-5);
        FilmController filmController = new FilmController();
        assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
    }
}
