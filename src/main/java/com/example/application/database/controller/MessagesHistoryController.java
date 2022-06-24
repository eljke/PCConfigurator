package com.example.application.database.controller;

import com.example.application.database.model.MessagesHistory;
import com.example.application.database.service.DatabaseService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagesHistoryController {

    private static DatabaseService<MessagesHistory> databaseService;

    public MessagesHistoryController(@Qualifier("messagesHistoryService") DatabaseService<MessagesHistory> databaseService) {
        MessagesHistoryController.databaseService = databaseService;
    }

    public static void insert(MessagesHistory messagesHistory){
        databaseService.insert(messagesHistory);
    }

    public static MessagesHistory select(int id){
        return databaseService.select(id);
    }

    public static MessagesHistory update(MessagesHistory userlogins, int id){
        return databaseService.update(userlogins, id);
    }

}
