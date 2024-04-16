package com.example.plantdiary.cam;

import static android.provider.Settings.System.getString;
import static androidx.core.app.ActivityCompat.startActivityForResult;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.plantdiary.MainActivity;
import com.example.plantdiary.PlantRollActivity;
import com.example.plantdiary.R;
import com.example.plantdiary.Util;
import com.example.plantdiary.plant.Plant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
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

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String mkPlantProfilePicPath(Context c, Plant p) throws IOException {
        String imageFileName = "ProfPic_" + p.getName() + "_" +p.getPlanttype() + "_";
        File storageDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image.getAbsolutePath();
    }

    private static String mkPlantLogPicPath(Context c, Plant p) throws IOException {
        String imageFileName = "LogPic_" + p.getName() +"_" + String.valueOf(p.getLogPicPaths().size());
        File storageDir = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpeg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image.getAbsolutePath();
    }

    public static BitmapAndPath replaceWithDownscaled(Context c, String path) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        int h = (bm.getWidth() < bm.getHeight()) ? 1024 : (int)((float)(bm.getHeight())/(float)(bm.getWidth())*1024);
        int w = (bm.getWidth() > bm.getHeight()) ? 1024 : (int)((float)(bm.getWidth())/(float)(bm.getHeight())*1024);
        bm = Util.scaleBitmap(bm, w, h);

        try {
            OutputStream fOut = null;
            File file = new File(path); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            fOut = Files.newOutputStream(file.toPath());

            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

            return new BitmapAndPath(bm, path);
        } catch(Exception e) {
            Log.e("DOWNSCALEPIC", e.getMessage());
            return new BitmapAndPath(null, c.getString(R.string.FLAG_err));
        }

    }

    public static BitmapAndPath dispatchPictureIntent(Context c, Plant p, boolean profpic) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(c.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = new File(profpic ? mkPlantProfilePicPath(c, p) : mkPlantLogPicPath(c, p));

            } catch (IOException ioe) {
                Log.e("dispatchPictureIntent", "Error making filepath");
            }

            if(photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(c, "com.example.plantdiary", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                if(profpic) startActivityForResult((MainActivity) c, takePictureIntent, REQUEST_IMAGE_CAPTURE, null);
                else startActivityForResult((PlantRollActivity)c, takePictureIntent, REQUEST_IMAGE_CAPTURE,null);

                return new BitmapAndPath((Bitmap)takePictureIntent.getExtras().get("data"), photoFile.getAbsolutePath());//replaceWithDownscaled(c, photoFile.getAbsolutePath());
            }
        }

        return new BitmapAndPath(null, c.getString(R.string.FLAG_err));
    }
}
