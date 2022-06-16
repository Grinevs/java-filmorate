package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

@Service
public class FilmIdGenerator {
    private int id = 1;

    public int generateId() {
        return id++;
    }
}
