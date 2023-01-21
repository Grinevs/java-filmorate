package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUserList();

    User addUser(User user) throws ValidationException, NotFoundException;

    User getUserById(Integer id);

    User patchUser(User user) throws NotFoundException, ValidationException;
}
