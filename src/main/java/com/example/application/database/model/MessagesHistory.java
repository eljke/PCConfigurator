package com.example.application.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "messageshistory")
public class MessagesHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String message;
    private String tab;

    public MessagesHistory() {
    }

    public MessagesHistory(String name, String message, String tab) {
        this.name = name;
        this.message = message;
        this.tab = tab;
    }

    public MessagesHistory(Integer id, String name, String message, String tab) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.tab = tab;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }
}
