package com.example.pocketgaragenotlogin.ui;

import android.graphics.Bitmap;

public class item_model {
    public String itemName;
    public String itemGeneration;
    public Float itemRating;
    private Bitmap itemImage;
    public item_model(String itemName, String itemGeneration, Float itemRating, Bitmap itemImage) {
        this.itemName = itemName;
        this.itemGeneration = itemGeneration;
        this.itemRating = itemRating;
        this.itemImage = itemImage;
    }



    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public void setItemGeneration(String itemName) {
        this.itemGeneration = itemGeneration;
    }
    public void setItemRating(Float itemRating) {
        this.itemRating = itemRating;
    }
    public void setItemImage(Bitmap itemImage) {
        this.itemImage = itemImage;
    }


}