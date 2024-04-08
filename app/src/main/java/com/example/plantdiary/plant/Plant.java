package com.example.plantdiary.plant;

import android.media.Image;

import com.example.plantdiary.plantaction.PlantAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Plant {
    private float potsize;
    private LocalDate owned_since;

    private AcquisitionType acqTyp;

    private String name;
    private String planttype;

    private String location;

    private ArrayList<PlantAction> log = new ArrayList<>();

    private Image profilepic;

    //--------------------------GETTERS AND SETTERS------------------------------------//
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

    public ArrayList<PlantAction> getLog() {
        return log;
    }

    public void setLog(ArrayList<PlantAction> log) {
        this.log = log;
    }

    public Image getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(Image profilepic) {
        this.profilepic = profilepic;
    }


    //----------------------------------------------------------------------------//

    public Plant(String name, AcquisitionType act) {
        this.name = name;
        acqTyp = act;
    }



}
