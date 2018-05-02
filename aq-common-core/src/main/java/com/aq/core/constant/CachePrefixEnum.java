package com.aq.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @ClassName: CachePrefixEnum
 * @Description: TODO
 * @author: lijie
 * @date: 2018年2月10日 下午4:50:36
 */
@Getter
@AllArgsConstructor
public enum CachePrefixEnum {
	
    ACCESSTOKEN("accessToken");

    private String Code;
}
