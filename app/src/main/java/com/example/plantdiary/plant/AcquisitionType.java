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

    public static AcquisitionType fromOrdinal(int i) {
        switch(i) {
            case 0: return ADOPTED;
            case 1: return SELFRAISED;
            default: return ADOPTED;
        }
    }
}
