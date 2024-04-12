package com.example.plantdiary.plantaction;

import com.example.plantdiary.io.PlantEventSave;
import com.example.plantdiary.io.PlantLogItemSave;

import java.time.LocalDate;

public class PlantEvent extends PlantLogItem {

    //---------- CONSTRUCTORS ---------------- //
    public PlantEvent(LocalDate timestamp) {
        super(timestamp);
        typ = ItemType.EVENT;
    }

    public PlantEvent(LocalDate timestamp, String comment) {
        super(timestamp, comment);
        typ = ItemType.EVENT;
    }

    public PlantEvent(PlantEventSave pes) {
        super(pes);
        typ = ItemType.EVENT;
    }

    //------------- OVERRIDES -----------------------//

    @Override
    public PlantEventSave toSave() {
        return new PlantEventSave(timestamp, comment);
    }


    public static PlantEvent fromSave(PlantLogItemSave save) {
        return new PlantEvent(save.timestamp, save.comment);
    }


}
