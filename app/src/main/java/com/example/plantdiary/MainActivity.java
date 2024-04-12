package com.example.plantdiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.plantdiary.datadapt.PlantAdapter;
import com.example.plantdiary.dialog.NewPlantDialog;
import com.example.plantdiary.io.PlantDiaryIO;
import com.example.plantdiary.plant.Plant;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NewPlantDialog.PlantAddedListener, PlantAdapter.PlantRemovedListener {
    //------------- VIEWS -------------------------------------------//
    Button newPlantBtn;

    TextView plantcntTv;
    RecyclerView plantRecView;

    //------------- VARS -------------------------------------------//

    ArrayList<Plant> plants = new ArrayList<>();

    //------------- CUSTOM FUNCS -------------------------------------------//
    void showNewPlantDialog() {
        FragmentManager fragMan = getSupportFragmentManager();
        NewPlantDialog newPlantDial = new NewPlantDialog(this);
        newPlantDial.show(fragMan, "newplantdial");
    }

    //------------- OVERRIDES -------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plants = PlantDiaryIO.loadData(this);

        newPlantBtn = findViewById(R.id.BTN_newplant);
        plantRecView = findViewById(R.id.RCLVW_plants);
        plantcntTv = findViewById(R.id.TV_main_plantcnt);

        plantRecView.setLayoutManager(new GridLayoutManager(this, 2));
        PlantAdapter plAdapt = new PlantAdapter(this, plants);
        plantRecView.setAdapter(plAdapt);

        newPlantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewPlantDialog();
            }
        });

        plantcntTv.setText(String.format(Locale.getDefault(), "%d Pflanzen", plants.size()));
    }

    @Override
    protected void onPause() {
        PlantDiaryIO.saveData(this, plants);
        super.onPause();
    }

    @Override
    public void onPlantAdded(Plant plant) {
        plants.add(plant);
        plantRecView.getAdapter().notifyItemInserted(plants.size()-1);
        plantRecView.getAdapter().notifyItemRangeChanged(plants.size()-1, 1);
        plantcntTv.setText(String.format(Locale.getDefault(), "%d Pflanzen, %d Einträge", plants.size(), plantRecView.getAdapter().getItemCount()));
    }

    @Override
    public boolean plantNameTaken(String name) {
        for(int i=0; i<plants.size(); i++) {
            if(name.compareToIgnoreCase(plants.get(i).getName()) == 0) {
                Snackbar.make(newPlantBtn, "Name existiert schon", Snackbar.LENGTH_SHORT).show();

                return true;
            }

        }
        return false;
    }

    @Override
    public void onPlantRemoved() {

    }
}