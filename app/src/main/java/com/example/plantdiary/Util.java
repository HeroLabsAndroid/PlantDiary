package com.example.plantdiary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

public class Util {
    public final static int ACTCODE_PLANT = 2;
    public static final int ACTCODE_PLANTROLL = 4;
    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap scaleBitmap(Bitmap source, float scale)
    {
        return Bitmap.createBitmap(source, 0, 0, (int)(source.getWidth()*scale), (int)(source.getHeight()*scale));
    }

    public static Bitmap scaleBitmap(Bitmap source, int w, int h)
    {
        Matrix matrix = new Matrix();
        matrix.postScale(w/(float)source.getWidth(), h/(float)source.getHeight());
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }


    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static String timestampToString(LocalDateTime ldt) {
        return String.format(Locale.getDefault(), "%d.%d.%d\r\n%d:%d:%d", ldt.getDayOfMonth(), ldt.getMonth().getValue(), ldt.getYear(), ldt.getHour(), ldt.getMinute(), ldt.getSecond());
    }

    public static String dateToString(LocalDate ldt) {
        return String.format(Locale.getDefault(), "%d.%d.%d", ldt.getDayOfMonth(), ldt.getMonth().getValue(), ldt.getYear());
    }
}
