package com.example.plantdiary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plantdiary.cam.BitmapAndTimestamp;
import com.example.plantdiary.datadapt.CommentAdapter;
import com.example.plantdiary.datadapt.PlantLogAdapter;
import com.example.plantdiary.dialog.ShowPhotoDialog;
import com.example.plantdiary.io.PlantSave;
import com.example.plantdiary.plant.Plant;

import java.util.Locale;

public class PlantActivity extends AppCompatActivity implements PlantLogAdapter.ItemRemovedListener, PlantRollActivity.PlantRollListener, CommentAdapter.CommentRemovedListener {


    //-------------- INTERFACES ----------------------------------//



    //-------------- VARS -----------------------------------------//

    Intent intent;

    public final int ACTIVITY_ID = 1;

    boolean show_log = true;

    Plant plant;
    int plantidx;

    RecyclerView rclvLog;
    View inclType, inclLoc, inclPotsize, inclAcqtyp, inclDate, inclStage;

    TextView tvTit_Type, tvVal_Type, tvTit_Loc, tvVal_Loc, tvTit_potsz, tvVal_potsz, tvTit_actyp, tvVal_actyp, tvTit_date, tvVal_date, tvTit_stage, tvVal_stage;

    TextView tvName;

    TextView tvLog;

    ImageView ivProfPic;

    Button btnShowPhotos, btnSwitchLog;

    //------------ CUSTOM FUNCS ------------------------------------//

    void setRecViewMode(boolean log) {
        if(log) {
            tvLog.setText(String.format(Locale.getDefault(), "Log: \r\n(%d Items)", plant.getLog().size()));
            btnSwitchLog.setText("Show Commies");
            rclvLog.setLayoutManager(new LinearLayoutManager(this));
            rclvLog.setAdapter(new PlantLogAdapter(this, plant.getLog()));
        } else {
            tvLog.setText(String.format(Locale.getDefault(), "Commies: \r\n(%d Items)", plant.getComments().size()));
            btnSwitchLog.setText("Show Log");
            rclvLog.setLayoutManager(new LinearLayoutManager(this));
            rclvLog.setAdapter(new CommentAdapter(this, plant.getComments()));
        }
    }

