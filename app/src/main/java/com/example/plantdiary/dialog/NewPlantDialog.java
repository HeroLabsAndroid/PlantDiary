package com.example.plantdiary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.example.plantdiary.cam.BitmapAndPath;
import com.example.plantdiary.cam.CamOps;
import com.example.plantdiary.plant.AcquisitionType;
import com.example.plantdiary.plant.Plant;

public class NewPlantDialog extends DialogFragment {

    //---------- INTERFACES --------------------------------------//

    public interface PlantAddedListener {
        void onPlantAdded(Plant plant);
        boolean plantNameTaken(String name);
    }

    //--------- VARS ----------------------------------------------//

    String img_path;
    boolean has_img = false;

    PlantAddedListener plListen;

    Button confirmBUTT, photoBUTT;

    ImageView photoIV;
    EditText nameET, typeET, locET, potszET;

    TextView dateTV;

    RadioGroup acqTypeRADGRP;
    RadioButton typeAdoptedRADBUTT, typeRaisedRADBUTT;

    //----------- FUNCTIONS ---------------------------------------------//

    boolean takeProfilePic() {
        BitmapAndPath bmpath = CamOps.dispatchPictureIntent(requireActivity(), makePlant());
        if(!(bmpath.path.compareTo(getString(R.string.FLAG_err))==0)) {
            has_img = true;
            img_path = bmpath.path;
            photoIV.setImageBitmap(bmpath.bm);
        }

        return has_img;
    }

    void confirmPlantAdded() {
        if(!plListen.plantNameTaken(nameET.getText().toString()))
            plListen.onPlantAdded(makePlant());
        dismiss();
    }

    Plant makePlant() {
        Plant p = new Plant(nameET.getText().toString(),
                typeET.getText().toString(),
                typeAdoptedRADBUTT.isChecked() ? AcquisitionType.ADOPTED : AcquisitionType.SELFRAISED);
        p.setLocation(locET.getText().toString());
        if(potszET.getText().toString().compareTo(getString(R.string.PROMPT_defineplant_potsize)) != 0) {
            p.setPotsize(Float.parseFloat(potszET.getText().toString()));
        } else p.setPotsize(0);


        return p;
    }


    //---------- CONSTRUCTORS -------------------------------------------//

    public NewPlantDialog(PlantAddedListener listener) {
        plListen = listener;
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

        acqTypeRADGRP = layout.findViewById(R.id.RADGRP_addplant_acqtyp);
        typeAdoptedRADBUTT = layout.findViewById(R.id.RADBTN_addplant_acqtyp_adopted);
        typeRaisedRADBUTT = layout.findViewById(R.id.RADBTN_addplant_acqtyp_raised);

        dateTV = layout.findViewById(R.id.TV_addplant_ownedsince);

        photoIV = layout.findViewById(R.id.IV_addplant_profpic);

        photoBUTT = layout.findViewById(R.id.BTN_addplant_takephoto);

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

        return builder.create();
    }


}
