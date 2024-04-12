package com.example.plantdiary.io;

import com.example.plantdiary.plantaction.PlantLogItem;

import java.io.Serializable;
import java.time.LocalDate;

public class PlantLogItemSave implements Serializable {
    public LocalDate timestamp;
    public String comment;

    public PlantLogItem.ItemType typ;

    public PlantLogItemSave(LocalDate timestamp, String comment) {
        this.timestamp = timestamp;
        this.comment = comment;
    }
}
