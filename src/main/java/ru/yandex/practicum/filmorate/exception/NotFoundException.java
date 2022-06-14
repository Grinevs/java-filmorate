package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NotFoundException extends Exception{
    public NotFoundException(String message) {
        super("Объект не найден " + message);
    }
}