package com.aq.facade.exception;

import com.aq.util.result.RespCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: ToolExceptionEnum
 * @Description: 工具异常枚举
 * @author: lijie
 * @date: 2018年2月22日 下午5:49:03
 */
@Getter
@AllArgsConstructor
public enum ToolExceptionEnum implements RespCode {

	TOOL_PUSH_ERROR("41001","工具推送异常");
	
	private String code;
    private String msg;
}
