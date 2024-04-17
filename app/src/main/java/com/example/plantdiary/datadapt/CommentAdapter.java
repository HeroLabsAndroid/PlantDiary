package com.example.plantdiary.datadapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plantdiary.R;
import com.example.plantdiary.Util;
import com.example.plantdiary.cam.BitmapAndTimestamp;
import com.example.plantdiary.plantaction.Comment;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    //------------------- INTERFACES ------------------------------//



    //---------------- VARS ----------------------------------------//

    ArrayList<Comment> comments = new ArrayList<>();

    Context ctx;

    //--------------- CUSTOM FUNCS --------------------------------//


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
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    //---------------- CONSTRUCTOR ---------------------------------------------//

    public CommentAdapter(Context c, ArrayList<Comment> comments) {
        this.comments = comments;
        ctx = c;
    }


    //-------------- VIEWHOLDER ----------------------------------------------//
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView getTvTS() {
            return tvTS;
        }

        public TextView getTvText() {
            return tvText;
        }

        private final TextView tvTS, tvText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTS = itemView.findViewById(R.id.TV_lstitm_comment_ts);
            tvText = itemView.findViewById(R.id.TV_lstitm_comment_text);
        }
    }
}
