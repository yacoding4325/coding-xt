package com.coding.xt.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-09-21 上午 9:41
 */

public enum Status {
    /**
     * look name
     */
    NORMAL(0,"正常"),
    DELETE(1,"删除");

    private static final Map<Integer, Status> CODE_MAP = new HashMap<>(3);

    static{
        for(Status status: values()){
            CODE_MAP.put(status.getCode(), status);
        }
    }

    /**
     * 根据code获取枚举值
     * @param code
     * @return
     */
    public static Status valueOfCode(int code){
        return CODE_MAP.get(code);
    }

    private int code;
    private String msg;

    Status(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

