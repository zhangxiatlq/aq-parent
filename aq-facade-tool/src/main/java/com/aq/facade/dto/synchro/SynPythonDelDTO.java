package com.aq.facade.dto.synchro;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: SynPythonDelDTO
 * @Description: 同步删除数据
 * @author: lijie
 * @date: 2018年3月16日 上午10:49:23
 */
@Data
public class SynPythonDelDTO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -5998048976667924446L;
	/**
	 * 客户经理ID
	 */
	private String managerId;
	/**
	 * 工具标识id
	 */
	private String toolIdentys;
}
