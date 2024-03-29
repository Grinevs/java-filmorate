package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmIdGenerator;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest
public class FilmControllerTest {

    @Test
    public void createFilm() throws ValidationException, ExistException, NotFoundException {
        Film film = new Film();
        film.setName("film");
        film.setDescription("description");
        film.setReleaseDate((LocalDate.of(2020, 01, 01)));
        film.setDuration(120);
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmService);
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
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmService);
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
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmService);
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
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmService);
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
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage);
        FilmController filmController = new FilmController(filmService);
        assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
    }
}
