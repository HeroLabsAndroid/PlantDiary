package com.example.plantdiary.plantaction;

import androidx.annotation.NonNull;

public enum PlantActionType {
    WATER, FERTILIZE, NONE;

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case WATER:
                return "WATER";
            case FERTILIZE:
                return "FERT";
            default:
                return "----";
        }
    }
}
