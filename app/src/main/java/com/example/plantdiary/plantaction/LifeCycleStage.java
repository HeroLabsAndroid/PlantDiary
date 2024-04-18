package com.example.plantdiary.plantaction;

import androidx.annotation.NonNull;

import java.io.Serializable;

public enum LifeCycleStage implements Serializable {
    SEED, SAPLING, GROWN, DIED, INVAL;

    @NonNull
    public String toString() {
        switch (this) {
            case SEED:
                return "Samen :)";
            case SAPLING:
                return "Setzling";
            case GROWN:
                return "Ausgewachsen";
            case DIED:
                return "Gestorben :(";
            default:
                return "INVAL";
        }


    }

    public static String[] getStages() {
        String[] stg = new String[values().length-2];
        for (int i=0; i< values().length-2; i++) {
            stg[i] = values()[i].toString();
        }

        return stg;
    }

    public static LifeCycleStage fromOrdinal(int ord) {
        switch (ord) {
            case 0:
                return SEED;
            case 1:
                return SAPLING;
            case 2:
                return GROWN;
            case 3:
                return DIED;
            default:
                return INVAL;
        }
    }
}
