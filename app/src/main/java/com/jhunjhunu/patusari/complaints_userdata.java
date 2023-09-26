package com.jhunjhunu.patusari;

import java.util.Map;

public class complaints_userdata {
    String name;
    String phone;
    String email;
    String feedback;
    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public complaints_userdata() {
    }

    public complaints_userdata(String name, String phone, String email, String feedback, String time) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.feedback = feedback;
        this.time = time;
    }
}
