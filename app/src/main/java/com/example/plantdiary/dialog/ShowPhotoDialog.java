package com.example.plantdiary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.plantdiary.R;
import com.example.plantdiary.Util;
import com.example.plantdiary.cam.BitmapAndTimestamp;
import com.example.plantdiary.cam.PhotoAnim;

import java.util.ArrayList;

public class ShowPhotoDialog extends DialogFragment {

    public interface IVInitListener {
        void onIVInit(ImageView iv);
    }

    //----------------- VIEWS -------------------------------//

    ImageView ivPhoto;
    TextView tvTS;

    //------------------- VARS ---------------------------------//

    boolean use_param_inflater = false;
    LayoutInflater inflater;
    BitmapAndTimestamp bat;

    ArrayList<BitmapAndTimestamp> bats = new ArrayList<>();
    boolean do_anim = false;
    int idx = 0;

    IVInitListener iiListen;


    //---------------- CUSTOM FUNCS ---------------------------//



    //---------------- CONSTRUCT ------------------------------//

    public ShowPhotoDialog(BitmapAndTimestamp bat) {
        this.bat = bat;
    }

    public ShowPhotoDialog(ArrayList<BitmapAndTimestamp> bats, Context c) {
        this.bats = bats;
        do_anim = true;
        idx = 0;
        iiListen = (IVInitListener) c;
    }

    //--------------- OVERRIDES -----------------------------//
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

       if(!use_param_inflater) {
           inflater = requireActivity().getLayoutInflater();
       }

        View layout = inflater.inflate(R.layout.dlg_showphoto, null);

        builder.setView(layout);

        ivPhoto = layout.findViewById(R.id.IV_showphoto_photo);
        tvTS = layout.findViewById(R.id.TV_showphoto_ts);

        if(!do_anim) {
            ivPhoto.setImageBitmap(Util.RotateBitmap(bat.bm, 90));
            if(bat.ldt != null) tvTS.setText(Util.timestampToString(bat.ldt));
            else tvTS.setVisibility(View.INVISIBLE);

        } else {
            ivPhoto.setImageBitmap(Util.RotateBitmap(bats.get(0).bm, 90));
        }




        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if(do_anim) iiListen.onIVInit(ivPhoto);
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(do_anim) iiListen.onIVInit(ivPhoto);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
