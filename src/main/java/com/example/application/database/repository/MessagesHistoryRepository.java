package com.example.application.database.repository;

import com.example.application.database.model.MessagesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesHistoryRepository extends JpaRepository<MessagesHistory, Integer> {
    List<MessagesHistory> findAll();

    List<MessagesHistory> findMessagesHistoryByName(String name);
}
