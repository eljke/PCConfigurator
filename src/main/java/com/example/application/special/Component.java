package com.example.application.special;

public class Component {
    private Integer id;
    private String image;
    private String url;
    private String name;
    private String price;
    private String description;

    public Component(Integer id, String url, String name) {
        this.id = id;
        this.url = url;
        this.name = name;
    }

    public Component(String image, String url, String name, String price) {
        this.image = image;
        this.url = url;
        this.name = name;
        this.price = price;
    }

    public Component(Integer id, String price) {
        this.id = id;
        this.price = price;
    }

    public Component(String image, String url, String name, String price, String description) {
        this.image = image;
        this.url = url;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Component{" +
                "image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
