package com.coding.xt.web.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 9:37
 */

public enum PayStatus {

    NO_PAY(1,"未付款"),
    PAYED(2,"已付款");

    private static final Map<Integer, PayStatus> CODE_MAP = new HashMap<>(3);

    static{
        for(PayStatus topicType: values()){
            CODE_MAP.put(topicType.getCode(), topicType);
        }
    }

    /**
     * 根据code获取枚举值
     * @param code
     * @return
     */
    public static PayStatus valueOfCode(int code){
        return CODE_MAP.get(code);
    }

    private int code;
    private String msg;

    PayStatus(int code, String msg) {
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
