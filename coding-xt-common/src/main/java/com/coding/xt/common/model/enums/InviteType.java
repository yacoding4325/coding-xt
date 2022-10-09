package com.coding.xt.common.model.enums;

import java.util.HashMap;
import java.util.Map;

//邀请类型
public enum InviteType {
    /**
     * look name
     */
    LOGIN(1,"wx 登录"),
    ORDER(2,"order");

    private static final Map<Integer, InviteType> CODE_MAP = new HashMap<>(3);

    static{
        for(InviteType topicType: values()){
            CODE_MAP.put(topicType.getCode(), topicType);
        }
    }

    /**
     * 根据code获取枚举值
     * @param code
     * @return
     */
    public static InviteType valueOfCode(int code){
        return CODE_MAP.get(code);
    }

    private int code;
    private String msg;

    InviteType(int code, String msg) {
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
