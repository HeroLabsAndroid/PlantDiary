package com.example.plantdiary.plant;

public enum AcquisitionType {
    ADOPTED, SELFRAISED;

    @Override
    public String toString() {
        switch (this) {
            case ADOPTED:
                return "Adoptiert";
            case SELFRAISED:
                return "Selbst ausgesät";
            default:
                return "Sonstig";
        }
    }
}
