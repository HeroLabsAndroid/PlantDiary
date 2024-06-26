package com.example.plantdiary.io;

import com.example.plantdiary.plantaction.PlantLogItem;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantLogItemSave implements Serializable {
    public LocalDateTime timestamp;
    public String comment;

    public PlantLogItem.ItemType typ;

    public PlantLogItemSave(LocalDateTime timestamp, String comment) {
        this.timestamp = timestamp;
        this.comment = comment;
    }
}
