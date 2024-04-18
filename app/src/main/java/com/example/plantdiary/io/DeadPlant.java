package com.example.plantdiary.io;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.plantdiary.plant.Plant;
import com.example.plantdiary.plantaction.CauseOfDeath;
import com.example.plantdiary.plantaction.LifeCycleStage;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class DeadPlant implements Comparable<DeadPlant> {
    LocalDate timeOfDeath;
    boolean pre_existing = false;

    LocalDate ownedSince;

    long lifetime() {
        return ownedSince.until(timeOfDeath, ChronoUnit.DAYS);
    }

    String profpicpath;

    LifeCycleStage lifeCycleStage;
    CauseOfDeath causeOfDeath;

    String name;
    String type;

    Bitmap img;

    //------------------ GETTERS & SETTERS --------------------------------------------//

    public LocalDate getTimeOfDeath() {
        return timeOfDeath;
    }


    public boolean isPre_existing() {
        return pre_existing;
    }


    public LocalDate getOwnedSince() {
        return ownedSince;
    }

    public String getProfpicpath() {
        return profpicpath;
    }


    public LifeCycleStage getLifeCycleStage() {
        return lifeCycleStage;
    }


    public CauseOfDeath getCauseOfDeath() {
        return causeOfDeath;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }


    public Bitmap getImg() {
        return img;
    }



    //------------------ CONSTRUCTORS -----------------------------------------------//

    public DeadPlant(CauseOfDeath cod, LocalDate timeOfDeath, boolean pre_existing, LocalDate ownedSince, String profpicpath, LifeCycleStage lifeCycleStage, String name, String type) {
        this.timeOfDeath = timeOfDeath;
        this.pre_existing = pre_existing;
        this.ownedSince = ownedSince;
        this.profpicpath = profpicpath;
        this.lifeCycleStage = lifeCycleStage;
        this.name = name;
        this.type = type;
        this.causeOfDeath = cod;

        img = BitmapFactory.decodeFile(profpicpath);
    }

    public DeadPlant(Plant pl, CauseOfDeath cod) {
        this.timeOfDeath = LocalDate.now();
        this.pre_existing = pl.isPre_existing();
        this.ownedSince = pl.getOwned_since();
        this.profpicpath = pl.getPicture_path();
        this.lifeCycleStage = pl.getLifestage();
        this.name = pl.getName();
        this.type = pl.getPlanttype();
        this.causeOfDeath = cod;

        img = BitmapFactory.decodeFile(profpicpath);
    }

    public static DeadPlant fromSave(DeadPlantSave dps) {
        return new DeadPlant(dps.cod, dps.timeOfDeath, dps.pre_existing, dps.ownedSince, dps.profpicpath, dps.lifeCycleStage, dps.name, dps.type);
    }

    public DeadPlantSave toSave() {
        return new DeadPlantSave(causeOfDeath, timeOfDeath, pre_existing, ownedSince, profpicpath, lifeCycleStage, name, type);
    }

    @Override
    public int compareTo(DeadPlant o) {
        return timeOfDeath.compareTo(o.timeOfDeath);
    }
}
