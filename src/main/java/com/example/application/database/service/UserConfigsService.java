package com.example.application.database.service;

import com.example.application.database.exception.ConfigNotFoundException;
import com.example.application.database.exception.PasswordOrKeyNotFoundException;
import com.example.application.database.model.UserConfigs;
import com.example.application.database.repository.UserConfigsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserConfigsService implements DatabaseService<UserConfigs>{

    private final UserConfigsRepository repository;

    public UserConfigsService(UserConfigsRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserConfigs insert(UserConfigs object) {
        return repository.save(object);
    }

    @Override
    public List<UserConfigs> selectAll() {
        return repository.findAll();
    }

    @Override
    public UserConfigs select(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ConfigNotFoundException(id));
    }

    @Override
    public List<UserConfigs> selectByName(String name) {
        return repository.findConfigByName(name);
    }

    @Override
    public boolean isPresent(String name) {
        return !repository.findConfigByName(name).isEmpty();
    }

    @Override
    public UserConfigs update(UserConfigs object, int id) {
        return repository.findById(id)
                .map(repository::save)
                .orElseGet(() -> repository.save(object));
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
