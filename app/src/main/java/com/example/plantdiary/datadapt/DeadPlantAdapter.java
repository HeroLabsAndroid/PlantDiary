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
import com.example.plantdiary.io.DeadPlant;
import com.example.plantdiary.io.PlantDiaryIO;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

public class DeadPlantAdapter extends RecyclerView.Adapter<DeadPlantAdapter.ViewHolder> {
    //------------------- INTERFACES ------------------------------//

    public interface DeadPlantRemovedListener {
        void onDeadPlantRemoved();
    }

    //---------------- VARS ----------------------------------------//

    ArrayList<DeadPlant> localDataSet = new ArrayList<>();

    Context ctx;

    FragmentManager fragMan;

    DeadPlantRemovedListener dprListen;

    //--------------- CUSTOM FUNCS --------------------------------//

    void removeItem(ViewHolder holder) {
        localDataSet.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());

        PlantDiaryIO.saveDeadPlants(ctx, localDataSet);
        dprListen.onDeadPlantRemoved();
    }

    void showPhotoDialog(ViewHolder holder) {
        ShowPhotoDialog spDial = new ShowPhotoDialog(new BitmapAndTimestamp(localDataSet.get(holder.getAdapterPosition()).getImg(),
                localDataSet.get(holder.getAdapterPosition()).getOwnedSince().atStartOfDay()));
        spDial.show(fragMan, "logpicdial");
    }

    //---------------- OVERRIDES -----------------------------------//
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grditm_deadplant, parent, false);

        return new DeadPlantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DeadPlant dp = localDataSet.get(position);

        holder.getTvName().setText(dp.getName());

        holder.getTvType().setText(dp.getType());

        String str_birth = (dp.isPre_existing()) ? "?" : Util.dateToString(dp.getOwnedSince());
        String str_death = Util.dateToString(dp.getTimeOfDeath());
        holder.getTvTimeframe().setText(String.format(Locale.getDefault(),"* %s - ✝ %s (%d Tage)", str_birth, str_death, dp.getOwnedSince().until(dp.getTimeOfDeath(), ChronoUnit.DAYS)));

        holder.getTvDeathcause().setText(dp.getCauseOfDeath().toString());

        holder.getTvLifecycleStage().setText(dp.getLifeCycleStage().toString());

        holder.getIvPhoto().setImageBitmap(Util.RotateBitmap(dp.getImg(), 90));
        holder.getIvPhoto().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoDialog(holder);
            }
        });

        holder.getBtnDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmAlertDialog.showDeleteDialog(ctx, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(holder);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    //---------------- CONSTRUCTOR ---------------------------------------------//

    public DeadPlantAdapter(Context c, FragmentManager fragMan, ArrayList<DeadPlant> fallen_bros) {
        this.localDataSet = fallen_bros;
        ctx = c;
        dprListen = (DeadPlantRemovedListener) c;
        this.fragMan = fragMan;
    }


    //-------------- VIEWHOLDER ----------------------------------------------//
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView getTvName() {
            return tvName;
        }

        public TextView getTvType() {
            return tvType;
        }

        public TextView getTvTimeframe() {
            return tvTimeframe;
        }

        public TextView getTvDeathcause() {
            return tvDeathcause;
        }

        public TextView getTvLifecycleStage() {
            return tvLifecycleStage;
        }

        public ImageView getIvPhoto() {
            return ivPhoto;
        }

        public Button getBtnDelete() {return btnDelete;}

        private final TextView tvName, tvType, tvTimeframe, tvDeathcause, tvLifecycleStage;
        private final ImageView ivPhoto;

        private final Button btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.TV_grditm_deadplant_name);
            tvType = itemView.findViewById(R.id.TV_grditm_deadplant_type);
            tvTimeframe = itemView.findViewById(R.id.TV_grditm_deadplant_timeframe);
            tvDeathcause = itemView.findViewById(R.id.TV_grditm_deadplant_cod);
            tvLifecycleStage = itemView.findViewById(R.id.TV_grditm_deadplant_lifecyclestate);
            ivPhoto = itemView.findViewById(R.id.IV_grditm_deadplant_photo);
            btnDelete = itemView.findViewById(R.id.BTN_grditm_deadplant_yeet);
        }
    }
}
