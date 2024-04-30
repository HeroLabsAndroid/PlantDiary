package com.example.plantdiary.datadapt;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantdiary.R;
import com.example.plantdiary.Util;
import com.example.plantdiary.cam.BitmapAndTimestamp;
import com.example.plantdiary.dialog.ConfirmAlertDialog;
import com.example.plantdiary.dialog.ShowPhotoDialog;

import java.util.ArrayList;

public class PlantRollAdapter extends RecyclerView.Adapter<PlantRollAdapter.ViewHolder> {
    //------------------- INTERFACES ------------------------------//

    public interface RollPhotoRemovedListener {
        void onRollPhotoRemoved(int idx);
    }

    //---------------- VARS ----------------------------------------//

    ArrayList<BitmapAndTimestamp> photos = new ArrayList<>();

    Context ctx;

    FragmentManager fragMan;

    RollPhotoRemovedListener rprListen;

    //--------------- CUSTOM FUNCS --------------------------------//

    void showPhotoDialog(ViewHolder holder) {
        ShowPhotoDialog spDial = new ShowPhotoDialog(photos.get(holder.getAdapterPosition()));
        spDial.show(fragMan, "logpicdial");
    }

    void removePhoto(ViewHolder holder) {
        rprListen.onRollPhotoRemoved(holder.getAdapterPosition());
    }

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
        holder.getIvPhoto().setImageBitmap(Util.RotateBitmap(photos.get(position).bm, 90));
        holder.getIvPhoto().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoDialog(holder);
            }
        });

        holder.getButtDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmAlertDialog.showDeleteDialog(ctx, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removePhoto(holder);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    //---------------- CONSTRUCTOR ---------------------------------------------//

    public PlantRollAdapter(Context c, FragmentManager fragMan, ArrayList<BitmapAndTimestamp> photos) {
        this.photos = photos;
        ctx = c;
        this.fragMan = fragMan;
        rprListen = (RollPhotoRemovedListener) ctx;
    }


    //-------------- VIEWHOLDER ----------------------------------------------//
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView getTvTS() {
            return tvTS;
        }

        public ImageView getIvPhoto() {
            return ivPhoto;
        }

        public Button getButtDelete() {
            return buttDelete;
        }

        private final TextView tvTS;
        private final ImageView ivPhoto;

        private final Button buttDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTS = itemView.findViewById(R.id.TV_grditm_plantroll_name);
            ivPhoto = itemView.findViewById(R.id.IV_grditm_plantroll_photo);
            buttDelete = itemView.findViewById(R.id.BTN_grditm_plantroll_delete);
        }
    }
}
