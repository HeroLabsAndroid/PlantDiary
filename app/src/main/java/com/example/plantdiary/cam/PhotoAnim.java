package com.example.plantdiary.cam;

import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;

import com.example.plantdiary.datadapt.PlantRollAdapter;
import com.example.plantdiary.dialog.ShowPhotoDialog;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class PhotoAnim implements Runnable {




    boolean no_photos = false;
    final int SECONDS_PER_DAY = 60;
    final double SECONDS_PER_HOUR = SECONDS_PER_DAY/24.0;
    ArrayList<BitmapAndTimestamp> pics;
    int idx;

    ImageView iv;




    long updatePhoto() {
        long runtime = (idx < pics.size()-1) ? (int)(pics.get(idx).ldt.until(pics.get(idx+1).ldt, ChronoUnit.HOURS)*SECONDS_PER_HOUR) : -1;
        iv.setImageBitmap(pics.get(idx).bm);
        idx++;

        return runtime;
    }

    public PhotoAnim(ImageView iv, ArrayList<BitmapAndTimestamp> pics, int idx) {
        this.pics = pics;
        this.idx = idx;
        this.iv = iv;
    }



    @Override
    public void run() {


        if(!no_photos) {
            if(idx < pics.size()) {
                /*long rt = */updatePhoto();
                //if(rt > 0) {
                    try {
                        Thread.sleep(/*rt*/10L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                //}
                run();
            }
        }

    }

}
