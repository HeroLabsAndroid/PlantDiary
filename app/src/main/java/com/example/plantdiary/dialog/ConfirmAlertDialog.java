package com.example.plantdiary.dialog;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ConfirmAlertDialog {
    public static void showDeleteDialog(Context ctx, DialogInterface.OnClickListener accept, DialogInterface.OnClickListener deny) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        boolean rpl = false;
        builder.setPositiveButton("I do!", accept);
        builder.setNegativeButton("Nah fuck that", deny);

        builder.setTitle("Delete that shit?");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showDeleteDialog(Context ctx, DialogInterface.OnClickListener accept) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        boolean rpl = false;
        builder.setPositiveButton("I do!", accept);
        builder.setNegativeButton("Nah fuck that", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setTitle("Delete that shit?");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
