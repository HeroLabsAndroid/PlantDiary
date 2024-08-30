package com.example.plantdiary;

import static com.example.plantdiary.cam.CamOps.REQUEST_IMAGE_CAPTURE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plantdiary.cam.BitmapAndPath;
import com.example.plantdiary.cam.BitmapAndTimestamp;
import com.example.plantdiary.cam.CamOps;
import com.example.plantdiary.datadapt.PlantRollAdapter;
import com.example.plantdiary.dialog.ShowAnimDialog;
import com.example.plantdiary.dialog.ShowPhotoDialog;
import com.example.plantdiary.io.PlantSave;
import com.example.plantdiary.plant.Plant;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class PlantRollActivity extends AppCompatActivity implements PlantRollAdapter.RollPhotoRemovedListener {


    //------------------- INTERFACES --------------------------//

    public interface PlantRollListener {
        void onPhotoAdded(Plant p);
        void onPhotoRemoved(int pos);
    }

    //------------------ VARS ---------------------------------//
    Button btnAddPhoto, btnShowAnim;
    RecyclerView rclvPhoto;

    Plant plant;

    TextView tvPhotoCnt;

    PlantRollListener prListen;

    ArrayList<BitmapAndTimestamp> roll = new ArrayList<>();

    //------------------ CUSTOM FUNCS -----------------------------//

    ArrayList<BitmapAndTimestamp> makeRollArray() {
        ArrayList<BitmapAndTimestamp> out = new ArrayList<>();

        for(int i=0; i<plant.getLogPicPaths().size(); i++) {
            File imgFile = new File(plant.getLogPicPaths().get(i));
            if(imgFile.exists()) {
                //CamOps.replaceWithDownscaled(this, plant.getLogPicPaths().get(i));
                if(plant.getLogPicTS().size()>i)
                    out.add(new BitmapAndTimestamp(BitmapFactory.decodeFile(plant.getLogPicPaths().get(i)), plant.getLogPicTS().get(i)));
                else
                    out.add(new BitmapAndTimestamp(BitmapFactory.decodeFile(plant.getLogPicPaths().get(i)), Util.DEFAULT_DATE));
            }


        }

        for(int i=0; i<out.size(); i++) {
            if(out.get(i).bm.getHeight()<out.get(i).bm.getWidth()) out.get(i).bm = Util.RotateBitmap(out.get(i).bm, 90);
        }
        return out;
    }

    String takeRollPic() {
        BitmapAndPath bmpath = CamOps.dispatchPictureIntent(this, plant, false);
        if(!(bmpath.path.compareTo(getString(R.string.FLAG_err))==0)) {
            plant.getLogPicPaths().add(bmpath.path);
            plant.getLogPicTS().add(LocalDateTime.now().minusSeconds(1));

        }

        return bmpath.path;
    }

    void addRollPic() {
        CamOps.replaceWithDownscaled(this, plant.getLogPicPaths().get(plant.getLogPicPaths().size()-1));
        roll = makeRollArray();
        //rclvPhoto.getAdapter().notifyItemInserted(roll.size());
        //rclvPhoto.getAdapter().notifyItemRangeChanged(roll.size(), 1);
        rclvPhoto.setAdapter(new PlantRollAdapter(this, getSupportFragmentManager(), roll));
        tvPhotoCnt.setText(String.format(Locale.getDefault(), "%d Fotos", plant.getLogPicPaths().size()));

    }

    ShowAnimDialog showAnimDial() {
        ShowAnimDialog animDial = null;
        if(roll.size() > 1) {
            animDial = new ShowAnimDialog(this, roll, this);
            animDial.show(getSupportFragmentManager(), "anim");

        } else {
            Snackbar.make(rclvPhoto, "Too few photos", Snackbar.LENGTH_SHORT).show();
        }
        return animDial;
    }



    //------------------ OVERRIDES ---------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_roll);

        btnAddPhoto = findViewById(R.id.BTN_plantroll_addphoto);
        btnShowAnim = findViewById(R.id.BTN_plantroll_anim);
        rclvPhoto = findViewById(R.id.RCLV_plantroll);
        tvPhotoCnt = findViewById(R.id.TV_plantroll_cnt);

        Intent intent = getIntent();

        try {
            plant = new Plant((PlantSave) intent.getSerializableExtra("plant"));
        } catch (Exception e) {
            Log.e("PlantActivity:ONCREATE", "ERROR reading plant data");
            finish();
        }


        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeRollPic();
            }
        });

        roll = makeRollArray();
        rclvPhoto.setLayoutManager(new GridLayoutManager(this, 2));
        rclvPhoto.setAdapter(new PlantRollAdapter(this, getSupportFragmentManager(), roll));

        tvPhotoCnt.setText(String.format(Locale.getDefault(), "%d Fotos", plant.getLogPicPaths().size()));

        btnShowAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnimDial();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            addRollPic();
        }
    }

    @Override
    public void onBackPressed() {
        Intent retIntent = new Intent();
        retIntent.putExtra("plantret", plant.toSave());
        setResult(RESULT_OK, retIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onRollPhotoRemoved(int idx) {
        File rem = new File(plant.getLogPicPaths().get(idx));
        if(rem.exists()) rem.delete();

        plant.getLogPicPaths().remove(idx);
        roll = makeRollArray();
        rclvPhoto.setAdapter(new PlantRollAdapter(this, getSupportFragmentManager(), roll));
        tvPhotoCnt.setText(String.format(Locale.getDefault(), "%d Fotos", plant.getLogPicPaths().size()));

    }
}