package com.example.plantdiary.datadapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantdiary.R;
import com.example.plantdiary.Util;
import com.example.plantdiary.plantaction.PlantAction;
import com.example.plantdiary.plantaction.PlantActionType;
import com.example.plantdiary.plantaction.PlantEvent;
import com.example.plantdiary.plantaction.PlantLogItem;

import java.util.ArrayList;

public class PlantLogAdapter extends RecyclerView.Adapter<PlantLogAdapter.ViewHolder> {
    //------------------- INTERFACES ------------------------------//

    public interface ItemRemovedListener {
        public void onItemRemoved(int pos);
    }


    //---------------- VARS ----------------------------------------//

    ArrayList<PlantLogItem> log;
    ItemRemovedListener irListen;

    Context ctx;

    //--------------- CUSTOM FUNCS --------------------------------//

    public void removeItem(ViewHolder holder) {
        log.remove(holder.getAdapterPosition());
        notifyItemRemoved(holder.getAdapterPosition());
        irListen.onItemRemoved(holder.getAdapterPosition());
    }

    //---------------- OVERRIDES -----------------------------------//
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lstitm_plantlog, parent, false);

        return new PlantLogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTvTit().setText(log.get(position).getTyp() == PlantLogItem.ItemType.ACTION ?
                        ((PlantAction)log.get(position)).getActTyp().toString() :
                ((PlantEvent) log.get(position)).getPet().toString());
        holder.getTvType().setText(log.get(position).getTyp() == PlantLogItem.ItemType.ACTION ? "ACTION" : "EVENT");
        holder.getTvTS().setText(Util.timestampToString(log.get(position).getTimestamp()));

        holder.getBtnDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return log.size();
    }

    //---------------- CONSTRUCTOR ---------------------------------------------//

    public PlantLogAdapter(Context c, ArrayList<PlantLogItem> log) {
        this.log = log;
        ctx = c;
        irListen = (ItemRemovedListener) c;
    }


    //-------------- VIEWHOLDER ----------------------------------------------//
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView getTvType() {
            return tvType;
        }

        public TextView getTvTit() {
            return tvTit;
        }

        public TextView getTvTS() {
            return tvTS;
        }

        public Button getBtnDelete() {
            return btnDelete;
        }

        private final TextView tvType, tvTit, tvTS;

        private final Button btnDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvType = itemView.findViewById(R.id.TV_lstitm_plantlog_actev);
            tvTit = itemView.findViewById(R.id.TV_listitm_plantlog_type);
            tvTS = itemView.findViewById(R.id.TV_lstitm_plantlog_timestamp);
            btnDelete = itemView.findViewById(R.id.BTN_listitm_plant_delete);
        }
    }
}
