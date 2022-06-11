package com.example.application.database.service;

import com.example.application.database.exception.PasswordOrKeyNotFoundException;
import com.example.application.database.exception.UserNotFoundException;
import com.example.application.database.model.PasswordSecretkeys;
import com.example.application.database.model.Userlogins;
import com.example.application.database.repository.DatabaseRepositoryMessagesHistory;
import com.example.application.database.repository.DatabaseRepositoryPasswordSecretkeys;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DatabaseServicePasswordSecretkeys implements DatabaseService{
    private final DatabaseRepositoryPasswordSecretkeys repository;

    private static final AtomicInteger PASSWORD_ID_HOLDER = new AtomicInteger();

    public DatabaseServicePasswordSecretkeys(DatabaseRepositoryPasswordSecretkeys repository) {
        this.repository = repository;
    }

    @Override
    public Object insert(Object object) {

        final int passwordId = PASSWORD_ID_HOLDER.incrementAndGet();

        return repository.save((PasswordSecretkeys) object);
    }

    @Override
    public List<Object> selectAll() {
        return Collections.singletonList(repository.findAll());
    }

    @Override
    public Object select(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new PasswordOrKeyNotFoundException(id));
    }

    @Override
    public List<Object> selectByName(String name) {
        return repository.findPasswordSecretkeysByName(name);
    }

    @Override
    public boolean isPresent(String name) {
        return !repository.findPasswordSecretkeysByName(name).isEmpty();
    }

    @Override
    public Object update(Object newPasswordSecretkeys, int id) {
        return repository.findById(id)
                .map(repository::save)
                .orElseGet(() -> {
                    final int userId = PASSWORD_ID_HOLDER.incrementAndGet();
                    return repository.save((PasswordSecretkeys) newPasswordSecretkeys);
                });
    }

    @Override
    public void delete(int id) {
        if (repository.findById(id).isPresent()){
            repository.deleteById(id);
        } else {
            throw new PasswordOrKeyNotFoundException(id);
        }
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
