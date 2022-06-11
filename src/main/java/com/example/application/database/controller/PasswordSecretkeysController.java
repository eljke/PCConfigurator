package com.example.application.database.controller;

import com.example.application.database.model.PasswordSecretkeys;
import com.example.application.database.service.DatabaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordSecretkeysController {
    private static DatabaseService databaseService;

    public PasswordSecretkeysController(@Qualifier("databaseServicePasswordSecretkeys") DatabaseService databaseService) {
        PasswordSecretkeysController.databaseService = databaseService;
    }

    public static PasswordSecretkeys insert(PasswordSecretkeys passwordSecretkeys){
        return (PasswordSecretkeys) databaseService.insert(passwordSecretkeys);
    }

    public static PasswordSecretkeys select(int id){
        return (PasswordSecretkeys) databaseService.select(id);
    }

    public static String selectEncodedSecretKeyByName(String name){
        PasswordSecretkeys secretkey = (PasswordSecretkeys) databaseService.selectByName(name).get(0);
        return secretkey.getEncodedsecretkey();
    }

    public static PasswordSecretkeys update(PasswordSecretkeys userlogins, int id){
        return (PasswordSecretkeys) databaseService.update(userlogins, id);
    }
}
