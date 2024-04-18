package com.example.plantdiary.plantaction;

import java.io.Serializable;

public enum CauseOfDeath implements Serializable {
    DROWNED, EATEN, PARTITIONED, SICKNESS, TRASHED, NATURAL, COLD, NONE;

    public String toString() {
        switch (this) {
            case DROWNED:
                return "Ersoffen";
            case EATEN:
                return "Verspaist";
            case PARTITIONED:
                return "Aufgeteilt";
            case SICKNESS:
                return "Crank :(";
            case TRASHED:
                return "Weggehauen";
            case NATURAL:
                return "Altersschwäche";
            case COLD:
                return "Erfroren";
            default:
                return "INVAL";
        }


    }

    public static String[] getCauses() {
        String[] stg = new String[values().length-1];
        for (int i=0; i< values().length-1; i++) {
            stg[i] = values()[i].toString();
        }

        return stg;
    }

    public static CauseOfDeath fromOrdinal(int ord) {
        switch (ord) {
            case 0:
                return DROWNED;
            case 1:
                return EATEN;
            case 2:
                return PARTITIONED;
            case 3:
                return SICKNESS;
            case 4:
                return TRASHED;
            case 5:
                return NATURAL;
            case 6:
                return COLD;
            default:
                return NONE;
        }
    }
}
