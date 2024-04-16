package com.example.plantdiary.datadapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantdiary.R;
import com.example.plantdiary.Util;
import com.example.plantdiary.cam.BitmapAndTimestamp;
import com.example.plantdiary.plantaction.PlantAction;
import com.example.plantdiary.plantaction.PlantEvent;
import com.example.plantdiary.plantaction.PlantLogItem;

import java.util.ArrayList;

public class PlantRollAdapter extends RecyclerView.Adapter<PlantRollAdapter.ViewHolder> {
    //------------------- INTERFACES ------------------------------//



    //---------------- VARS ----------------------------------------//

    ArrayList<BitmapAndTimestamp> photos = new ArrayList<>();

    Context ctx;

    //--------------- CUSTOM FUNCS --------------------------------//


    //---------------- OVERRIDES -----------------------------------//
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grditm_plantroll_photo, parent, false);

        return new PlantRollAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTvTS().setText(Util.timestampToString(photos.get(position).ldt));
        holder.getIvPhoto().setImageBitmap(photos.get(position).bm);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    //---------------- CONSTRUCTOR ---------------------------------------------//

    public PlantRollAdapter(Context c, ArrayList<BitmapAndTimestamp> photos) {
        this.photos = photos;
        ctx = c;
    }


    //-------------- VIEWHOLDER ----------------------------------------------//
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView getTvTS() {
            return tvTS;
        }

        public ImageView getIvPhoto() {
            return ivPhoto;
        }

        private final TextView tvTS;
        private final ImageView ivPhoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTS = itemView.findViewById(R.id.TV_grditm_plantroll_ts);
            ivPhoto = itemView.findViewById(R.id.IV_grditm_plantroll_photo);
        }
    }
}