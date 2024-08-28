package com.example.plantdiary.plant;

import java.time.LocalDateTime;

public class PlantActData {
    public LocalDateTime last_watered, last_fertilized;
    public int commiecnt;

    public PlantActData(LocalDateTime last_watered, LocalDateTime last_fertilized, int commiecnt) {
        this.last_watered = last_watered;
        this.last_fertilized = last_fertilized;
        this.commiecnt = commiecnt;
    }
}
