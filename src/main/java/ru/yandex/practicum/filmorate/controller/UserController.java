package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

@RestController
public class UserController {
    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        return user;
    }

    @PatchMapping(value = "/users")
    public User patchUser(@RequestBody User user)  {
        return user;
    }

    @GetMapping("/users")
    public void getUsers() {

    }
}
