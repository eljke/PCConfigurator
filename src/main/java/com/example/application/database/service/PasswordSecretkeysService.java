package com.example.application.database.service;

import com.example.application.database.exception.PasswordOrKeyNotFoundException;
import com.example.application.database.model.PasswordSecretkeys;
import com.example.application.database.repository.PasswordSecretkeysRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PasswordSecretkeysService implements DatabaseService <PasswordSecretkeys>{
    private final PasswordSecretkeysRepository repository;

    private static final AtomicInteger PASSWORD_ID_HOLDER = new AtomicInteger();

    public PasswordSecretkeysService(PasswordSecretkeysRepository repository) {
        this.repository = repository;
    }

    @Override
    public PasswordSecretkeys insert(PasswordSecretkeys object) {

        final int passwordId = PASSWORD_ID_HOLDER.incrementAndGet();

        return repository.save(object);
    }

    @Override
    public List<PasswordSecretkeys> selectAll() {
        return repository.findAll();
    }

    @Override
    public PasswordSecretkeys select(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new PasswordOrKeyNotFoundException(id));
    }

    @Override
    public List<PasswordSecretkeys> selectByName(String name) {
        return repository.findPasswordSecretkeysByName(name);
    }

    @Override
    public boolean isPresent(String name) {
        return !repository.findPasswordSecretkeysByName(name).isEmpty();
    }

    @Override
    public PasswordSecretkeys update(PasswordSecretkeys newPasswordSecretkeys, int id) {
        return repository.findById(id)
                .map(repository::save)
                .orElseGet(() -> {
                    final int userId = PASSWORD_ID_HOLDER.incrementAndGet();
                    return repository.save(newPasswordSecretkeys);
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
