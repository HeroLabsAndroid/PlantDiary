package com.example.plantdiary.plantaction;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Comment implements Serializable {
    public String comment = "";
    public LocalDateTime ldt;

    public boolean pinned = false;

    public Comment(String cmt, LocalDateTime ld) {
        comment = cmt;
        ldt = ld;
    }

    public Comment(String cmt, LocalDateTime ld, boolean pin) {
        comment = cmt;
        ldt = ld;
        pinned = pin;
    }
}
