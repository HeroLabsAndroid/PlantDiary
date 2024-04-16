package com.example.plantdiary;

import static com.example.plantdiary.cam.CamOps.REQUEST_IMAGE_CAPTURE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.plantdiary.cam.BitmapAndPath;
import com.example.plantdiary.cam.BitmapAndTimestamp;
import com.example.plantdiary.cam.CamOps;
import com.example.plantdiary.cam.PathAndTimestamp;
import com.example.plantdiary.datadapt.PlantRollAdapter;
import com.example.plantdiary.io.PlantSave;
import com.example.plantdiary.plant.Plant;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Locale;

public class PlantRollActivity extends AppCompatActivity {
    //------------------- INTERFACES --------------------------//

    public interface PlantRollListener {
        void onPhotoAdded(Plant p);
        void onPhotoRemoved(int pos);
    }

    //------------------ VARS ---------------------------------//
    Button btnAddPhoto;
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
                out.add(new BitmapAndTimestamp(BitmapFactory.decodeFile(plant.getLogPicPaths().get(i)), plant.getLogPicTS().get(i)));
            }


        }
        return out;
    }

    String takeRollPic() {
        BitmapAndPath bmpath = CamOps.dispatchPictureIntent(this, plant, false);
        if(!(bmpath.path.compareTo(getString(R.string.FLAG_err))==0)) {
            plant.getLogPicPaths().add(bmpath.path);
            plant.getLogPicTS().add(LocalDateTime.now());

        }

        return bmpath.path;
    }

    void addRollPic() {
        roll = makeRollArray();
        //rclvPhoto.getAdapter().notifyItemInserted(roll.size());
        //rclvPhoto.getAdapter().notifyItemRangeChanged(roll.size(), 1);
        rclvPhoto.setAdapter(new PlantRollAdapter(this, roll));
        tvPhotoCnt.setText(String.format(Locale.getDefault(), "%d Fotos", plant.getLogPicPaths().size()));

    }

    //------------------ OVERRIDES ---------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_roll);

        btnAddPhoto = findViewById(R.id.BTN_plantroll_addphoto);
        rclvPhoto = findViewById(R.id.RCLV_plantroll_photos);
        tvPhotoCnt = findViewById(R.id.TV_plantroll_photocnt);

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
        rclvPhoto.setAdapter(new PlantRollAdapter(this, roll));

        tvPhotoCnt.setText(String.format(Locale.getDefault(), "%d Fotos", plant.getLogPicPaths().size()));
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
}