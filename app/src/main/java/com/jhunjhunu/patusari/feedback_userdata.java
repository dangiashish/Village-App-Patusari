package com.jhunjhunu.patusari;

import android.widget.ImageView;

public class feedback_userdata {

    String name, phone, feedback, email;

    public feedback_userdata(){
    }
    public feedback_userdata(String name, String phone, String feedback, String email) {
        this.name = name;
        this.phone = phone;
        this.feedback = feedback;
        this.email = email;
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

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
