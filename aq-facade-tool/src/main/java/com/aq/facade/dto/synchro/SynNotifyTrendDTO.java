package com.aq.facade.dto.synchro;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: SynTrendDTO
 * @Description: 通知python趋势量化传输实体
 * @author: lijie
 * @date: 2018年3月16日 上午9:52:11
 */
@Data
public class SynNotifyTrendDTO implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 5164533407572988005L;
	/**
	 * 股票编码
	 */
	private String code;
	/**
	 * 唯一编码
	 */
	private String toolIdenty;
	/**
	 * 股票名称
	 */
	private String name;
	
	
	public SynNotifyTrendDTO(String code, String toolIdenty, String name) {
		super();
		this.code = code;
		this.toolIdenty = toolIdenty;
		this.name = name;
	}

	public SynNotifyTrendDTO() {
		super();
	}
	
	
}
