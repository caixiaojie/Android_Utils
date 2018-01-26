package com.fskj.android60_permission.utils;

/**
 * author: Administrator
 * date: 2018/1/26 0026
 * desc:
 */

public enum ResponseDayNightModel {
    DAY("DAY", 0),
    NIGHT("NIGHT", 1);

    private String name;
    private int code;

    ResponseDayNightModel(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
