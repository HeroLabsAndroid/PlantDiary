package com.example.plantdiary.cam;

import static android.provider.Settings.System.getString;
import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.plantdiary.MainActivity;
import com.example.plantdiary.R;
import com.example.plantdiary.plant.Plant;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;




public class CamOps {
//    public static String makeUniquePlantImagePath() {
////        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
////        String imageFileName = "JPEG_" + timeStamp + "_";
////        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
////        File image = File.createTempFile(
////                imageFileName,  /* prefix */
////                ".jpg",         /* suffix */
////                storageDir      /* directory */
////        );
////
////        // Save a file: path for use with ACTION_VIEW intents
////        currentPhotoPath = image.getAbsolutePath();
////        return image;
//
//    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String mkPlantProfilePicPath(Context c, Plant p) throws IOException {
        String imageFileName = "ProfPic_" + p.getName() + "_" +p.getPlanttype() + "_";
        File storageDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image.getAbsolutePath();
    }

    public static BitmapAndPath dispatchPictureIntent(Context c, Plant p) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(c.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = new File(mkPlantProfilePicPath(c, p));

            } catch (IOException ioe) {
                Log.e("dispatchPictureIntent", "Error making filepath");
            }

            if(photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(c, "com.example.plantdiary", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile);
                startActivityForResult((MainActivity) c, takePictureIntent, REQUEST_IMAGE_CAPTURE, null);

                return new BitmapAndPath((Bitmap) takePictureIntent.getExtras().get("data"), photoFile.getAbsolutePath());
            }
        }

        return new BitmapAndPath(null, c.getString(R.string.FLAG_err));
    }
}
