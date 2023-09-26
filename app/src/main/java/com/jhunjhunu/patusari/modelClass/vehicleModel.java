package com.jhunjhunu.patusari.modelClass;


import android.content.Context;

public class vehicleModel {


    String img, name, phone, owner;

    vehicleModel() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public vehicleModel(String img, String name, String phone, String owner) {
        this.img = img;
        this.name = name;
        this.phone = phone;
        this.owner = owner;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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
}
