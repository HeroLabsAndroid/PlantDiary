package com.example.plantdiary.plantaction;

import com.example.plantdiary.LDTsave;
import com.example.plantdiary.io.PlantActionSave;
import com.example.plantdiary.io.PlantLogItemSave;

import org.json.JSONException;
import org.json.JSONObject;

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

    public PlantAction(JSONObject jsave) throws JSONException {
        super(new LDTsave(jsave.getJSONObject("timestamp")).toLDT(), jsave.getString("comment"));
        actTyp = PlantActionType.fromOrdinal(jsave.getInt("type"));
        typ=ItemType.ACTION;

    }

    //-------------- OVERRIDES -----------------------//

    @Override
    public PlantActionSave toSave() {
        return new PlantActionSave(actTyp, timestamp, comment);
    }

    @Override
    public JSONObject toJSONSave() throws JSONException {
        JSONObject jpasave = new JSONObject();
        jpasave.put("comment", comment);
        jpasave.put("type", actTyp.ordinal());
        jpasave.put("timestamp", new LDTsave(timestamp).toJSONSave());
        return jpasave;
    }


    public static PlantAction fromSave(PlantLogItemSave save) {
        PlantActionSave pas = (PlantActionSave) save;
        PlantAction paTmp = new PlantAction(pas.timestamp, pas.comment);
        paTmp.actTyp = pas.plantActionType;

        return paTmp;
    }

}
