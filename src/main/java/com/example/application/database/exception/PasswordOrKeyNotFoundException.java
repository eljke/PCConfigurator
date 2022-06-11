package com.example.application.database.exception;

public class PasswordOrKeyNotFoundException extends RuntimeException{
    public PasswordOrKeyNotFoundException(int id) {
        super("Не удалось найти сообщение с id = " + id);
    }
}
