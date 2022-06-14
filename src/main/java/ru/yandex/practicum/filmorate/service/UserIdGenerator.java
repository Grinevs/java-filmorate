package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;

@Service
public class UserIdGenerator {
    private int id = 1;

    public int genId() {
        return id++;
    }
}
