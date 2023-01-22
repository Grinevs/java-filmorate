package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) throws ValidationException, NotFoundException {
        userService.addUser(user);
        return user;
    }

    @PutMapping("/users")
    public User patchUser(@RequestBody User user) throws ValidationException, NotFoundException {
        userService.patchUser(user);
        return user;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUserList();
    }

    @GetMapping("/users/{id}")
    public User GetUserById(@PathVariable Integer id) throws NotFoundException {
        return userService.getUserById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId)
            throws ValidationException, NotFoundException {
        userService.addFriend(userService.getUserById(id), friendId);
    }

    @DeleteMapping ("/users/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId)
            throws ValidationException, NotFoundException {
        userService.removeFriend(userService.getUserById(id), friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable Integer id) throws NotFoundException {
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) throws NotFoundException {
        return userService.getCommonFriends(userService.getUserById(id), userService.getUserById(otherId));
    }
}
