package ru.yandex.practicum.filmorate.service;

public class UserIdGen {
    private static int id=1;
    public static int genId() {
        return id++;
    }
}
