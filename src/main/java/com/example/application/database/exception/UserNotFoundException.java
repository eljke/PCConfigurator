package com.example.application.database.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(int id) {
        super("Не удалось найти пользователя с id = " + id);
    }
}
