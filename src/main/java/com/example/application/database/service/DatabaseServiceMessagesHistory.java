package com.example.application.database.service;

import com.example.application.database.exception.MessageNotFoundException;
import com.example.application.database.model.MessagesHistory;
import com.example.application.database.repository.DatabaseRepositoryMessagesHistory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DatabaseServiceMessagesHistory implements DatabaseService{
    private final DatabaseRepositoryMessagesHistory repository;

    private static final AtomicInteger MESSAGE_ID_HOLDER = new AtomicInteger();

    public DatabaseServiceMessagesHistory(DatabaseRepositoryMessagesHistory repository) {
        this.repository = repository;
    }

    @Override
    public Object insert(Object object) {
        System.out.println(object.getClass());
        final int messageId = MESSAGE_ID_HOLDER.incrementAndGet();

        return repository.save((MessagesHistory) object);
    }

    @Override
    public List<Object> selectAll() {
        return Collections.singletonList(repository.findAll());
    }

    @Override
    public Object select(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
    }

    @Override
    public List<Object> selectByName(String name) {
        return repository.findMessagesHistoryByName(name);
    }

    @Override
    public boolean isPresent(String name) {
        return !repository.findMessagesHistoryByName(name).isEmpty();
    }

    @Override
    public Object update(Object newMessagesHistory, int id) {
        return repository.findById(id)
                .map(repository::save)
                .orElseGet(() -> {
                    final int messageId = MESSAGE_ID_HOLDER.incrementAndGet();
                    return repository.save((MessagesHistory) newMessagesHistory);
                });
    }

    @Override
    public void delete(int id) {
        if (repository.findById(id).isPresent()){
            repository.deleteById(id);
        } else {
            throw new MessageNotFoundException(id);
        }
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
