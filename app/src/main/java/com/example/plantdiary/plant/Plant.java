package com.example.plantdiary.plant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.plantdiary.io.PlantLogItemSave;
import com.example.plantdiary.io.PlantSave;
import com.example.plantdiary.plantaction.CauseOfDeath;
import com.example.plantdiary.plantaction.Comment;
import com.example.plantdiary.plantaction.LifeCycleStage;
import com.example.plantdiary.plantaction.PlantAction;
import com.example.plantdiary.plantaction.PlantActionType;
import com.example.plantdiary.plantaction.PlantEvent;
import com.example.plantdiary.plantaction.PlantLogItem;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Plant implements Comparable<Plant>{

    //-------------- VARS -------------------------- //
    private float potsize;

    private boolean potsize_na = false;
    private LocalDate owned_since;
    private boolean pre_existing = true;

    private AcquisitionType acqTyp;

    private String name;

    public ArrayList<Comment> comments = new ArrayList<>();
    private String planttype;

    private String location;

    private ArrayList<PlantLogItem> log = new ArrayList<>();

    private CauseOfDeath deathcause = CauseOfDeath.NONE;

    private Bitmap profilepic;

    private String picture_path;

    private ArrayList<String> logPicPaths = new ArrayList<>();
    private ArrayList<LocalDateTime> logPicTS = new ArrayList<>();
    private boolean has_picture = false;

    private boolean has_fruits = false;

    private boolean has_flowers = false;

    private LifeCycleStage lifestage = LifeCycleStage.GROWN;


    //--------------------------GETTERS AND SETTERS------------------------------------//



    public ArrayList<LocalDateTime> getLogPicTS() {
        return logPicTS;
    }

    public void setLogPicTS(ArrayList<LocalDateTime> logPicTS) {
        this.logPicTS = logPicTS;
    }

    public ArrayList<String> getLogPicPaths() {
        return logPicPaths;
    }

    public void setLogPicPaths(ArrayList<String> logPicPaths) {
        this.logPicPaths = logPicPaths;
    }

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

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comment) {
        this.comments = comments;
    }

    public boolean isPre_existing() {
        return pre_existing;
    }

    public void setPre_existing(boolean pre_existing) {
        this.pre_existing = pre_existing;
    }

    public CauseOfDeath getDeathcause() {
        return deathcause;
    }

    public void setDeathcause(CauseOfDeath deathcause) {
        this.deathcause = deathcause;
    }

    public boolean isHas_fruits() {
        return has_fruits;
    }

    public void setHas_fruits(boolean has_fruits) {
        this.has_fruits = has_fruits;
    }

    public boolean isHas_flowers() {
        return has_flowers;
    }

    public void setHas_flowers(boolean has_flowers) {
        this.has_flowers = has_flowers;
    }

    public LifeCycleStage getLifestage() {
        return lifestage;
    }

    public void setLifestage(LifeCycleStage lifestage) {
        this.lifestage = lifestage;
    }

    public boolean isPotsize_na() {
        return potsize_na;
    }

    public void setPotsize_na(boolean potsize_na) {
        this.potsize_na = potsize_na;
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
        this.potsize = ps.potsize;
        this.log = new ArrayList<>();
        for(PlantLogItemSave plis: ps.log) {
            log.add(plis.typ == PlantLogItem.ItemType.ACTION ? PlantAction.fromSave(plis) : PlantEvent.fromSave(plis));
        }
        this.owned_since = ps.owned_since;
        this.has_picture = ps.has_img;
        if(has_picture) {
            this.picture_path = ps.img_path;
            File imgFile = new File(picture_path);
            if(imgFile.exists()) {
                Bitmap bm = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                if(bm != null) {
                    this.profilepic = bm;
                } else {
                    Log.e("PLANTCONSTRUCT", "ERROR! image file exists, but created bitmap is null.");
                }
            } else {
                Log.e("PLANTCONSTRUCT", "ERROR! has_picture is true but image file doesn't exist.");
                has_picture = false;
            }
            this.logPicPaths = ps.logpicpaths;
            this.logPicTS = ps.logpictimes;
        }
        this.comments.addAll(ps.comments);
        this.pre_existing = ps.pre_existing;
        this.potsize_na = ps.potsize_na;
        this.lifestage = ps.lifeCycleStage;
        this.deathcause = ps.causeOfDeath;
        this.has_flowers = ps.has_flowers;
        this.has_fruits = ps.has_fruit;
    }

    //------------------------ CUSTOM FUNCS -------------------------------------//

    public PlantSave toSave() {
        ArrayList<PlantLogItemSave> logsave = new ArrayList<>();
        for(PlantLogItem pli: log) {
            logsave.add(pli.toSave());
        }
        return new PlantSave(potsize, potsize_na, owned_since, pre_existing, name, planttype, location, acqTyp, logsave, has_picture, picture_path, logPicPaths, logPicTS, comments,
                has_flowers, has_fruits, deathcause, lifestage);
    }

    public void water() {
        PlantAction pa = new PlantAction(LocalDateTime.now());
        pa.setActTyp(PlantActionType.WATER);
        log.add(pa);
    }

    public void fertilize() {
        PlantAction pa = new PlantAction(LocalDateTime.now());
        pa.setActTyp(PlantActionType.FERTILIZE);
        log.add(pa);
    }


    @Override
    public int compareTo(Plant o) {
        return name.compareTo(o.getName());
    }
}
