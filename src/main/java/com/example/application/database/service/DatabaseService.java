package com.example.application.database.service;

import java.util.List;

public interface DatabaseService <T> {

    T insert(T object);

    List<T> selectAll();

    T select(int id);

    List<T> selectByName(String name);

    boolean isPresent(String name);

    T update(T object, int id);

    void delete(int id);

    void deleteAll();
}
