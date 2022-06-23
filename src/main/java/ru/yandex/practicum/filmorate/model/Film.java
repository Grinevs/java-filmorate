package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;
    private String description;
    private String name;
    private LocalDate releaseDate;
    private int duration;
}
