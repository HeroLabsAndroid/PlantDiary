package com.example.plantdiary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.plantdiary.R;
import com.example.plantdiary.Util;
import com.example.plantdiary.cam.BitmapAndTimestamp;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

public class ShowAnimDialog extends DialogFragment implements Runnable{


    //----------------- VIEWS -------------------------------//

    ImageView ivPhoto;
    Button btnStart;

    TextView tvProgress;

    //------------------- VARS ---------------------------------//

    boolean no_photos = false;
    boolean anim_running = false;
    final long SECONDS_PER_DAY = 10;
    final double SECONDS_PER_HOUR = SECONDS_PER_DAY/24.0;
    ArrayList<BitmapAndTimestamp> pics;
    int idx = 0;
    Context ctx;



    //---------------- CUSTOM FUNCS ---------------------------//

    long updatePhoto() {
        long runtime = (idx < pics.size()-1) ? (int)(pics.get(idx).ldt.until(pics.get(idx+1).ldt, ChronoUnit.HOURS)*SECONDS_PER_HOUR) : -1;
        ivPhoto.setImageBitmap(Util.RotateBitmap(pics.get(idx).bm, 90));
        idx++;

        tvProgress.setText(String.format(Locale.getDefault(), "%d/%d", idx, pics.size()));

        return runtime;
    }

    //---------------- CONSTRUCT ------------------------------//


    public ShowAnimDialog(ArrayList<BitmapAndTimestamp> bats, Context c) {
        this.pics = bats;
        idx = 0;
        ctx = c;
    }

    //--------------- OVERRIDES -----------------------------//
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());


        LayoutInflater inflater = requireActivity().getLayoutInflater();


        View layout = inflater.inflate(R.layout.dlg_showanim, null);

        builder.setView(layout);

        ivPhoto = layout.findViewById(R.id.IV_showanim_photo);
        btnStart = layout.findViewById(R.id.BTN_showanim_start);
        tvProgress = layout.findViewById(R.id.TV_showanim_progress);

        tvProgress.setText(String.format(Locale.getDefault(), "%d/%d", 0, pics.size()));


        ivPhoto.setImageBitmap(Util.RotateBitmap(pics.get(0).bm, 90));

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!anim_running) {
                    anim_running = true;
                    btnStart.setBackgroundColor(getContext().getColor(R.color.DRK_GRAYOUT));

                    run();
                }

            }
        });


        return builder.create();
    }

    @Override
    public void run() {
        if(!no_photos) {
            if(idx < pics.size()) {
                long rt = updatePhoto();
                if(rt > 0) {
                    try {
                        Thread.sleep(rt*SECONDS_PER_DAY);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                run();
            } else {
                anim_running = false;
                btnStart.setBackgroundColor(getContext().getColor(R.color.DRK_ACC6));
            }
        }
    }
}
