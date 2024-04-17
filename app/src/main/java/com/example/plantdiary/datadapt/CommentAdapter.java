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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantdiary.R;
import com.example.plantdiary.Util;
import com.example.plantdiary.cam.BitmapAndTimestamp;
import com.example.plantdiary.dialog.ConfirmAlertDialog;
import com.example.plantdiary.plantaction.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    //------------------- INTERFACES ------------------------------//

    public interface CommentRemovedListener {
        void onCommentRemoved(int idx);
    }


    //---------------- VARS ----------------------------------------//

    ArrayList<Comment> comments = new ArrayList<>();

    Context ctx;

    CommentRemovedListener crListen;

    //--------------- CUSTOM FUNCS --------------------------------//

    public void removeItem(ViewHolder holder) {
        int pos = holder.getAdapterPosition();
        comments.remove(pos);
        notifyItemRemoved(pos);
    }

    //---------------- OVERRIDES -----------------------------------//
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lstitm_comment, parent, false);

        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getTvTS().setText(Util.timestampToString(comments.get(position).ldt));
        holder.getTvText().setText(comments.get(position).comment);

        holder.getBtnRemove().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                boolean rpl = false;
                builder.setPositiveButton("I do!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(holder);
                    }
                });
                builder.setNegativeButton("Nah fuck that", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
        return comments.size();
    }

    //---------------- CONSTRUCTOR ---------------------------------------------//

    public CommentAdapter(Context c, ArrayList<Comment> comments) {
        this.comments = comments;
        ctx = c;
        crListen = (CommentRemovedListener) c;
    }


    //-------------- VIEWHOLDER ----------------------------------------------//
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView getTvTS() {
            return tvTS;
        }

        public TextView getTvText() {
            return tvText;
        }

        public Button getBtnRemove() {return btnRemove;}

        private final TextView tvTS, tvText;
        private final Button btnRemove;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTS = itemView.findViewById(R.id.TV_lstitm_comment_ts);
            tvText = itemView.findViewById(R.id.TV_lstitm_comment_text);
            btnRemove = itemView.findViewById(R.id.BTN_lstitm_comment_delete);
        }
    }
}
