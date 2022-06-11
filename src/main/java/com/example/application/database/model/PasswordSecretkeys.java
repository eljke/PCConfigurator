package com.example.application.database.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "passwordsecretkeys")
public class PasswordSecretkeys {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String password;
    private String encodedsecretkey;
    private String secretkey;
    private Integer random;

    public PasswordSecretkeys() {
    }

    public PasswordSecretkeys(Integer id, String name, String password, String encodedsecretkey, String secretkey, Integer random) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.encodedsecretkey = encodedsecretkey;
        this.secretkey = secretkey;
        this.random = random;
    }

    public PasswordSecretkeys(String name, String password, String encodedsecretkey, String secretkey, Integer random) {
        this.name = name;
        this.password = password;
        this.encodedsecretkey = encodedsecretkey;
        this.secretkey = secretkey;
        this.random = random;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEncodedsecretkey() {
        return encodedsecretkey;
    }

    public void setEncodedsecretkey(String encodedsecretkey) {
        this.encodedsecretkey = encodedsecretkey;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }

    public Integer getRandom() {
        return random;
    }

    public void setRandom(Integer random) {
        this.random = random;
    }
}
