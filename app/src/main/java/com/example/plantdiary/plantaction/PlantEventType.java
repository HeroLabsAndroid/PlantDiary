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

    public static PlantEventType fromOrdinal(int o) {
        switch (o) {
            case 0:
                return REPOT;
            case 1:
                return RELOCATE;
            case 2:
                return RENAME;
            case 3:
                return NEWCOMMENT;
            case 4:
                return DIED;
            default:
                return GROWN;
        }
    }
}
