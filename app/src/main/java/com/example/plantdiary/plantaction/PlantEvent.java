package com.example.plantdiary.plantaction;

import com.example.plantdiary.LDTsave;
import com.example.plantdiary.io.PlantEventSave;
import com.example.plantdiary.io.PlantLogItemSave;

import org.json.JSONException;
import org.json.JSONObject;

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

    public PlantEvent(JSONObject jsave) throws JSONException {
        super(new LDTsave(jsave.getJSONObject("timestamp")).toLDT(), jsave.getString("comment"));
        pet = PlantEventType.fromOrdinal(jsave.getInt("type"));
        typ = ItemType.EVENT;
    }

    //------------- OVERRIDES -----------------------//

    @Override
    public PlantEventSave toSave() {
        return new PlantEventSave(pet, timestamp, comment);
    }

    @Override
    public JSONObject toJSONSave() throws JSONException {
        JSONObject jpesave = new JSONObject();
        jpesave.put("timestamp", new LDTsave(timestamp).toJSONSave());
        jpesave.put("comment", comment);
        jpesave.put("type", pet.ordinal());
        return jpesave;
    }


    public static PlantEvent fromSave(PlantLogItemSave save) {
        return new PlantEvent((PlantEventSave) save);
    }


}
