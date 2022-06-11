package com.example.application.database.controller;

import com.example.application.database.model.Userlogins;
import com.example.application.database.service.DatabaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.List;

@RestController
public class UserloginsController {

    private static DatabaseService databaseService;

    public UserloginsController(@Qualifier("databaseServiceUserlogins") DatabaseService databaseService) {
        UserloginsController.databaseService = databaseService;
    }

    public static Userlogins insert(Userlogins userlogins){
        return (Userlogins) databaseService.insert(userlogins);
    }

    public static boolean validate(String name) {
        return databaseService.isPresent(name);
    }

    public static Userlogins selectEncryptedPassword(String name) {
        Object object = databaseService.selectByName(name).get(0);
        return (Userlogins) object;
    }

}
