package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends Exception{
    public NotFoundException(String message) {
        super("Объект не найден " + message);
    }
}
