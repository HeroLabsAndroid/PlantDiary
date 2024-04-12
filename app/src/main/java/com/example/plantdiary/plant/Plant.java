package com.example.plantdiary.plant;

import android.graphics.Bitmap;
import android.media.Image;

import com.example.plantdiary.io.PlantActionSave;
import com.example.plantdiary.io.PlantEventSave;
import com.example.plantdiary.io.PlantLogItemSave;
import com.example.plantdiary.io.PlantSave;
import com.example.plantdiary.plantaction.PlantAction;
import com.example.plantdiary.plantaction.PlantEvent;
import com.example.plantdiary.plantaction.PlantLogItem;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Plant {

    //-------------- VARS -------------------------- //
    private float potsize;
    private LocalDate owned_since;

    private AcquisitionType acqTyp;

    private String name;
    private String planttype;

    private String location;

    private ArrayList<PlantLogItem> log = new ArrayList<>();

    private Bitmap profilepic;

    private String picture_path;

    private boolean has_picture = false;

    //--------------------------GETTERS AND SETTERS------------------------------------//


    public String getPicture_path() {
        return picture_path;
    }

    public void setPicture_path(String picture_path) {
        this.picture_path = picture_path;
    }

    public float getPotsize() {
        return potsize;
    }

    public void setPotsize(float potsize) {
        this.potsize = potsize;
    }

    public LocalDate getOwned_since() {
        return owned_since;
    }

    public void setOwned_since(LocalDate owned_since) {
        this.owned_since = owned_since;
    }

    public AcquisitionType getAcqTyp() {
        return acqTyp;
    }

    public void setAcqTyp(AcquisitionType acqTyp) {
        this.acqTyp = acqTyp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanttype() {
        return planttype;
    }

    public void setPlanttype(String planttype) {
        this.planttype = planttype;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<PlantLogItem> getLog() {
        return log;
    }

    public void setLog(ArrayList<PlantLogItem> log) {
        this.log = log;
    }

    public Bitmap getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(Bitmap profilepic) {
        this.profilepic = profilepic;
        has_picture = true;
    }

    public boolean hasPicture() {
        return has_picture;
    }


    //-------------------------- CONSTRUCTORS--------------------------------------//

    public Plant(String name, AcquisitionType act) {
        this.name = name;
        acqTyp = act;
    }

    public Plant(String name, String planttype, AcquisitionType act) {
        this.name = name;
        acqTyp = act;
        this.planttype = planttype;
    }

    public Plant(PlantSave ps) {
        this.planttype = ps.planttype;
        this.name = ps.name;
        this.location = ps.location;
        this.acqTyp = ps.acqType;
        this.log = new ArrayList<>();
        for(PlantLogItemSave plis: ps.log) {
            log.add(plis.typ == PlantLogItem.ItemType.ACTION ? PlantAction.fromSave(plis) : PlantEvent.fromSave(plis));
        }
        this.owned_since = ps.owned_since;

    }

    //------------------------ CUSTOM FUNCS -------------------------------------//

    public PlantSave toSave() {
        ArrayList<PlantLogItemSave> logsave = new ArrayList<>();
        for(PlantLogItem pli: log) {
            logsave.add(pli.toSave());
        }
        return new PlantSave(potsize, owned_since, name, planttype, location, acqTyp, logsave);
    }


}
