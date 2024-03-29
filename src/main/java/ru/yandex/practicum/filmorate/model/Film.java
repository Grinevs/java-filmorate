package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    private int id;
    private String description;
    private String name;
    private LocalDate releaseDate;
    private int duration;
    private Set<Integer> listLikesId;
    private Integer rate = 0;
}
