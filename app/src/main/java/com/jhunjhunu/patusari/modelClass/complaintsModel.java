package com.jhunjhunu.patusari.modelClass;


import java.util.Map;

public class complaintsModel {

    String name;
    String phone;
    String feedback;
    String email;
    String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }





    public complaintsModel(String name, String phone, String feedback, String email, String time) {
        this.name = name;
        this.phone = phone;
        this.feedback = feedback;
        this.email = email;
        this.time = time;
    }

    public complaintsModel(){
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }


}
