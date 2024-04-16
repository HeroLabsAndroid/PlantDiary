package com.example.plantdiary.cam;

import android.graphics.Bitmap;

import java.time.LocalDateTime;

public class BitmapAndTimestamp {
    public Bitmap bm;
    public LocalDateTime ldt;

    public BitmapAndTimestamp(Bitmap bm, LocalDateTime ldt) {
        this.bm = bm;
        this.ldt = ldt;
    }
}
