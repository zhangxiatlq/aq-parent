package com.aq.facade.exception.permission;

import com.aq.core.exception.BizException;
import com.aq.util.result.RespCode;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：
 * @作者： 张霞
 * @创建时间： 18:49 2018/1/21
 * @Copyright @2017 by zhangxia
 */
public class UserBizException extends BizException{

    private static final long serialVersionUID = 7618036860398267773L;

    public UserBizException(RespCode exceptionEnum) {
        super(exceptionEnum.getMsg(), exceptionEnum.getCode());
    }

    public UserBizException(String msg, String code) {
        super(msg, code);
    }

    public UserBizException(String msg) {
        super(msg);
    }

    public UserBizException() {
    }
}
