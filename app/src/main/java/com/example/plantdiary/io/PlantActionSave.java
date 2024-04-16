package com.example.plantdiary.io;

import com.example.plantdiary.plantaction.PlantActionType;
import com.example.plantdiary.plantaction.PlantLogItem;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantActionSave extends PlantLogItemSave {
    public PlantActionType plantActionType;
    public PlantActionSave(PlantActionType paType, LocalDateTime timestamp, String comment) {
        super(timestamp, comment);
        plantActionType = paType;
        typ = PlantLogItem.ItemType.ACTION;
    }
}
