package com.example.application.database.repository;

import com.example.application.database.model.PasswordSecretkeys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseRepositoryPasswordSecretkeys extends JpaRepository<PasswordSecretkeys, Integer> {
    List<PasswordSecretkeys> findAll();
    List<Object> findPasswordSecretkeysByName(String name);
}
