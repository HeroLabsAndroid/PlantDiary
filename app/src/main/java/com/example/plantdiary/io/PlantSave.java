package com.example.plantdiary.io;

import com.example.plantdiary.plant.AcquisitionType;
import com.example.plantdiary.plantaction.PlantAction;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class PlantSave implements Serializable {

    //----------------- VARS ------------------------//
    public float potsize;
    public LocalDate owned_since;

    public String name;
    public String planttype;

    public String location;

    public AcquisitionType acqType;
    public ArrayList<PlantLogItemSave> log = new ArrayList<>();

    //--------------- CONSTRUCTOR --------------------------//


    public PlantSave(float potsize, LocalDate owned_since, String name, String planttype, String location, AcquisitionType aqcType, ArrayList<PlantLogItemSave> log) {
        this.potsize = potsize;
        this.owned_since = owned_since;
        this.name = name;
        this.planttype = planttype;
        this.location = location;
        this.log = log;
        this.acqType = aqcType;
    }
}
