package com.example.plantdiary.io;

import com.example.plantdiary.plantaction.PlantEventType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantEventSave extends PlantLogItemSave{
    public PlantEventType pet;
    public PlantEventSave(PlantEventType pet, LocalDateTime timestamp, String comment) {
        super(timestamp, comment);
        this.pet = pet;
    }
}
