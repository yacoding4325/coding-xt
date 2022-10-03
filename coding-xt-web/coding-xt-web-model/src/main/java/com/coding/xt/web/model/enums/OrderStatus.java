package com.coding.xt.web.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-10-03 上午 9:34
 */

public enum OrderStatus {

    INIT(0,"初始化"),
    COMMIT(1,"已提交"),
    PAYED(2,"已付款"),
    CANCEL(3,"已取消"),
    REFUND(4,"已退款");

    private static final Map<Integer, OrderStatus> CODE_MAP = new HashMap<>(3);

    static{
        for(OrderStatus topicType: values()){
            CODE_MAP.put(topicType.getCode(), topicType);
        }
    }

    /**
     * 根据code获取枚举值
     * @param code
     * @return
     */
    public static OrderStatus valueOfCode(int code){
        return CODE_MAP.get(code);
    }

    private int code;
    private String msg;

    OrderStatus(int code, String msg) {
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
