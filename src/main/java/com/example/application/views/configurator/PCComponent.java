package com.example.application.views.configurator;

public class PCComponent {

    private String image;
    private String name;
    private String needful;
    private String post;
    private int parseCounter = 0;

    public PCComponent () {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNeedful() {
        return needful;
    }

    public void setNeedful(String needful) {
        this.needful = needful;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getParseCounter() {
        return parseCounter;
    }

    public void setParseCounter(int parseCounter) {
        this.parseCounter = parseCounter;
    }
}
