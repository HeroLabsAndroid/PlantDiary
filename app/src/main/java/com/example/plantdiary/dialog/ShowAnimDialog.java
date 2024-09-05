package com.example.plantdiary.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
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

public class ShowAnimDialog extends DialogFragment{


    //----------------- VIEWS -------------------------------//

    ImageView ivPhoto;
    Button btnStart;

    TextView tvProgress;

    //------------------- VARS ---------------------------------//

    boolean no_photos = false;
    boolean anim_running = false;
    final double SECONDS_PER_DAY = 1;
    final double SECONDS_PER_MINUTE = SECONDS_PER_DAY/(24.0*60.0);
    ArrayList<BitmapAndTimestamp> pics;
    int idx = 0;
    Context ctx;
    Activity act;

    //---------------- CUSTOM FUNCS ---------------------------//



    void animate() {
        if(!anim_running && !pics.isEmpty()) {
            idx = 0;
            ivPhoto.setImageBitmap(pics.get(0).bm);
            anim_running = true;
            btnStart.setBackgroundColor(getContext().getColor(R.color.DRK_GRAYOUT));

            resetAnim();
            anim.start();
        }
    }

    //---------------- CONSTRUCT ------------------------------//


    public ShowAnimDialog(Activity act, ArrayList<BitmapAndTimestamp> bats, Context c) {
        this.pics = bats;
        idx = 0;
        ctx = c;
        this.act = act;
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

        tvProgress.setText(String.format(Locale.getDefault(), "%d/%d", 1, pics.size()));


        ivPhoto.setImageBitmap(pics.get(0).bm);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animate();
            }
        });


        return builder.create();
    }

    Thread anim;

    void resetAnim() {
       anim = new Thread(new Runnable() {
           @Override
           public void run() {
               if(!no_photos) {
                   if(idx < pics.size()-1) {

                       double runtime = (idx < pics.size()-1) ? (int)((float)pics.get(idx).ldt.until(pics.get(idx+1).ldt, ChronoUnit.MINUTES)*SECONDS_PER_MINUTE) : -1;

                       if(runtime > 0) {
                           try {
                               Thread.sleep((long)(runtime*1000));

                               //updatePhoto();
                           } catch (InterruptedException e) {
                               throw new RuntimeException(e);
                           }
                       }

                       Animation fadeIn = getAnimation();

                       tvProgress.post(()->tvProgress.startAnimation(fadeIn));
                       //TODO: Add fade animations
                       tvProgress.post(() -> tvProgress.setText(String.format(Locale.getDefault(), "%d/%d", idx+1, pics.size())));
                       ivPhoto.post(() -> ivPhoto.setImageBitmap(pics.get(idx).bm));
                       idx++;


                       // ivPhoto.refreshDrawableState();

                       //tvProgress.refreshDrawableState();
                   } else {
                       Animation fadeIn = getAnimation();

                       tvProgress.post(()->tvProgress.startAnimation(fadeIn));
                       ivPhoto.post(() -> ivPhoto.setImageBitmap(pics.get(idx).bm));
                       tvProgress.post(() -> tvProgress.setText(String.format(Locale.getDefault(), "%d/%d", idx+1, pics.size())));
                       anim_running = false;
                       btnStart.setBackgroundColor(getContext().getColor(R.color.DRK_ACC6));
                   }

               }
           }
       });
    }

    @NonNull
    private Animation getAnimation() {
        Animation fadeIn = new AlphaAnimation(0,1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(250);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                onWaitEnd();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return fadeIn;
    }


    private void onWaitEnd() {
        if(anim_running) {
            if(anim.isAlive()) anim.interrupt();
            resetAnim();
            anim.start();
        };
    }
}
