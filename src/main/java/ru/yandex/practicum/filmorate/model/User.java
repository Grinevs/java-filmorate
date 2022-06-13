package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.service.UserIdGen;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class User {
    private int id = UserIdGen.genId();
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

}
