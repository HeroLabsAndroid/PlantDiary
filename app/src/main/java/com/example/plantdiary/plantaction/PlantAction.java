package com.example.plantdiary.plantaction;

import com.example.plantdiary.io.PlantActionSave;
import com.example.plantdiary.io.PlantLogItemSave;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantAction extends PlantLogItem {
    //------- VARS ---------------------------------//
    PlantActionType actTyp;

    //-------- GETTERS & SETTERS ---------------------//


    public PlantActionType getActTyp() {
        return actTyp;
    }

    public void setActTyp(PlantActionType actTyp) {
        this.actTyp = actTyp;
    }

    //-------- CONSTRUCTORS ------------------------//
    public PlantAction(LocalDateTime timestamp) {
        super(timestamp);
        typ = ItemType.ACTION;
    }

    public PlantAction(LocalDateTime timestamp, String comment) {
        super(timestamp, comment);
        typ = ItemType.ACTION;
    }

    public PlantAction(PlantActionSave pas) {
        super(pas.timestamp, pas.comment);
        actTyp = pas.plantActionType;
        typ = ItemType.ACTION;
    }

    //-------------- OVERRIDES -----------------------//

    @Override
    public PlantActionSave toSave() {
        return new PlantActionSave(actTyp, timestamp, comment);
    }


    public static PlantAction fromSave(PlantLogItemSave save) {
        PlantActionSave pas = (PlantActionSave) save;
        PlantAction paTmp = new PlantAction(pas.timestamp, pas.comment);
        paTmp.actTyp = pas.plantActionType;

        return paTmp;
    }

}
