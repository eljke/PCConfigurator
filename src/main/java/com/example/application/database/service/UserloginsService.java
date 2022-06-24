package com.example.application.database.service;

import com.example.application.database.exception.UserNotFoundException;
import com.example.application.database.model.Userlogins;
import com.example.application.database.repository.UserloginsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserloginsService implements DatabaseService <Userlogins>{
    private final UserloginsRepository repository;

    private static final AtomicInteger USER_ID_HOLDER = new AtomicInteger();

    public UserloginsService(UserloginsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Userlogins insert(Userlogins object) {

        final int userId = USER_ID_HOLDER.incrementAndGet();

        return repository.save(object);
    }

    @Override
    public List<Userlogins> selectAll() {
        return repository.findAll();
    }

    @Override
    public Userlogins select(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public List<Userlogins> selectByName(String name) {
        return repository.findUserloginsByName(name);
    }

    @Override
    public boolean isPresent(String name) {
        System.out.println(name);
        return !repository.findUserloginsByName(name).isEmpty();
    }

    @Override
    public Userlogins update(Userlogins newUserlogins, int id) {
        return repository.findById(id)
                .map(repository::save)
                .orElseGet(() -> {
                    final int userId = USER_ID_HOLDER.incrementAndGet();
                    return repository.save(newUserlogins);
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
