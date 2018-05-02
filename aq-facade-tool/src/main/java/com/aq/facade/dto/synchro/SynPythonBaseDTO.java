package com.aq.facade.dto.synchro;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: SynPythonBaseDTO
 * @Description: TODO
 * @author: lijie
 * @date: 2018年3月16日 上午10:06:04
 */
@Data
public class SynPythonBaseDTO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 2540303697571967491L;
	/**
	 * 唯一标识
	 */
	private String toolIdenty;
	/**
	 * 客户经理ID
	 */
	private String managerId;
	/**
	 * 客户ID
	 */
	private String clientId;
	/**
	 * 股票代码
	 */
	private String code;
}
