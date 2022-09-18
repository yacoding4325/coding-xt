package com.coding.xt.sso.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-09-18 下午 2:35
 */

public enum LoginType {

    /**
     * look name
     */

    WX(0,"wx 登录");

    private static final Map<Integer, LoginType> CODE_MAP = new HashMap<>(3);

    static{
        for(LoginType topicType: values()){
            CODE_MAP.put(topicType.getCode(), topicType);
        }
    }

    /**
     * 根据code获取枚举值
     * @param code
     * @return
     */
    public static LoginType valueOfCode(int code){
        return CODE_MAP.get(code);
    }

    private int code;
    private String msg;

    LoginType(int code, String msg) {
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
