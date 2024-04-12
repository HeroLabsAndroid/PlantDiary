package com.example.plantdiary.plantaction;

import com.example.plantdiary.io.PlantLogItemSave;

import java.time.LocalDate;

public abstract class PlantLogItem {
    public enum ItemType {
        ACTION, EVENT;
    }

    //------- VARS ------------------------------------//
    ItemType typ;
    LocalDate timestamp;
    String comment = "";
    //-------- GETTERS & SETTERS ----------------------------//

    public LocalDate getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    //------- CONSTRUCTOS ------------------------------------//

    public PlantLogItem(LocalDate timestamp) {
        this.timestamp = timestamp;
    }

    public PlantLogItem(LocalDate timestamp, String comment) {
        this.timestamp = timestamp;
        this.comment = comment;
    }

    public PlantLogItem(PlantLogItemSave plis) {
        timestamp = plis.timestamp;
        comment = plis.comment;
    }

    //------------------ METHODS ------------------------------//
    public abstract  PlantLogItemSave toSave();

    public static PlantLogItem fromSave(PlantLogItemSave save) {
        return null;
    }

}
