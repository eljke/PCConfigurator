package com.example.application.database.repository;

import com.example.application.database.model.PasswordSecretkeys;
import com.example.application.database.model.UserConfigs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserConfigsRepository extends JpaRepository <UserConfigs, Integer> {
    List<UserConfigs> findAll();
    List<UserConfigs> findConfigByName(String name);
}
