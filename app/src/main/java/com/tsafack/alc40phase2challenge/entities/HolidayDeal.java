package com.tsafack.alc40phase2challenge.entities;

public class HolidayDeal {
    private String name;
    private double price;
    private String description;
    private String urlImage;

    public HolidayDeal() {
    }

    public HolidayDeal(String name, double price, String description, String urlImage) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.urlImage = urlImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
