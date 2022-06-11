package com.example.application.database.exception;

public class MessageNotFoundException extends RuntimeException{
    public MessageNotFoundException(int id) {
        super("Не удалось найти сообщение с id = " + id);
    }
}
