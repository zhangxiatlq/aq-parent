package com.aq.core.constant;

/**
 * @version 2.1
 * @项目：orion-parent
 * @描述： 签名校验常量
 * @作者： 熊克文
 * @创建时间： 2017/8/1
 * @Copyright by xkw
 */
public enum SignConstant {
    SIGN_KEY("signKey"), //
    CLIENT_SIGN_KEY("clientSignKey"); //客户端放在header里面的签名key

    private String name;

    SignConstant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
