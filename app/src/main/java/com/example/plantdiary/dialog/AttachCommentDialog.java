package com.example.plantdiary.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.plantdiary.R;
import com.example.plantdiary.Util;
import com.example.plantdiary.plantaction.Comment;

import java.time.LocalDateTime;

public class AttachCommentDialog extends DialogFragment {
    public interface AttachCommentListener {
        void attachComment(Comment cmt, int idx);
    }

    EditText commentET;
    Button addCommentBtn;

    AttachCommentListener acListen;
    int plantidx;



    void confirmComment() {
        if(commentET.getText().toString().compareTo(getString(R.string.PROMT_defineplant_comment))!=0) {
            Comment cmt = new Comment(commentET.getText().toString(), LocalDateTime.now());
            acListen.attachComment(cmt, plantidx);
        }
        dismiss();

    }

    public AttachCommentDialog(AttachCommentListener acListen, int plantidx) {
        this.acListen = acListen;
        this.plantidx = plantidx;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dlg_attachcomment, null);

        builder.setView(layout);

        commentET = layout.findViewById(R.id.ET_dlg_attachcomment);
        addCommentBtn = layout.findViewById(R.id.BTN_dlg_attachcomment);

        commentET.setText(getString(R.string.PROMT_defineplant_comment));

        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmComment();
            }
        });


        return builder.create();
    }
}
