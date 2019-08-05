package com.tsafack.alc40phase2challenge.entities;

public class User {
    private String email;
    private String name;
    private String phone;
    private String imageUrl;

    public User() {
    }

    public User(String email, String name, String phone ,String imageUrl) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.imageUrl  = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
