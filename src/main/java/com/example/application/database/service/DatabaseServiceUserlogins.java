package com.example.application.database.service;

import com.example.application.database.exception.UserNotFoundException;
import com.example.application.database.model.Userlogins;
import com.example.application.database.repository.DatabaseRepositoryUserlogins;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DatabaseServiceUserlogins implements DatabaseService{
    private final DatabaseRepositoryUserlogins repository;

    private static final AtomicInteger USER_ID_HOLDER = new AtomicInteger();

    public DatabaseServiceUserlogins(DatabaseRepositoryUserlogins repository) {
        this.repository = repository;
    }

    @Override
    public Object insert(Object object) {

        final int userId = USER_ID_HOLDER.incrementAndGet();

        return repository.save((Userlogins) object);
    }

    @Override
    public List<Object> selectAll() {
        return Collections.singletonList(repository.findAll());
    }

    @Override
    public Object select(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<Object> selectByName(String name) {
        return repository.findUserloginsByName(name);
    }

    @Override
    public boolean isPresent(String name) {
        System.out.println(name);
        return !repository.findUserloginsByName(name).isEmpty();
    }

    @Override
    public Object update(Object newUserlogins, int id) {
        return repository.findById(id)
                .map(repository::save)
                .orElseGet(() -> {
                    final int userId = USER_ID_HOLDER.incrementAndGet();
                    return repository.save((Userlogins) newUserlogins);
                });
    }

    @Override
    public void delete(int id) {
        if (repository.findById(id).isPresent()){
            repository.deleteById(id);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
