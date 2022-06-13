package ru.yandex.practicum.filmorate.service;

public class FilmIdGen {
    private static int id=0;
    public static int genId() {
        return id++;
    }
}
