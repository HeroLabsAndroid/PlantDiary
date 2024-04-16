package com.example.plantdiary.plant;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;

import com.example.plantdiary.Util;
import com.example.plantdiary.io.PlantActionSave;
import com.example.plantdiary.io.PlantEventSave;
import com.example.plantdiary.io.PlantLogItemSave;
import com.example.plantdiary.io.PlantSave;
import com.example.plantdiary.plantaction.PlantAction;
import com.example.plantdiary.plantaction.PlantActionType;
import com.example.plantdiary.plantaction.PlantEvent;
import com.example.plantdiary.plantaction.PlantLogItem;

import java.io.File;
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

    private String comment = "";
    private String planttype;

    private String location;

    private ArrayList<PlantLogItem> log = new ArrayList<>();

    private Bitmap profilepic;

    private String picture_path;

    private ArrayList<String> logPicPaths = new ArrayList<>();
    private ArrayList<LocalDateTime> logPicTS = new ArrayList<>();
    private boolean has_picture = false;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        this.comment = ps.comment;

    }

    //------------------------ CUSTOM FUNCS -------------------------------------//

    public PlantSave toSave() {
        ArrayList<PlantLogItemSave> logsave = new ArrayList<>();
        for(PlantLogItem pli: log) {
            logsave.add(pli.toSave());
        }
        return new PlantSave(potsize, owned_since, name, planttype, location, acqTyp, logsave, has_picture, picture_path, logPicPaths, logPicTS, comment);
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


}
