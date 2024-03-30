package com.example.pocketgaragenotlogin;

public class CarModel {
    private String imagePath;
    private String modelName;
    private String modelGen;
    private String rate;

    public CarModel(String imagePath, String modelName, String modelGen, String rate) {
        this.imagePath = imagePath;
        this.modelName = modelName;
        this.modelGen = modelGen;
        this.rate = rate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getModelName() {
        return modelName;
    }

    public String getModelGen() {
        return modelGen;
    }

    public String getRate() {
        return rate;
    }
}
