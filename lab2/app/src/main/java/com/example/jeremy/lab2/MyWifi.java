package com.example.jeremy.lab2;

public class MyWifi {
    private String name;
    private Integer level;

    public MyWifi() {
    }

    public MyWifi(String name, Integer level) {
        this.name = name;
        this.level = level;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer genre) {
        this.level = genre;
    }
}
