package ru.yandex.practicum.filmorate.service;

public class UserIdGen {
    private static int id=0;
    public static int genId() {
        return id++;
    }
}
