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

    //----------------- VIEWS -------------------------------//

    ImageView ivPhoto;
    TextView tvTS;

    //------------------- VARS ---------------------------------//

    BitmapAndTimestamp bat;



    //---------------- CUSTOM FUNCS ---------------------------//



    //---------------- CONSTRUCT ------------------------------//

    public ShowPhotoDialog(BitmapAndTimestamp bat) {
        this.bat = bat;
    }



    //--------------- OVERRIDES -----------------------------//
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());


        LayoutInflater inflater = requireActivity().getLayoutInflater();


        View layout = inflater.inflate(R.layout.dlg_showphoto, null);

        builder.setView(layout);

        ivPhoto = layout.findViewById(R.id.IV_showphoto_photo);
        tvTS = layout.findViewById(R.id.TV_showphoto_ts);

        ivPhoto.setImageBitmap(Util.RotateBitmap(bat.bm, 90));
        if(bat.ldt != null) tvTS.setText(Util.timestampToString(bat.ldt));
        else tvTS.setVisibility(View.INVISIBLE);


        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
