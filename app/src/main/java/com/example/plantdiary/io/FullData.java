package com.example.plantdiary.io;

import com.example.plantdiary.plant.Plant;

import java.util.ArrayList;

public class FullData {
    public ArrayList<Plant> plants;
    public ArrayList<DeadPlant> deadplants;

    public FullData(ArrayList<Plant> plants, ArrayList<DeadPlant> deadplants) {
        this.plants = plants;
        this.deadplants = deadplants;
    }

    public boolean isEmpty() {
        return plants.isEmpty() && deadplants.isEmpty();
    }
}