    void fillLayoutWithPlantData() {

        tvVal_date.setText(plant.isPre_existing() ? getString(R.string.LBL_defineplant_preex) : Util.dateToString(plant.getOwned_since()));

        tvName.setText(plant.getName());

        tvVal_Type.setText(plant.getPlanttype());

        tvVal_Loc.setText(plant.getLocation());

        tvVal_potsz.setText(plant.isPotsize_na() ? getString(R.string.LBL_defineplant_nopotsz): String.format(Locale.getDefault(), "%.2f", plant.getPotsize()));

        tvVal_actyp.setText(plant.getAcqTyp().toString());

        tvVal_stage.setText(plant.getLifestage().toString());

        if(plant.hasPicture()) {
            Bitmap bm = Util.RotateBitmap(plant.getProfilepic(), 90);
            ivProfPic.setImageBitmap(bm);
        }

        tvLog.setText(String.format(Locale.getDefault(), "Log: \r\n(%d Items)", plant.getLog().size()));
        btnSwitchLog.setText("Show Commies");

        setRecViewMode(show_log);


        btnShowPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPlantActivity();
            }
        });

        ivProfPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(plant.hasPicture()) {
                    FragmentManager fragMan = getSupportFragmentManager();
                    ShowPhotoDialog spDial = new ShowPhotoDialog(new BitmapAndTimestamp(plant.getProfilepic(), null));
                    spDial.show(fragMan, "profpicdial");
                }

            }
        });

        btnSwitchLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_log = !show_log;
                setRecViewMode(show_log);
            }
        });


    }

    void launchPlantActivity() {
        Intent plantIntent = new Intent(PlantActivity.this, PlantRollActivity.class);
        plantIntent.putExtra("plant", plant.toSave());
        ActivityCompat.startActivityForResult(this, plantIntent, Util.ACTCODE_PLANTROLL, null);
    }

    void initViews() {
        tvName = findViewById(R.id.TV_plant_name);

        inclType = findViewById(R.id.INCL_TITVAL_type);
        tvTit_Type = inclType.findViewById(R.id.TV_incl_titval_tit);
        tvTit_Type.setText(getString(R.string.PROMPT_defineplant_type));
        tvVal_Type = inclType.findViewById(R.id.TV_incl_titval_val);

        inclLoc = findViewById(R.id.INCL_TITVAL_loc);
        tvTit_Loc = inclLoc.findViewById(R.id.TV_incl_titval_tit);
        tvTit_Loc.setText(getString(R.string.PROMPT_defineplant_location));
        tvVal_Loc = inclLoc.findViewById(R.id.TV_incl_titval_val);

        inclPotsize = findViewById(R.id.INCL_TITVAL_potsz);
        tvTit_potsz = inclPotsize.findViewById(R.id.TV_incl_titval_tit);
        tvTit_potsz.setText(getString(R.string.PROMPT_defineplant_potsize));
        tvVal_potsz = inclPotsize.findViewById(R.id.TV_incl_titval_val);

        inclAcqtyp = findViewById(R.id.INCL_TITVAL_actyp);
        tvTit_actyp = inclAcqtyp.findViewById(R.id.TV_incl_titval_tit);
        tvTit_actyp.setText(getString(R.string.PROMPT_defineplant_acqtyp));
        tvVal_actyp = inclAcqtyp.findViewById(R.id.TV_incl_titval_val);

        inclDate = findViewById(R.id.INCL_TITVAL_ownedsince);
        tvTit_date = inclDate.findViewById(R.id.TV_incl_titval_tit);
        tvTit_date.setText(getString(R.string.PROMPT_defineplant_date));
        tvVal_date = inclDate.findViewById(R.id.TV_incl_titval_val);

        inclStage = findViewById(R.id.INCL_TITVAL_S_stage);
        tvTit_stage = inclStage.findViewById(R.id.TV_incl_titval_tit);
        tvTit_stage.setText(getString(R.string.PROMPT_defineplant_stage));
        tvVal_stage = inclStage.findViewById(R.id.TV_incl_titval_val);

        ivProfPic = findViewById(R.id.IV_plant_profpic);

        tvLog = findViewById(R.id.TV_plant_log);

        rclvLog = findViewById(R.id.RCLV_plant_log);

        btnShowPhotos = findViewById(R.id.BTN_plant_showphotos);

        btnSwitchLog = findViewById(R.id.BTN_plant_logmode);


    }

    //------------- OVERRIDES --------------------------------------//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);

        intent = getIntent();
        plantidx = intent.getIntExtra("idx", -1);
        if(plantidx == -1) {
            Log.e("PlantActivity:ONCREATE", "ERROR reading plant index");
            finish();
        }

        try {
            plant = new Plant((PlantSave) intent.getSerializableExtra("plant"));
        } catch (Exception e) {
            Log.e("PlantActivity:ONCREATE", "ERROR reading plant data");
            finish();
        }

        initViews();

        fillLayoutWithPlantData();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        Intent retIntent = new Intent();
        retIntent.putExtra("plantretidx", plantidx);
        retIntent.putExtra("plantret", plant.toSave());
        setResult(RESULT_OK, retIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onItemRemoved(int pos) {
        tvLog.setText(String.format(Locale.getDefault(), "Log \r\n(%d Items)", plant.getLog().size()));
    }

    @Override
    public void onPhotoAdded(Plant p) {
        plant = p;
    }

    @Override
    public void onPhotoRemoved(int pos) {
        plant.getLogPicPaths().remove(pos);
        plant.getLogPicTS().remove(pos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == Util.ACTCODE_PLANTROLL) {
            if(data.hasExtra("plantret")) {

                plant = new Plant((PlantSave)data.getExtras().getSerializable("plantret"));
            }
        }
    }

    @Override
    public void onCommentRemoved(int idx) {
        tvLog.setText(String.format(Locale.getDefault(), "Commies: \r\n(%d Items)", plant.getComments().size()));
    }
}