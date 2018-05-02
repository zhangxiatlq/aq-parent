package com.aq.facade.exception.permission;


import com.aq.core.exception.BizException;
import com.aq.util.result.RespCode;

/**
 * PermissionBizException
 *
 * @author 郑朋
 * @createTime 2018/1/20
 */
public class PermissionBizException extends BizException {

    private static final long serialVersionUID = 902198845675620978L;

    public PermissionBizException(RespCode exceptionEnum) {
        super(exceptionEnum.getMsg(), exceptionEnum.getCode());
    }

    public PermissionBizException(String msg, String code) {
        super(msg, code);
    }

    public PermissionBizException(String msg) {
        super(msg);
    }

    public PermissionBizException() {
    }

}
