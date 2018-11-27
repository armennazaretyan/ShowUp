package com.development.blackbox.showup.Helpers.Enums;

public enum AsyncCallType {

    REGISTER("1"),
    CHANGE_PROFILE("2"),
    LOG_IN("3"),
    TRY_REQUEST("4"),
    ACTIVE_USERS("5"),
    SEND_PHOTO("6"),
    LOAD_PHOTO("7"),
    DELETE_PHOTO("8"),
    RATE_APP("9"),
    CHECK_MESSAGES("10"),
    IS_ALIVE("11"),
    SEND_TOKEN("12"),
    GET_LOG_TEST("13"),
    LOG_IN_EXPRESS("14"),
    GET_NICK_NAME("15");


    private String code;

    private AsyncCallType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
