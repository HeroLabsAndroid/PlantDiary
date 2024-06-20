package com.example.plantdiary.plantaction;

import com.example.plantdiary.LDTsave;

import org.json.JSONException;
import org.json.JSONObject;

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

    public Comment(JSONObject jsave) throws JSONException {
        this.pinned = jsave.getBoolean("pin");
        this.comment = jsave.getString("comment");
        this.ldt = new LDTsave(jsave.getJSONObject("ts")).toLDT();
    }

    public JSONObject toJSONSave() throws JSONException {
        JSONObject jcommentsave = new JSONObject();
        jcommentsave.put("pin", pinned);
        jcommentsave.put("comment", comment);
        jcommentsave.put("ts", new LDTsave(ldt).toJSONSave());

        return jcommentsave;
    }
}
