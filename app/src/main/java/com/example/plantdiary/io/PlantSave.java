package com.example.plantdiary.io;

import com.example.plantdiary.plant.AcquisitionType;
import com.example.plantdiary.plantaction.PlantAction;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PlantSave implements Serializable {

    //----------------- VARS ------------------------//
    public float potsize;
    public LocalDate owned_since;

    public String name;
    public String planttype;

    public String comment;

    public String location;

    public String img_path;
    public boolean has_img;

    public AcquisitionType acqType;
    public ArrayList<PlantLogItemSave> log = new ArrayList<>();

    public ArrayList<String> logpicpaths;
    public ArrayList<LocalDateTime> logpictimes;

    //--------------- CONSTRUCTOR --------------------------//


    public PlantSave(float potsize, LocalDate owned_since, String name, String planttype, String location, AcquisitionType aqcType,
                     ArrayList<PlantLogItemSave> log, boolean has_img, String img_path, ArrayList<String> logpicpaths, ArrayList<LocalDateTime> logpictimes, String comment) {
        this.potsize = potsize;
        this.owned_since = owned_since;
        this.name = name;
        this.planttype = planttype;
        this.location = location;
        this.log = log;
        this.acqType = aqcType;
        this.has_img = has_img;
        this.img_path = img_path;
        this.logpicpaths = logpicpaths;
        this.comment = comment;
        this.logpictimes = logpictimes;
    }
}
