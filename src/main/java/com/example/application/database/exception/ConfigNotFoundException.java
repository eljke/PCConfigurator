package com.example.application.database.exception;

public class ConfigNotFoundException extends RuntimeException{
    public ConfigNotFoundException(int id) {
        super("Не удалось найти конфигурацию с id = " + id);
    }
}
