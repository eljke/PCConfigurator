package com.example.application.database.repository;

import com.example.application.database.model.Userlogins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseRepositoryUserlogins extends JpaRepository<Userlogins, Integer> {
    List<Userlogins> findAll();
    List<Object> findUserloginsByName(String name);
}
