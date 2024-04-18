package com.example.plantdiary.io;

import com.example.plantdiary.plant.AcquisitionType;
import com.example.plantdiary.plantaction.CauseOfDeath;
import com.example.plantdiary.plantaction.Comment;
import com.example.plantdiary.plantaction.LifeCycleStage;
import com.example.plantdiary.plantaction.PlantAction;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PlantSave implements Serializable {

    //----------------- VARS ------------------------//
    public float potsize;
    public LocalDate owned_since;
    public boolean pre_existing;

    public String name;
    public String planttype;

    public ArrayList<Comment> comments = new ArrayList<>();

    public String location;

    public String img_path;
    public boolean has_img;

    public AcquisitionType acqType;
    public ArrayList<PlantLogItemSave> log = new ArrayList<>();

    public ArrayList<String> logpicpaths;
    public ArrayList<LocalDateTime> logpictimes;

    public boolean has_fruit, has_flowers;

    public CauseOfDeath causeOfDeath;

    public LifeCycleStage lifeCycleStage;

    //--------------- CONSTRUCTOR --------------------------//


    public PlantSave(float potsize, LocalDate owned_since, boolean pre_existing, String name, String planttype, String location, AcquisitionType aqcType,
                     ArrayList<PlantLogItemSave> log, boolean has_img, String img_path, ArrayList<String> logpicpaths, ArrayList<LocalDateTime> logpictimes, ArrayList<Comment> comments,
                     boolean has_flowers, boolean has_fruit, CauseOfDeath causeOfDeath, LifeCycleStage lifeCycleStage) {
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
        this.comments = comments;
        this.logpictimes = logpictimes;
        this.pre_existing = pre_existing;
        this.has_flowers = has_flowers;
        this.has_fruit = has_fruit;
        this.causeOfDeath = causeOfDeath;
        this.lifeCycleStage = lifeCycleStage;
    }
}
