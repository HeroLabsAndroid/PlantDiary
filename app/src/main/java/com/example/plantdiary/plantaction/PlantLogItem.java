package com.example.plantdiary.plantaction;

import com.example.plantdiary.io.PlantLogItemSave;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class PlantLogItem {
    public enum ItemType {
        ACTION, EVENT;
    }

    //------- VARS ------------------------------------//
    ItemType typ;
    LocalDateTime timestamp;
    String comment = "";
    //-------- GETTERS & SETTERS ----------------------------//


    public ItemType getTyp() {
        return typ;
    }

    public void setTyp(ItemType typ) {
        this.typ = typ;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    //------- CONSTRUCTOS ------------------------------------//

    public PlantLogItem(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public PlantLogItem(LocalDateTime timestamp, String comment) {
        this.timestamp = timestamp;
        this.comment = comment;
    }

    public PlantLogItem(PlantLogItemSave plis) {
        timestamp = plis.timestamp;
        comment = plis.comment;
    }



    //------------------ METHODS ------------------------------//
    public abstract  PlantLogItemSave toSave();

    public abstract JSONObject toJSONSave() throws JSONException;

    public static PlantLogItem fromSave(PlantLogItemSave save) {
        return null;
    }

}
