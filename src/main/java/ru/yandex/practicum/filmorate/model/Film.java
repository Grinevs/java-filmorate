package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.service.FilmIdGen;

import java.time.Duration;
import java.time.LocalDateTime;

@Data
public class Film {
    private int id = FilmIdGen.genId();
    private String description;
    private String name;
    private LocalDateTime releaseDate;
    private Duration duration;



}
