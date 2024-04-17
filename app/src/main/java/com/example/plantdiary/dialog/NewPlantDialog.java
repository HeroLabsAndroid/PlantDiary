package com.example.plantdiary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.plantdiary.R;
import com.example.plantdiary.Util;
import com.example.plantdiary.cam.BitmapAndPath;
import com.example.plantdiary.cam.CamOps;
import com.example.plantdiary.io.PlantEventSave;
import com.example.plantdiary.plant.AcquisitionType;
import com.example.plantdiary.plant.Plant;
import com.example.plantdiary.plantaction.Comment;
import com.example.plantdiary.plantaction.PlantEvent;
import com.example.plantdiary.plantaction.PlantEventType;
import com.example.plantdiary.plantaction.PlantLogItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class NewPlantDialog extends DialogFragment {

    //---------- INTERFACES --------------------------------------//

    public interface PlantAddedListener {
        void onPlantAdded(Plant plant);
        void onPlantChanged(Plant plant, int pos);
        boolean plantNameTaken(String name);
    }

    //--------- VARS ----------------------------------------------//

    boolean edit_mode = false;
    int plantidx = 0;
    Plant plant;
    ArrayList<Comment> cmt = new ArrayList<>();

    String img_path;
    boolean has_img = false;

    PlantAddedListener plListen;

    Button confirmBUTT, photoBUTT;
    FloatingActionButton commentBUTT;

    Chip preexCHIP;

    ImageView photoIV;
    EditText nameET, typeET, locET, potszET, commentET;

    TextView dateTV, commentcntTV;

    RadioGroup acqTypeRADGRP;
    RadioButton typeAdoptedRADBUTT, typeRaisedRADBUTT;

    //----------- FUNCTIONS ---------------------------------------------//

    boolean takeProfilePic() {
        BitmapAndPath bmpath = CamOps.dispatchPictureIntent(requireActivity(), makePlant(), true);
        if(!(bmpath.path.compareTo(getString(R.string.FLAG_err))==0)) {
            has_img = true;
            img_path = bmpath.path;
            File imgFile = new File(img_path);
            if(imgFile.exists()) {
                photoIV.setImageBitmap(BitmapFactory.decodeFile(bmpath.path));
                photoIV.refreshDrawableState();
            } else {
                Log.e("TAKEPROFPIC", "ERROR! Image File not found...");
            }

        }

        return has_img;
    }

    void confirmPlantAdded() {
        if(!edit_mode) {
            if(!plListen.plantNameTaken(nameET.getText().toString()))
                plListen.onPlantAdded(makePlant());
        } else {
            plListen.onPlantChanged(makePlant(), plantidx);
        }

        dismiss();
    }

    Plant makePlant() {

        if(!edit_mode) {
            plant = new Plant(nameET.getText().toString(),
                    typeET.getText().toString(),
                    typeAdoptedRADBUTT.isChecked() ? AcquisitionType.ADOPTED : AcquisitionType.SELFRAISED);
            plant.setOwned_since(LocalDate.now());
            plant.setComments(cmt);
        } else {
            if(plant.getName().compareTo(nameET.getText().toString())!=0) {
                PlantEvent pe = new PlantEvent(LocalDateTime.now());
                pe.setPet(PlantEventType.RENAME);
                plant.getLog().add(pe);
            }
            plant.setName(nameET.getText().toString());
            plant.setPlanttype(typeET.getText().toString());
            plant.setAcqTyp(typeAdoptedRADBUTT.isChecked() ? AcquisitionType.ADOPTED : AcquisitionType.SELFRAISED);
            plant.setComments(cmt);
        }


        if(edit_mode && plant.getLocation().compareTo(locET.getText().toString())!=0) {
            PlantEvent pe = new PlantEvent(LocalDateTime.now());
            pe.setPet(PlantEventType.RELOCATE);
            plant.getLog().add(pe);
        }
        plant.setLocation(locET.getText().toString());

        if(edit_mode && plant.getPotsize()!=Float.parseFloat(potszET.getText().toString())) {
            PlantEvent pe = new PlantEvent(LocalDateTime.now());
            pe.setPet(PlantEventType.REPOT);
            plant.getLog().add(pe);
        }

        if(potszET.getText().toString().compareTo(getString(R.string.PROMPT_defineplant_potsize)) != 0) {
            plant.setPotsize(Float.parseFloat(potszET.getText().toString()));
        } else plant.setPotsize(0);

        if(has_img && img_path.compareTo(getString(R.string.FLAG_err))!=0) {
            plant.setPicture_path(img_path);
            File imgFile = new File(img_path);
            if(imgFile.exists()) {
                CamOps.replaceWithDownscaled(getContext(), img_path);
                plant.setProfilepic(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
            } else {
                Log.e("MKPLANT", "ERROR! Image File not found...");
            }
        }

        if(preexCHIP.isChecked()) {
            plant.setPre_existing(true);
        }


        return plant;
    }

    void fillDialogFromPlant(Plant p) {
        nameET.setText(p.getName());

        typeET.setText(p.getPlanttype());

        if(p.getAcqTyp()==AcquisitionType.ADOPTED)
            typeAdoptedRADBUTT.setChecked(true);
        else typeRaisedRADBUTT.setChecked(true);

        locET.setText(p.getLocation());
        potszET.setText(String.format(Locale.getDefault(), "%.2f", p.getPotsize()));



        if(p.isPre_existing()) {
            preexCHIP.setChecked(true);
            dateTV.setText("Pre-Existing");
        } else {
            dateTV.setText(Util.dateToString(p.getOwned_since()));
        }

        cmt = p.getComments();
        commentcntTV.setText(String.format(Locale.getDefault(), "%d Kommis", cmt.size()));
    }

    void addComment() {
        if(commentET.getText().toString().compareTo(getString(R.string.PROMT_defineplant_comment))!=0) {
            cmt.add(new Comment(commentET.getText().toString(), LocalDateTime.now()));
            commentET.setText(getString(R.string.PROMT_defineplant_comment));
            commentcntTV.setText(String.format(Locale.getDefault(), "%d Kommis", cmt.size()));
            commentET.selectAll();
        }
    }


    //---------- CONSTRUCTORS -------------------------------------------//

    public NewPlantDialog(PlantAddedListener listener) {
        plListen = listener;
        edit_mode = false;
    }

    public NewPlantDialog(PlantAddedListener listener, Plant p, int plantidx) {
        plListen = listener;
        this.plantidx = plantidx;
        plant = p;
        edit_mode = true;


    }

    //---------- OVERRIDES ----------------------------------------------//
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dlg_defineplant, null);

        builder.setView(layout);

        confirmBUTT = layout.findViewById(R.id.BTN_addplant_confirm);

        nameET = layout.findViewById(R.id.ET_addplant_name);
        typeET = layout.findViewById(R.id.ET_addplant_type);
        locET = layout.findViewById(R.id.ET_addplant_loc);
        potszET = layout.findViewById(R.id.ET_addplant_potsize);
        commentET = layout.findViewById(R.id.ET_addplant_comment);

        acqTypeRADGRP = layout.findViewById(R.id.RADGRP_addplant_acqtyp);
        typeAdoptedRADBUTT = layout.findViewById(R.id.RADBTN_addplant_acqtyp_adopted);
        typeRaisedRADBUTT = layout.findViewById(R.id.RADBTN_addplant_acqtyp_raised);

        dateTV = layout.findViewById(R.id.TV_addplant_ownedsince);

        photoIV = layout.findViewById(R.id.IV_addplant_profpic);

        photoBUTT = layout.findViewById(R.id.BTN_addplant_takephoto);
        commentBUTT = layout.findViewById(R.id.BTN_addplant_addcomment);
        commentcntTV = layout.findViewById(R.id.TV_addplant_commentcnt);

        preexCHIP = layout.findViewById(R.id.CHP_addplant_preex);

        photoBUTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeProfilePic();
            }
        });


        confirmBUTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPlantAdded();
            }
        });

        commentBUTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        if(edit_mode) fillDialogFromPlant(plant);

        return builder.create();
    }


}
