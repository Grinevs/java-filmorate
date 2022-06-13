package ru.yandex.practicum.filmorate.exception;

public class ExistException extends Exception{
    public ExistException(String message) {
        super("Обьект уже существует " + message);
    }
}
