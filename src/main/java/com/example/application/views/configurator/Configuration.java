package com.example.application.views.configurator;

public class Configuration {
    private int count;
    private String name;

    public Configuration() {
    }

    public Configuration(int count) {
        this.count = count;
    }

    public Configuration(int count, String name) {
        this.count = count;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incCount() {
        this.count = count + 1;
    }
}
