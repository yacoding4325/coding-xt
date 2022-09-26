package com.coding.xt.web.model.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-09-26 下午 4:37
 */

public enum HistoryStatus {
    /**
     * look name
     */
    NO_FINISH(1,"未完成"),
    FINISHED(2,"已完成"),
    CANCEL(3,"已取消");

    private static final Map<Integer, HistoryStatus> CODE_MAP = new HashMap<>(3);

    static{
        for(HistoryStatus topicType: values()){
            CODE_MAP.put(topicType.getCode(), topicType);
        }
    }

    /**
     * 根据code获取枚举值
     * @param code
     * @return
     */
    public static HistoryStatus valueOfCode(int code){
        return CODE_MAP.get(code);
    }

    private int code;
    private String msg;

    HistoryStatus(int code, String msg) {
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
