package com.example.plantdiary;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantdiary.datadapt.PlantAdapter;
import com.example.plantdiary.dialog.AttachCommentDialog;
import com.example.plantdiary.dialog.CauseOfDeathDialog;
import com.example.plantdiary.dialog.NewPlantDialog;
import com.example.plantdiary.io.DeadPlant;
import com.example.plantdiary.io.FullData;
import com.example.plantdiary.io.PlantDiaryIO;
import com.example.plantdiary.io.PlantSave;
import com.example.plantdiary.plant.Plant;
import com.example.plantdiary.plant.PlantGridData;
import com.example.plantdiary.plantaction.CauseOfDeath;
import com.example.plantdiary.plantaction.Comment;
import com.example.plantdiary.plantaction.PlantActionType;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements PlantAdapter.PlantActionListener,
                                                                NewPlantDialog.PlantAddedListener,
                                                                PlantAdapter.PlantRemovedListener,
                                                                PlantAdapter.PlantEditDialogLauncher,
                                                                CauseOfDeathDialog.CauseOfDeathListener,
                                                                AttachCommentDialog.AttachCommentListener {
    //------------ CONST ---------------------------------------------//

    final boolean LOAD_LEGACY = false;

    //------------- VIEWS -------------------------------------------//
    Button newPlantBtn, waterBtn, fertBtn, commentBtn, fallenComradesBtn;

    Button exportBtn, importBtn;

    ConstraintLayout controlsCLYT;

    SwitchCompat showCtrlSWTC;

    TextView plantcntTv;
    RecyclerView plantRecView;

    //------------- VARS -------------------------------------------//

    boolean show_ctrls = false;

    ArrayList<Plant> plants = new ArrayList<>();
    ArrayList<PlantGridData> pgdat = new ArrayList<>();

    ArrayList<DeadPlant> fallen_brothers = new ArrayList<>();


    //------------- CUSTOM FUNCS -------------------------------------------//

    void launchDeadPlantActivity() {
        try {
            PlantDiaryIO.saveData(this, plants);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent plantIntent = new Intent(MainActivity.this, DeadPlantActivity.class);
        ActivityCompat.startActivity(this, plantIntent, null);
    }

    void showNewPlantDialog() {
        FragmentManager fragMan = getSupportFragmentManager();
        NewPlantDialog newPlantDial = new NewPlantDialog(this);
        FragmentTransaction transaction = fragMan.beginTransaction();
        // For a polished look, specify a transition animation.
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity.
        transaction.add(android.R.id.content, newPlantDial)
                .addToBackStack(null).commit();

        //newPlantDial.show(fragMan, "newplantdial");
    }

    void showEditPlantDialog(int idx) {
        FragmentManager fragMan = getSupportFragmentManager();
        Plant toEdit = plants.get(idx);
        NewPlantDialog newPlantDial = new NewPlantDialog(this, toEdit, idx);
        FragmentTransaction transaction = fragMan.beginTransaction();
        // For a polished look, specify a transition animation.
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        // To make it fullscreen, use the 'content' root view as the container
        // for the fragment, which is always the root view for the activity.
        transaction.add(android.R.id.content, newPlantDial)
                .addToBackStack(null).commit();
    }

    void launchAttachCommentDialog(int plantidx) {
        FragmentManager fragMan = getSupportFragmentManager();
        AttachCommentDialog acDial = new AttachCommentDialog((AttachCommentDialog.AttachCommentListener) this, plantidx);
        acDial.show(fragMan, "attachcomment");
    }

    public void launchPlantActivity(int plantidx) {
        try {
            PlantDiaryIO.saveData(this, plants);
        } catch(Exception e) {
            e.printStackTrace();
        }
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

    public void export_dat() {
        //Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);


        //startActivityForResult(intent, 3);
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "plantlogsave");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        //intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, Util.ACTCODE_EXPORT);

    }

    public void import_dat() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");


        startActivityForResult(intent, Util.ACTCODE_IMPORT);
    }

    //------------- OVERRIDES -------------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*if(LOAD_LEGACY) {
            plants = PlantDiaryIO.loadLegacyData(this);
            PlantDiaryIO.saveData(this, plants);
            finish();
        }*/
        plants = PlantDiaryIO.loadData(this);
        fallen_brothers = PlantDiaryIO.loadDeadPlants(this);

        newPlantBtn = findViewById(R.id.BTN_newplant);
        waterBtn = findViewById(R.id.BTN_water);
        fertBtn = findViewById(R.id.BTN_fertilize);
        plantRecView = findViewById(R.id.RCLVW_plants);
        plantcntTv = findViewById(R.id.TV_main_plantcnt);
        controlsCLYT = findViewById(R.id.CSTRLYT_plantactions);
        showCtrlSWTC = findViewById(R.id.SWTCH_show_actions);
        commentBtn = findViewById(R.id.BTN_comment);
        fallenComradesBtn = findViewById(R.id.BTN_main_fallencomrades);

        exportBtn = findViewById(R.id.BTN_export);
        importBtn = findViewById(R.id.BTN_import);


        controlsCLYT.setVisibility(View.GONE);

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                export_dat();
            }
        });

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                import_dat();
            }
        });

        plantRecView.setLayoutManager(new GridLayoutManager(this, 2));
        pgdat = new ArrayList<>();
        for(Plant p: plants) {
            pgdat.add(new PlantGridData(p));
        }
        PlantAdapter plAdapt = new PlantAdapter(this, pgdat);
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

        fallenComradesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDeadPlantActivity();
            }
        });

        plantcntTv.setText(String.format(Locale.getDefault(), "%d Pflanzen", plants.size()));
    }



    @Override
    protected void onPause() {
        try {
            PlantDiaryIO.saveData(this, plants);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onPlantAdded(Plant plant) {
        Log.d("ON_PLANT_ADDED", "added plant "+plant.getName());
        int pos = 0;
        while(pos < plants.size() && plants.get(pos).compareTo(plant)<0) {
            pos++;
        }
        plants.add(pos, plant);
        pgdat.add(pos, new PlantGridData(plant));
        try {
            PlantDiaryIO.saveData(this, plants);
        } catch(Exception e) {
            Log.d("ON_PLANT_ADDED", "Error saving data: "+e.getLocalizedMessage());
        }
        plantRecView.getAdapter().notifyItemInserted(pos);
        plantRecView.getAdapter().notifyItemRangeChanged(pos, 1);
        plantcntTv.setText(String.format(Locale.getDefault(), "%d Pflanzen, %d Einträge", plants.size(), plantRecView.getAdapter().getItemCount()));
    }

    @Override
    public void onPlantChanged(Plant plant, int pos) {
        plants.set(pos, plant);
        pgdat.set(pos, new PlantGridData(plant));
        plantRecView.getAdapter().notifyItemChanged(pos);
        plantRecView.getAdapter().notifyItemRangeChanged(pos, 1);
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
    public void onPlantRemoved(int plantidx) {
        CauseOfDeathDialog codDial = new CauseOfDeathDialog(this, plantidx, plants.get(plantidx));
        codDial.show(getSupportFragmentManager(), "slctcod");
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
        else if (requestCode == Util.ACTCODE_EXPORT && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            Uri uri = null;
            if (data != null) {
                uri =data.getData();

                if(PlantDiaryIO.exportData(plants, fallen_brothers, uri, getContentResolver())) {
                    Snackbar.make(this, exportBtn, "Exported "+plants.size()+" plantlogs and " + fallen_brothers.size() + "dead plants!", BaseTransientBottomBar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(this, exportBtn, "Error exporting "+plants.size()+" plantlogs and " + fallen_brothers.size() + "dead plants :(", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        }else if(requestCode == Util.ACTCODE_IMPORT && resultCode == Activity.RESULT_OK) {
           Uri uri = null;
            if (data != null) {
                uri = data.getData();

                FullData dat = PlantDiaryIO.importData(uri, getContentResolver());
                if (!dat.isEmpty()) {
                    Snackbar.make(this, importBtn, "Imported " + dat.plants.size() + " plants and "+dat.deadplants.size()+" dead plants!", BaseTransientBottomBar.LENGTH_SHORT).show();
                    plants = dat.plants;
                    fallen_brothers = dat.deadplants;
                    pgdat = new ArrayList<>();
                    for(Plant p: plants) {
                        pgdat.add(new PlantGridData(p));
                    }
                    PlantAdapter plAdapt = new PlantAdapter(this, pgdat);
                    plantRecView.setAdapter(plAdapt);
                    try {
                        PlantDiaryIO.saveData(this, plants);
                        PlantDiaryIO.saveDeadPlants(this, fallen_brothers);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    Snackbar.make(this, importBtn, "Error importing "+ dat.plants.size() + " plants and "+dat.deadplants.size()+" dead plants!", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        }

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

    @Override
    public void onCauseSelected(Plant plant, int plantidx, CauseOfDeath cod) {
        plants.remove(plantidx);
        pgdat.remove(plantidx);
        plantRecView.getAdapter().notifyItemRemoved(plantidx);


        plantcntTv.setText(String.format(Locale.getDefault(), "%d Pflanzen, %d Einträge", plants.size(), plantRecView.getAdapter().getItemCount()));
        for(String s: plant.getLogPicPaths()) {
            File imgPath = new File(s);
            if(imgPath.exists()) imgPath.delete();
        }

        fallen_brothers.add(new DeadPlant(plant, cod));
        try {
            PlantDiaryIO.saveDeadPlants(this, fallen_brothers);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPlantWatered(int plantidx) {
        plants.get(plantidx).water();
        Snackbar.make(plantRecView, String.format(Locale.getDefault(), "Watered %s", plants.get(plantidx).getName()),
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onPlantFertilized(int plantidx) {
        plants.get(plantidx).fertilize();
        Snackbar.make(plantRecView, String.format(Locale.getDefault(), "Fertilized %s", plants.get(plantidx).getName()),
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onPlantCommented(int plantidx) {

    }

    @Override
    public void attachComment(Comment cmt, int idx) {
        plants.get(idx).getComments().add(cmt);
    }
}