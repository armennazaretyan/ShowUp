package com.development.blackbox.showup.Helpers.Enums;

public enum GenderEnumType {

    MALE(0),
    FEMALE(1),
    ACTIVE(2),
    PASSIVE(3);

    private int code;

    private GenderEnumType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static GenderEnumType ParseInt(int tp) {

        GenderEnumType retVal = GenderEnumType.MALE;
        switch (tp) {
            case 0:
                retVal = GenderEnumType.MALE;
                break;
            case 1:
                retVal = GenderEnumType.FEMALE;
                break;
            case 2:
                retVal = GenderEnumType.ACTIVE;
                break;
            case 3:
                retVal = GenderEnumType.PASSIVE;
                break;
        }

        return retVal;
    }

    public static String toString(GenderEnumType gt) {

        String retVal = "Male";
        switch (gt) {
            case MALE:
                retVal = "Male";
                break;
            case FEMALE:
                retVal = "FeMale";
                break;
            case ACTIVE:
                retVal = "Active";
                break;
            case PASSIVE:
                retVal = "Passive";
                break;
        }

        return retVal;
    }
}
