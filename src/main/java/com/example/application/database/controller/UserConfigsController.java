package com.example.application.database.controller;

import com.example.application.database.model.UserConfigs;
import com.example.application.database.service.DatabaseService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserConfigsController {

    private static DatabaseService<UserConfigs> databaseService;

    public UserConfigsController(@Qualifier("userConfigsService") DatabaseService<UserConfigs> databaseService) {
        UserConfigsController.databaseService = databaseService;
    }

    public static void insert(UserConfigs config){
        databaseService.insert(config);
    }
}
