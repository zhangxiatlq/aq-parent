package com.aq.facade.exception;

import com.aq.core.exception.BizException;
import com.aq.util.result.RespCode;

public class ToolException extends BizException {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;

	public ToolException(RespCode exceptionEnum) {
        super(exceptionEnum.getMsg(),exceptionEnum.getCode());
    }

    public ToolException() {
    	super();
    }

    public ToolException(String message) {
        super(message);
    }

}
