package com.example.plantdiary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.plantdiary.R;
import com.example.plantdiary.Util;
import com.example.plantdiary.cam.BitmapAndPath;
import com.example.plantdiary.cam.CamOps;
import com.example.plantdiary.plant.AcquisitionType;
import com.example.plantdiary.plant.Plant;
import com.example.plantdiary.plantaction.CauseOfDeath;
import com.example.plantdiary.plantaction.Comment;
import com.example.plantdiary.plantaction.LifeCycleStage;
import com.example.plantdiary.plantaction.PlantEvent;
import com.example.plantdiary.plantaction.PlantEventType;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class CauseOfDeathDialog extends DialogFragment {

    //---------- INTERFACES --------------------------------------//

    public interface CauseOfDeathListener {
        void onCauseSelected(Plant plant, int plantidx, CauseOfDeath cod);
    }

    //--------- VARS ----------------------------------------------//
    Plant p;
    int idx;

    CauseOfDeathListener codListen;

    Button confirmBUTT;
    Spinner deathSPN;

    //----------- FUNCTIONS ---------------------------------------------//



    void confirmCauseOfDeath() {
        codListen.onCauseSelected(p, idx, CauseOfDeath.fromOrdinal(deathSPN.getSelectedItemPosition()));

        dismiss();
    }




    //---------- CONSTRUCTORS -------------------------------------------//


    public CauseOfDeathDialog(CauseOfDeathListener listener, int idx, Plant p) {
        codListen = listener;
        this.p = p;
        this.idx = idx;


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
        View layout = inflater.inflate(R.layout.dlg_cause_of_death, null);

        builder.setView(layout);

        confirmBUTT = layout.findViewById(R.id.BTN_causedeath_confirm);


        deathSPN = layout.findViewById(R.id.SPN_causedeath);



        confirmBUTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmCauseOfDeath();
            }
        });


        ArrayAdapter<String> deatharr = new ArrayAdapter<String>(requireContext(), R.layout.spnitm_lifecycle, CauseOfDeath.getCauses());
        deatharr.setDropDownViewResource(R.layout.spnitm_lifecycle);
        deathSPN.setAdapter(deatharr);


        return builder.create();
    }


}
