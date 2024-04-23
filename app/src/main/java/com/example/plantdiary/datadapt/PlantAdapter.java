package com.example.plantdiary.datadapt;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantdiary.R;
import com.example.plantdiary.Util;
import com.example.plantdiary.dialog.AttachCommentDialog;
import com.example.plantdiary.dialog.CauseOfDeathDialog;
import com.example.plantdiary.plant.Plant;
import com.example.plantdiary.plant.PlantGridData;
import com.example.plantdiary.plantaction.Comment;
import com.example.plantdiary.plantaction.PlantActionType;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder>{



    //--------------- INTERFACES ---------------------------------//

    public interface PlantRemovedListener {
        public void onPlantRemoved(int plantidx);
    }

    public interface PlantActionListener {
        void onPlantWatered(int plantidx);
        void onPlantFertilized(int plantidx);
        void onPlantCommented(int plantidx);
    }

    public interface PlantEditDialogLauncher {
        void startPlantEditDialog(int plantidx);
        void startPlantActivity(int plantidx);

        void startAttachCommentDialog(int plantidx);
    }

    //---------------- VARS ------------------------------------//

    ArrayList<PlantGridData> localDataSet;
    Context ctx;

    PlantActionListener paListen;
    PlantRemovedListener prListen;
    PlantEditDialogLauncher pedLaunch;

    PlantActionType selectedAction = PlantActionType.NONE;

    //--------------- GETTER AND SETTER -------------------------//

    public PlantActionType getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(PlantActionType selectedAction) {
        this.selectedAction = selectedAction;
    }


    //--------------- CONSTRUCTORS -----------------------------//

    public PlantAdapter(Context ctx, ArrayList<PlantGridData> lds) {
        this.ctx = ctx;
        localDataSet = lds;
        prListen = (PlantRemovedListener) ctx;
        pedLaunch = (PlantEditDialogLauncher) ctx;
        paListen = (PlantActionListener) ctx;
    }

    //--------------- CUSTOM FUNCS -----------------------------//



    public void removeItem(ViewHolder holder) {
        int pos = holder.getAdapterPosition();

        prListen.onPlantRemoved(pos);
        //localDataSet.remove(pos);
        //notifyItemRemoved(pos);
    }

    public void launchEditPlantDialog(ViewHolder holder) {
        pedLaunch.startPlantEditDialog(holder.getAdapterPosition());
    }

    public void launchPlantActivity(ViewHolder holder) {
        pedLaunch.startPlantActivity(holder.getAdapterPosition());
    }

    public void launchAttachCommentDialog(ViewHolder holder) {
        pedLaunch.startAttachCommentDialog(holder.getAdapterPosition());
    }

    public void setBitmap(ViewHolder holder) {
        Bitmap bm = Util.RotateBitmap(localDataSet.get(holder.getAdapterPosition()).photo, 90);
        int height = (int)ctx.getResources().getDimension(R.dimen.MAINGRID_PIC_HEIGHT);
        bm = Util.scaleBitmap(bm, (int)((float)(bm.getWidth()/(float)bm.getHeight())*height), height);
        holder.getIvProfPic().setImageBitmap(bm);
        holder.getIvProfPic().setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    public void onImageClicked(ViewHolder holder) {
        switch (selectedAction) {
            case NONE:
                launchPlantActivity(holder);
                break;
            case WATER:
                paListen.onPlantWatered(holder.getAdapterPosition());
                break;
            case FERTILIZE:
                paListen.onPlantFertilized(holder.getAdapterPosition());
                break;
            case PLANTCOMMENT:
                launchAttachCommentDialog(holder);
        }
    }


    //--------------- OVERRIDES ------------------------------//

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grditm_plant, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTvName().setText(localDataSet.get(position).name);
        holder.getTvType().setText(localDataSet.get(position).type);

        holder.getFabDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeItem(holder);
            }
        });

        holder.getFabEdit().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchEditPlantDialog(holder);
            }
        });


        holder.getIvProfPic().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClicked(holder);
            }
        });

        if(localDataSet.get(position).photo != null)  {
            setBitmap(holder);
        }


    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }





    //----------------- VIEWHOLDER ---------------------------------//

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final private TextView tvName, tvType;

        final private ImageView ivProfPic;
        final private FloatingActionButton fabDelete, fabEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.TV_grditm_plant_name);
            tvType = itemView.findViewById(R.id.TV_grditm_plant_type);
            fabDelete = itemView.findViewById(R.id.FAB_grditm_plant_delete);
            fabEdit = itemView.findViewById(R.id.FAB_grditm_plant_edit);
            ivProfPic = itemView.findViewById(R.id.IV_grditm_plant_profpic);
        }

        public TextView getTvName() {
            return tvName;
        }

        public FloatingActionButton getFabDelete() {
            return fabDelete;
        }
        public FloatingActionButton getFabEdit() {
            return fabEdit;
        }

        public TextView getTvType() {
            return tvType;
        }

        public ImageView getIvProfPic() {return ivProfPic;}
    }
}
