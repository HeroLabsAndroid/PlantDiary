package com.example.plantdiary.plant;

import android.graphics.Bitmap;

public class PlantGridData {
    public String name;
    public String type;
    public Bitmap photo;

    public int idx;

    public PlantGridData(String name, String type, Bitmap photo, int idx) {
        this.name = name;
        this.type = type;
        this.photo = photo;
        this.idx = idx;
    }

    public PlantGridData(String name, String type, Bitmap photo) {
        this.name = name;
        this.type = type;
        this.photo = photo;
        idx = 0;
    }

    public PlantGridData(Plant p) {
        this.name = p.getName();
        this.type = p.getPlanttype();
        this.photo = p.getProfilepic();
    }
}
