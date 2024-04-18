package com.example.plantdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.plantdiary.datadapt.DeadPlantAdapter;
import com.example.plantdiary.io.DeadPlant;
import com.example.plantdiary.io.PlantDiaryIO;

import java.util.ArrayList;
import java.util.Locale;

public class DeadPlantActivity extends AppCompatActivity implements DeadPlantAdapter.DeadPlantRemovedListener {

    RecyclerView deadPlantRCLV;

    TextView deadPlantCntTV;

    ArrayList<DeadPlant> fallen_brothers = new ArrayList<>();


    void initViews() {
        deadPlantCntTV = findViewById(R.id.TV_deadplant_cntcnt);
        deadPlantRCLV = findViewById(R.id.RCLV_deadplants);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dead_plant);

        initViews();

        fallen_brothers = PlantDiaryIO.loadDeadPlants(this);

        deadPlantCntTV.setText(String.format(Locale.getDefault(), "Pour one out for %d fallen brothers.", fallen_brothers.size()));

        deadPlantRCLV.setLayoutManager(new GridLayoutManager(this, 2));
        deadPlantRCLV.setAdapter(new DeadPlantAdapter(this, getSupportFragmentManager(), fallen_brothers));

    }

    @Override
    public void onDeadPlantRemoved() {
        deadPlantCntTV.setText(String.format(Locale.getDefault(), "Pour one out for %d fallen brothers.", fallen_brothers.size()));
    }
}