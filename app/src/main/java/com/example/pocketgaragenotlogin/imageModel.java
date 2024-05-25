package com.example.pocketgaragenotlogin;

public class imageModel {
    private String imageUrl;

    public imageModel() {
        // Default constructor required for calls to DataSnapshot.getValue(ImageModel.class)
    }

    public imageModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

