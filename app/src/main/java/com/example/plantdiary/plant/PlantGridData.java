package com.example.plantdiary.plant;

import android.graphics.Bitmap;

import java.time.LocalDateTime;

public class PlantGridData {
    public String name;
    public String type;
    public String latname;
    public Bitmap photo;

    public PlantActData paDat;


    public PlantGridData(Plant p) {
        this.name = p.getName();
        this.type = p.getPlanttype();
        this.photo = p.getProfilepic();
        this.paDat = new PlantActData(p.last_watered(), p.last_fertilized(), p.getComments().size());
        this.latname = p.getName_latin();
    }
}
