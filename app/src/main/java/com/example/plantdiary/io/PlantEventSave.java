package com.example.plantdiary.io;

import java.time.LocalDate;

public class PlantEventSave extends PlantLogItemSave{
    public PlantEventSave(LocalDate timestamp, String comment) {
        super(timestamp, comment);
    }
}
