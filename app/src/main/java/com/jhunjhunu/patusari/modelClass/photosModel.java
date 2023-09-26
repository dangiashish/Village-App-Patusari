package com.jhunjhunu.patusari.modelClass;

import android.widget.TextView;

public class photosModel {
    public String userName;
    public String imageURL;

    public photosModel(){

    }

    public photosModel(String userName, String imageURL) {
        this.userName = userName;
        this.imageURL = imageURL;
    }

    public photosModel(String toString) {
    }

//    public photosModel(String imageURL) {
//    }


    public String getUserName() {
        return userName;
    }
    public String getImageURL() {
        return imageURL;
    }
}

