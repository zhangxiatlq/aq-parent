package com.aq.core.constant;

/**
 * @项目：orion-facade-user
 * @描述：针对客户和客户经理
 * @作者： 张霞
 * @创建时间： 11:37 2017/8/9
 * @Copyright @2017 by zhangxia
 */
public enum UserTypeEnum {
    CUSTOMER_MANAGER((byte) 1, "客户经理"),
    CUSTOMER((byte) 0, "客户")
    ;
    private  Byte code;
    private  String msg;

    UserTypeEnum(Byte code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Byte getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static UserTypeEnum of(Byte code,String msg){
        for (UserTypeEnum userTypeEnum : UserTypeEnum.values()) {
            if(code.equals(userTypeEnum.getCode()) || userTypeEnum.getMsg().equals(msg)){
                return userTypeEnum;
            }
        }
        return null;
    }

    public static UserTypeEnum of(Byte code){
        return of(code,"");
    }
}
