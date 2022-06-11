package com.example.application.database.controller;

import com.example.application.database.model.MessagesHistory;
import com.example.application.database.service.DatabaseService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessagesHistoryController {

    private static DatabaseService databaseService;

    public MessagesHistoryController(@Qualifier("databaseServiceMessagesHistory") DatabaseService databaseService) {
        MessagesHistoryController.databaseService = databaseService;
    }

    public static MessagesHistory insert(MessagesHistory messagesHistory){
        return (MessagesHistory) databaseService.insert(messagesHistory);
    }

    public static MessagesHistory select(int id){
        return (MessagesHistory) databaseService.select(id);
    }

    public static MessagesHistory update(MessagesHistory userlogins, int id){
        return (MessagesHistory) databaseService.update(userlogins, id);
    }

}
