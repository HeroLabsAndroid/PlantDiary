package com.example.plantdiary.datadapt;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantdiary.R;
import com.example.plantdiary.plant.Plant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder> {

    public interface PlantRemovedListener {
        public void onPlantRemoved();
    }

    ArrayList<Plant> localDataSet;
    Context ctx;

    PlantRemovedListener prListen;

    public PlantAdapter(Context ctx, ArrayList<Plant> lds) {
        this.ctx = ctx;
        localDataSet = lds;
        prListen = (PlantRemovedListener) ctx;
    }

    public void removeItem(ViewHolder holder) {
        int pos = holder.getAdapterPosition();
        localDataSet.remove(pos);
        notifyItemRemoved(pos);
        prListen.onPlantRemoved();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grditm_plant, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTvName().setText(localDataSet.get(position).getName());
        holder.getTvType().setText(localDataSet.get(position).getPlanttype());

        holder.getFabDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                boolean rpl = false;
                builder.setPositiveButton("I do!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeItem(holder);
                    }
                });
                builder.setNegativeButton("Nah fuck that", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancels the dialog.
                    }
                });

                builder.setTitle("Delete that shit?");

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final private TextView tvName, tvType;
        final private FloatingActionButton fabDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.TV_grditm_plant_name);
            tvType = itemView.findViewById(R.id.TV_grditm_plant_type);
            fabDelete = itemView.findViewById(R.id.FAB_grditm_plant_delete);
        }

        public TextView getTvName() {
            return tvName;
        }

        public FloatingActionButton getFabDelete() {
            return fabDelete;
        }

        public TextView getTvType() {
            return tvType;
        }
    }
}
