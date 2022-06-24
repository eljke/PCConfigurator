package com.example.application.database.service;

import com.example.application.database.exception.MessageNotFoundException;
import com.example.application.database.model.MessagesHistory;
import com.example.application.database.repository.MessagesHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MessagesHistoryService implements DatabaseService <MessagesHistory>{
    private final MessagesHistoryRepository repository;

    private static final AtomicInteger MESSAGE_ID_HOLDER = new AtomicInteger();

    public MessagesHistoryService(MessagesHistoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public MessagesHistory insert(MessagesHistory object) {
        System.out.println(object.getClass());
        final int messageId = MESSAGE_ID_HOLDER.incrementAndGet();

        return repository.save(object);
    }

    @Override
    public List<MessagesHistory> selectAll() {
        return repository.findAll();
    }

    @Override
    public MessagesHistory select(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));
    }

    @Override
    public List<MessagesHistory> selectByName(String name) {
        return repository.findMessagesHistoryByName(name);
    }

    @Override
    public boolean isPresent(String name) {
        return !repository.findMessagesHistoryByName(name).isEmpty();
    }

    @Override
    public MessagesHistory update(MessagesHistory newMessagesHistory, int id) {
        return repository.findById(id)
                .map(repository::save)
                .orElseGet(() -> {
                    final int messageId = MESSAGE_ID_HOLDER.incrementAndGet();
                    return repository.save(newMessagesHistory);
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
