package com.example.plantdiary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plantdiary.cam.BitmapAndTimestamp;
import com.example.plantdiary.datadapt.PlantLogAdapter;
import com.example.plantdiary.io.PlantDiaryIO;
import com.example.plantdiary.io.PlantSave;
import com.example.plantdiary.plant.Plant;

import java.util.Locale;

public class PlantActivity extends AppCompatActivity implements PlantLogAdapter.ItemRemovedListener, PlantRollActivity.PlantRollListener {


    //-------------- INTERFACES ----------------------------------//



    //-------------- VARS -----------------------------------------//

    Intent intent;

    public final int ACTIVITY_ID = 1;

    Plant plant;
    int plantidx;

    RecyclerView rclvLog;
    View inclType, inclLoc, inclPotsize, inclAcqtyp, inclComm;

    TextView tvTit_Type, tvVal_Type, tvTit_Loc, tvVal_Loc, tvTit_potsz, tvVal_potsz, tvTit_actyp, tvVal_actyp, tvTit_comm, tvVal_comm;

    TextView tvName;

    TextView tvLog;

    ImageView ivProfPic;

    Button btnShowPhotos;

    //------------ CUSTOM FUNCS ------------------------------------//

    void fillLayoutWithPlantData() {
        tvVal_comm.setText(plant.getComment());

        tvName.setText(plant.getName());

        tvVal_Type.setText(plant.getPlanttype());

        tvVal_Loc.setText(plant.getLocation());

        tvVal_potsz.setText(String.format(Locale.getDefault(), "%.2f", plant.getPotsize()));

        tvVal_actyp.setText(plant.getAcqTyp().toString());

        if(plant.hasPicture()) {
            Bitmap bm = Util.RotateBitmap(plant.getProfilepic(), 90);
            ivProfPic.setImageBitmap(bm);
        }

        tvLog.setText(String.format(Locale.getDefault(), "Log \r\n(%d Items)", plant.getLog().size()));

        rclvLog.setLayoutManager(new LinearLayoutManager(this));
        rclvLog.setAdapter(new PlantLogAdapter(this, plant.getLog()));

        btnShowPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPlantActivity();
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

        inclComm = findViewById(R.id.INCL_TITVAL_comment);
        tvTit_comm = inclComm.findViewById(R.id.TV_incl_titval_tit);
        tvTit_comm.setText("Kommentar: ");
        tvVal_comm = inclComm.findViewById(R.id.TV_incl_titval_val);

        ivProfPic = findViewById(R.id.IV_plant_profpic);

        tvLog = findViewById(R.id.TV_plant_log);

        rclvLog = findViewById(R.id.RCLV_plant_log);

        btnShowPhotos = findViewById(R.id.BTN_plant_showphotos);



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
}