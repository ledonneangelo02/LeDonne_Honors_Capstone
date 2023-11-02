package com.example.capstoneattmpt1shopandspeak.model;

public class Products {
    private String upcId;

    private String name;

    private String calories;

    private String servingSize;

    private String servingCount;

    // Getters and Setters
    public String getUpcId() {
        return upcId;
    }

    public void setUpcId(String upcId) {
        this.upcId = upcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServingCount() {
        return servingCount;
    }

    public void setServingCount(String servingCount) {
        this.servingCount = servingCount;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getServingSize() {
        return servingSize;
    }

    public void setServingSize(String servingSize) {
        this.servingSize = servingSize;
    }
}
