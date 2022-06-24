package com.example.application.database.controller;

import com.example.application.database.model.PasswordSecretkeys;
import com.example.application.database.service.DatabaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordSecretkeysController {
    private static DatabaseService<PasswordSecretkeys> databaseService;

    public PasswordSecretkeysController(@Qualifier("passwordSecretkeysService") DatabaseService<PasswordSecretkeys> databaseService) {
        PasswordSecretkeysController.databaseService = databaseService;
    }

    public static void insert(PasswordSecretkeys passwordSecretkeys){
        databaseService.insert(passwordSecretkeys);
    }

    public static PasswordSecretkeys select(int id){
        return databaseService.select(id);
    }

    public static String selectEncodedSecretKeyByName(String name){
        PasswordSecretkeys secretkey = databaseService.selectByName(name).get(0);
        return secretkey.getEncodedsecretkey();
    }

    public static PasswordSecretkeys update(PasswordSecretkeys userlogins, int id){
        return databaseService.update(userlogins, id);
    }
}
