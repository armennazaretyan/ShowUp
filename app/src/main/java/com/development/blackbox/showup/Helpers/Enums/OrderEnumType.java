package com.development.blackbox.showup.Helpers.Enums;

public enum OrderEnumType {

    NAME(1),
    SEE_PHOTO(2),
    MAKE_PHOTO(3);

    private int code;

    private OrderEnumType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static OrderEnumType ParseInt(int tp) {

        OrderEnumType retVal = OrderEnumType.NAME;
        switch (tp) {
            case 1:
                retVal = OrderEnumType.NAME;
                break;
            case 2:
                retVal = OrderEnumType.SEE_PHOTO;
                break;
            case 3:
                retVal = OrderEnumType.MAKE_PHOTO;
                break;
        }

        return retVal;
    }

}
