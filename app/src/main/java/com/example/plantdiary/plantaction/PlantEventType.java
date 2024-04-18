package com.example.plantdiary.plantaction;

import androidx.annotation.NonNull;

public enum PlantEventType {
    REPOT, RELOCATE, RENAME, NEWCOMMENT, DIED, GROWN;
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
            case GROWN:
                return "Gewachsen";
            default:
                return "Gestorben";
        }
    }
}
