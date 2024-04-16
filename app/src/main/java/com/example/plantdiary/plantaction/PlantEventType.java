package com.example.plantdiary.plantaction;

import androidx.annotation.NonNull;

public enum PlantEventType {
    REPOT, RELOCATE, RENAME, NEWCOMMENT, DIED;
    @NonNull
    @Override
    public String toString() {
        switch(this) {
            case REPOT:
                return "Umgetopft";
            case RENAME:
                return "Umbenannt";
            case RELOCATE:
                return "Umgezogen";
            case NEWCOMMENT:
                return "Neuer Kommentar";
            default:
                return "Gestorben";
        }
    }
}
