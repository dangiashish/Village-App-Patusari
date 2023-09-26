package com.jhunjhunu.patusari;

public class vehicle_listdata {

    int phone;
    String  name;

    public vehicle_listdata() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

//    public String getImg() {
//        return img;
//    }
//
//    public void setImg(String img) {
//        this.img = img;
//    }

    public vehicle_listdata(String img, String name, int phone) {
//        this.img = img;
        this.name = name;
        this.phone = phone;
    }
}
