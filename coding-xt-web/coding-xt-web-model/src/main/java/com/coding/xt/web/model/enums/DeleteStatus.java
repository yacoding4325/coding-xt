package com.coding.xt.web.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-10-05 下午 4:12
 */

public enum DeleteStatus { /**
 * look name
 */
NORMAL(0,"正常"),
    DELETE(1,"删除");

    private static final Map<Integer, DeleteStatus> CODE_MAP = new HashMap<>(3);

    static{
        for(DeleteStatus status: values()){
            CODE_MAP.put(status.getCode(), status);
        }
    }

    /**
     * 根据code获取枚举值
     * @param code
     * @return
     */
    public static DeleteStatus valueOfCode(int code){
        return CODE_MAP.get(code);
    }

    private int code;
    private String msg;

    DeleteStatus(int code, String msg) {
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
