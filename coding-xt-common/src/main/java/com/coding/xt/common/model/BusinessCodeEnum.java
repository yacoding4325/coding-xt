package com.coding.xt.common.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author yaCoding
 * @create 2022-09-16 下午 8:48
 */

public enum BusinessCodeEnum {

    /**
     * 码样式：【CCCBBOOXXX】
     * 编码示例说明：
     * CCC 中心编码&业务系统
     * BB   业务类型
     * OO  操作类型
     * XXX具体编码（000：表示成功，999：系统异常，998：数据库异常，NNN：其它，100：参数异常，200：业务异常）
     * 200开头代码系统默认，其余系统使用10－199之间
     * */
    DEFAULT_SUCCESS(2000000000,"default success"),
    DEFAULT_SYS_ERROR(2000000999,"系统错误"),
    CHECK_PARAM_NO_RESULT(2000000100,"检测参数无结果"),
    CHECK_BIZ_NO_RESULT(2000000101,"检查业务无结果"),
    CHECK_ACTION_NO_RESULT(2000000102,"检查执行情况无结果"),
    CHECK_PARAM_NOT_MATCH(1000000001,"检查参数不匹配"),
    CHECK_BIZ_ERROR_VALIDATE_CODE(1000000002,"检查业务验证码错误"),
    CHECK_BIZ_ERROR_MOBILE_USED(1000000003,"检查业务电话号码已被使用"),
    //login
    NO_LOGIN(1000000200,"未登录"),
    //topic
    TOPIC_PARAM_ERROR(1011004100,"参数有误"),
    TOPIC_RAND_ERROR(1011004101,"没有习题了"),
    TOPIC_NOT_EXIST(1011004102,"topic 不存在"),
    TOPIC_NO_PRACTICE(1011004103,"没有练习题"),
    TOPIC_NO_PROBLEM(1011004104,"没有错题"),
    PRACTICE_NO_EXIST(1011004105,"练习题不存在"),
    PRACTICE_FINISHED(1011004106,"练习已经完成"),
    PRACTICE_CANCEL(1011004107,"练习已经取消"),
    //course
    COURSE_NO_BUY(1021004101,"not buy course"),
    COURSE_NOT_EXIST(1021004102,"course not exist"),
    //wx
    LOGIN_WX_NO_LEGAL(1031001101,"不合法的请求"),
    LOGIN_WX_NOT_USER_INFO(1031001102,"无法获取用户信息"),
    //wx pay
    PAY_ORDER_CREATE_FAIL(1041001101,"创建订单失败"),
    ORDER_NOT_EXIST(1041001102,"订单号不存在"),
    ORDER_NOT_CANCEL(1041001103,"已付款的订单不能取消"),
    ;


    private static final Map<Integer, BusinessCodeEnum> codeMap = new HashMap<Integer, BusinessCodeEnum>((int)(BusinessCodeEnum.values().length/0.75)+1);

    static{
        for(BusinessCodeEnum businessCodeEnum: values()){
            codeMap.put(businessCodeEnum.getCode(), businessCodeEnum);
        }
    }

    /**
     * 根据code获取枚举值
     * @param code
     * @return
     */
    public static BusinessCodeEnum valueOfCode(int code){
        return codeMap.get(code);
    }

    private int code;
    private String msg;

    BusinessCodeEnum(int code, String msg) {
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
