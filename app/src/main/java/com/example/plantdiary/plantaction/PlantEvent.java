package com.example.plantdiary.plantaction;

import com.example.plantdiary.io.PlantEventSave;
import com.example.plantdiary.io.PlantLogItemSave;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantEvent extends PlantLogItem {


    //-------- VARS ----------------------------//
    PlantEventType pet;

    //------- GETTER & SETTER ---------------------------//

    public PlantEventType getPet() {
        return pet;
    }

    public void setPet(PlantEventType pet) {
        this.pet = pet;
    }

    //---------- CONSTRUCTORS ---------------- //
    public PlantEvent(LocalDateTime timestamp) {
        super(timestamp);
        typ = ItemType.EVENT;
    }

    public PlantEvent(LocalDateTime timestamp, String comment) {
        super(timestamp, comment);
        typ = ItemType.EVENT;
    }

    public PlantEvent(PlantEventSave pes) {
        super(pes);
        typ = ItemType.EVENT;
        pet = pes.pet;
    }

    //------------- OVERRIDES -----------------------//

    @Override
    public PlantEventSave toSave() {
        return new PlantEventSave(pet, timestamp, comment);
    }


    public static PlantEvent fromSave(PlantLogItemSave save) {
        return new PlantEvent((PlantEventSave) save);
    }


}
