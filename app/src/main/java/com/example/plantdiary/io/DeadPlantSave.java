package com.example.plantdiary.io;

import com.example.plantdiary.plantaction.CauseOfDeath;
import com.example.plantdiary.plantaction.LifeCycleStage;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DeadPlantSave implements Serializable {
    LocalDate timeOfDeath;
    boolean pre_existing = false;

    LocalDate ownedSince;

    CauseOfDeath cod;


    String profpicpath;

    LifeCycleStage lifeCycleStage;

    String name;
    String type;

    public DeadPlantSave(CauseOfDeath cod, LocalDate timeOfDeath, boolean pre_existing, LocalDate ownedSince, String profpicpath, LifeCycleStage lifeCycleStage, String name, String type) {
        this.timeOfDeath = timeOfDeath;
        this.pre_existing = pre_existing;
        this.ownedSince = ownedSince;
        this.profpicpath = profpicpath;
        this.lifeCycleStage = lifeCycleStage;
        this.name = name;
        this.type = type;
        this.cod = cod;
    }
}
