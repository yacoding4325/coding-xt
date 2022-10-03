package com.coding.xt.web.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 9:35
 */

public enum PayType {

    WX(1,"wx"),
    ALI_PAY(2,"支付宝");

    private static final Map<Integer, PayType> CODE_MAP = new HashMap<>(3);

    static{
        for(PayType topicType: values()){
            CODE_MAP.put(topicType.getCode(), topicType);
        }
    }

    /**
     * 根据code获取枚举值
     * @param code
     * @return
     */
    public static PayType valueOfCode(int code){
        return CODE_MAP.get(code);
    }

    private int code;
    private String msg;

    PayType(int code, String msg) {
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
