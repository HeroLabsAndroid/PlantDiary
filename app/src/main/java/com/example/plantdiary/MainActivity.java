package com.example.plantdiary;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantdiary.datadapt.PlantAdapter;
import com.example.plantdiary.dialog.AttachCommentDialog;
import com.example.plantdiary.dialog.NewPlantDialog;
import com.example.plantdiary.io.PlantDiaryIO;
import com.example.plantdiary.io.PlantSave;
import com.example.plantdiary.plant.Plant;
import com.example.plantdiary.plantaction.PlantActionType;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NewPlantDialog.PlantAddedListener, PlantAdapter.PlantRemovedListener, PlantAdapter.PlantEditDialogLauncher {
    //------------ CONST ---------------------------------------------//

    final boolean LOAD_LEGACY = false;

    //------------- VIEWS -------------------------------------------//
    Button newPlantBtn, waterBtn, fertBtn, commentBtn;

    ConstraintLayout controlsCLYT;

    SwitchCompat showCtrlSWTC;

    TextView plantcntTv;
    RecyclerView plantRecView;

    //------------- VARS -------------------------------------------//

    boolean show_ctrls = false;

    ArrayList<Plant> plants = new ArrayList<>();


    //------------- CUSTOM FUNCS -------------------------------------------//
    void showNewPlantDialog() {
        FragmentManager fragMan = getSupportFragmentManager();
        NewPlantDialog newPlantDial = new NewPlantDialog(this);
        newPlantDial.show(fragMan, "newplantdial");
    }

    void showEditPlantDialog(int idx) {
        FragmentManager fragMan = getSupportFragmentManager();
        NewPlantDialog newPlantDial = new NewPlantDialog(this, plants.get(idx), idx);
        newPlantDial.show(fragMan, "newplantdial");
    }

    void launchAttachCommentDialog(int plantidx) {
        FragmentManager fragMan = getSupportFragmentManager();
        AttachCommentDialog acDial = new AttachCommentDialog((AttachCommentDialog.AttachCommentListener) plantRecView.getAdapter(), plantidx);
        acDial.show(fragMan, "attachcomment");
    }

    public void launchPlantActivity(int plantidx) {
        PlantDiaryIO.saveData(this, plants);
        Intent plantIntent = new Intent(MainActivity.this, PlantActivity.class);
        plantIntent.putExtra("idx", plantidx);
        plantIntent.putExtra("plant", plants.get(plantidx).toSave());
        ActivityCompat.startActivityForResult(this, plantIntent, Util.ACTCODE_PLANT, null);
    }

    public void switchSelectedAction(PlantActionType pat) {
        if(((PlantAdapter) plantRecView.getAdapter()).getSelectedAction() == pat) {
            ((PlantAdapter) plantRecView.getAdapter()).setSelectedAction(PlantActionType.NONE);
        } else {
            ((PlantAdapter) plantRecView.getAdapter()).setSelectedAction(pat);
        }
        adjustActionUI();
    }

    private void adjustActionUI() {
        PlantActionType pat = ((PlantAdapter) plantRecView.getAdapter()).getSelectedAction();
        switch (pat) {
            case WATER:
                waterBtn.setBackgroundColor(getColor(R.color.DRK_ACC4));
                fertBtn.setBackgroundColor(getColor(R.color.DRK_GRAYOUT));
                commentBtn.setBackgroundColor(getColor(R.color.DRK_GRAYOUT));
                break;
            case FERTILIZE:
                waterBtn.setBackgroundColor(getColor(R.color.DRK_GRAYOUT));
                fertBtn.setBackgroundColor(getColor(R.color.DRK_ACC5));
                commentBtn.setBackgroundColor(getColor(R.color.DRK_GRAYOUT));
                break;
            case PLANTCOMMENT:
                waterBtn.setBackgroundColor(getColor(R.color.DRK_GRAYOUT));
                fertBtn.setBackgroundColor(getColor(R.color.DRK_GRAYOUT));
                commentBtn.setBackgroundColor(getColor(R.color.DRK_ACC6));
                break;
            default:
                waterBtn.setBackgroundColor(getColor(R.color.DRK_GRAYOUT));
                fertBtn.setBackgroundColor(getColor(R.color.DRK_GRAYOUT));
                commentBtn.setBackgroundColor(getColor(R.color.DRK_GRAYOUT));
                break;
        }
    }

    //------------- OVERRIDES -------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(LOAD_LEGACY) {
            plants = PlantDiaryIO.loadLegacyData(this);
            PlantDiaryIO.saveData(this, plants);
            finish();
        }
        plants = PlantDiaryIO.loadData(this);

        newPlantBtn = findViewById(R.id.BTN_newplant);
        waterBtn = findViewById(R.id.BTN_water);
        fertBtn = findViewById(R.id.BTN_fertilize);
        plantRecView = findViewById(R.id.RCLVW_plants);
        plantcntTv = findViewById(R.id.TV_main_plantcnt);
        controlsCLYT = findViewById(R.id.CSTRLYT_plantactions);
        showCtrlSWTC = findViewById(R.id.SWTCH_show_actions);
        commentBtn = findViewById(R.id.BTN_comment);

        controlsCLYT.setVisibility(View.GONE);

        plantRecView.setLayoutManager(new GridLayoutManager(this, 2));
        PlantAdapter plAdapt = new PlantAdapter(this, plants);
        plantRecView.setAdapter(plAdapt);

        newPlantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchSelectedAction(PlantActionType.NONE);
                showNewPlantDialog();
            }
        });

        waterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchSelectedAction(PlantActionType.WATER);
            }
        });

        fertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchSelectedAction(PlantActionType.FERTILIZE);
            }
        });

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchSelectedAction(PlantActionType.PLANTCOMMENT);
            }
        });

        showCtrlSWTC.setChecked(false);
        showCtrlSWTC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                show_ctrls = isChecked;
                if(show_ctrls) controlsCLYT.setVisibility(View.VISIBLE);
                else {
                    controlsCLYT.setVisibility(View.GONE);
                    switchSelectedAction(PlantActionType.NONE);
                }
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
    public void onPlantChanged(Plant plant, int pos) {
        plants.set(pos, plant);

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
    public void onPlantRemoved(Plant p) {
        plantcntTv.setText(String.format(Locale.getDefault(), "%d Pflanzen, %d Einträge", plants.size(), plantRecView.getAdapter().getItemCount()));
        if(p.hasPicture()) {
            File imgPath = new File(p.getPicture_path());
            if(imgPath.exists()) imgPath.delete();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void startPlantEditDialog(int plantidx) {
        showEditPlantDialog(plantidx);
    }

    @Override
    public void startPlantActivity(int plantidx) {
        launchPlantActivity(plantidx);
    }

    @Override
    public void startAttachCommentDialog(int plantidx) {
        launchAttachCommentDialog(plantidx);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        int errfl = 0;
        if (resultCode == Activity.RESULT_OK && requestCode == Util.ACTCODE_PLANT) {
            int idx = -1;
            if (data.hasExtra("plantretidx")) {

                idx = data.getExtras().getInt("plantretidx");
            } else errfl = 2;
            if(data.hasExtra("plantret")) {
                if(idx>=0) {
                    plants.set(idx, new Plant((PlantSave)data.getExtras().getSerializable("plantret")));
                } else errfl = 3;
            } else errfl = 4;
        } else if(requestCode == Util.ACTCODE_PLANT) errfl = 1;

        if(errfl > 0) {
            String errstr = "";
            switch (errfl) {
                case 1:
                    errstr = "RESULT NOT OKAY";
                    break;
                case 2:
                    errstr = "NO INDEX FOUND";
                    break;
                case 3:
                    errstr = "NEGATIVE INDEX";
                    break;
                case 4:
                    errstr = "NO PLANT DATA";
                    break;
                case 5:
                    errstr = "SURPRISE ERROR";
                    break;
            }
            Toast.makeText(this, errstr,
                    Toast.LENGTH_SHORT).show();
        }
    }
}