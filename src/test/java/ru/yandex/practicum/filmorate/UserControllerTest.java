package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserIdGenerator;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserValidator;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {

    @Test
    public void createUser() throws ValidationException, ExistException, NotFoundException {
        User user = new User();
        user.setName("Name");
        user.setLogin("Name");
        user.setEmail("test@test.com");
        user.setBirthday(LocalDate.of(1999,01,01));
        UserValidator userValidator = new UserValidator();
        UserIdGenerator userIdGenerator = new UserIdGenerator();
        InMemoryUserStorage userStorage = new InMemoryUserStorage(userIdGenerator, userValidator);
        UserService userService = new UserService(userStorage);
        UserController userController = new UserController(userService);
        userController.addUser(user);
        assertEquals(1, userController.getUsers().size());
    }

    @Test
    public void invalidLogin()  {
        User user = new User();
        user.setName("Name");
        user.setLogin("");
        user.setEmail("test@test.com");
        user.setBirthday(LocalDate.of(1999,01,01));
        UserValidator userValidator = new UserValidator();
        UserIdGenerator userIdGenerator = new UserIdGenerator();
        InMemoryUserStorage userStorage = new InMemoryUserStorage(userIdGenerator, userValidator);
        UserService userService = new UserService(userStorage);
        UserController userController = new UserController(userService);
        assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });
    }

    @Test
    public void invalidEmail()  {
        User user = new User();
        user.setName("Name");
        user.setLogin("Name");
        user.setEmail("test.com");
        user.setBirthday(LocalDate.of(1999,01,01));
        UserValidator userValidator = new UserValidator();
        UserIdGenerator userIdGenerator = new UserIdGenerator();
        InMemoryUserStorage userStorage = new InMemoryUserStorage(userIdGenerator, userValidator);
        UserService userService = new UserService(userStorage);
        UserController userController = new UserController(userService);
        assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });
    }

    @Test
    public void invalidBirthdayDate()  {
        User user = new User();
        user.setName("Name");
        user.setLogin("Name");
        user.setEmail("test@test.com");
        user.setBirthday(LocalDate.of(2100,01,01));
        UserValidator userValidator = new UserValidator();
        UserIdGenerator userIdGenerator = new UserIdGenerator();
        InMemoryUserStorage userStorage = new InMemoryUserStorage(userIdGenerator, userValidator);
        UserService userService = new UserService(userStorage);
        UserController userController = new UserController(userService);
        assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });
    }

    @Test
    public void invalidName() throws ValidationException, ExistException, NotFoundException {
        User user = new User();
        user.setName("");
        user.setLogin("Name");
        user.setEmail("test@test.com");
        user.setBirthday(LocalDate.of(2000,01,01));
        UserValidator userValidator = new UserValidator();
        UserIdGenerator userIdGenerator = new UserIdGenerator();
        InMemoryUserStorage userStorage = new InMemoryUserStorage(userIdGenerator, userValidator);
        UserService userService = new UserService(userStorage);
        UserController userController = new UserController(userService);
        userController.addUser(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    public void matchEmails() throws ValidationException, ExistException, NotFoundException {
        User user = new User();
        user.setName("1");
        user.setLogin("Name");
        user.setEmail("test@test.com");
        user.setBirthday(LocalDate.of(2000,01,01));
        User user1 = new User();
        user1.setName("2");
        user1.setLogin("Name");
        user1.setEmail("test@test.com");
        user1.setBirthday(LocalDate.of(2000,01,01));
        UserValidator userValidator = new UserValidator();
        UserIdGenerator userIdGenerator = new UserIdGenerator();
        InMemoryUserStorage userStorage = new InMemoryUserStorage(userIdGenerator, userValidator);
        UserService userService = new UserService(userStorage);
        UserController userController = new UserController(userService);
        userController.addUser(user);
        assertThrows(ExistException.class, () -> {
            userController.addUser(user1);
        });
    }
}
