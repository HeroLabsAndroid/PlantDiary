package com.example.plantdiary.cam;

import android.graphics.Bitmap;

import java.time.LocalDateTime;

public class PathAndTimestamp {
    public LocalDateTime ldt;
    public String path;

    public PathAndTimestamp(LocalDateTime ldt, String path) {
        this.ldt = ldt;
        this.path = path;
    }
}
