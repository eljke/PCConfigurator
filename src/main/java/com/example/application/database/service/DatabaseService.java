package com.example.application.database.service;

import java.util.List;

public interface DatabaseService {

    Object insert(Object object);

    List<Object> selectAll();

    Object select(int id);

    List<Object> selectByName(String name);

    boolean isPresent(String name);

    Object update(Object object, int id);

    void delete(int id);

    void deleteAll();
}
