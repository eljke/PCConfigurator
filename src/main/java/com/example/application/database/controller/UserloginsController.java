package com.example.application.database.controller;

import com.example.application.database.model.Userlogins;
import com.example.application.database.service.DatabaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserloginsController {

    private static DatabaseService<Userlogins> databaseService;

    public UserloginsController(@Qualifier("userloginsService") DatabaseService<Userlogins> databaseService) {
        UserloginsController.databaseService = databaseService;
    }

    public static void insert(Userlogins userlogins){
        databaseService.insert(userlogins);
    }

    public static boolean validate(String name) {
        return databaseService.isPresent(name);
    }

    public static Userlogins selectEncryptedPassword(String name) {
        return databaseService.selectByName(name).get(0);
    }

}
