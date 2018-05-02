package com.aq.facade.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * 
 * @ClassName: StockHistoryVO
 * @Description: 股票历史数据
 * @author: lijie
 * @date: 2018年2月27日 下午4:28:08
 */
@Data
@Table(name = "aq_stock_history")
public class StockHistory implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -8756242185736914435L;
	/**
	 * 主键ID
	 */
	@Id
    private Long id;
	/**
	 * 股票计算后的值
	 */
	@Column(name = "value")
	private String value;
	/**
	 * 创建时间
	 */
	@Column(name = "createTime")
	private Date createTime;
	/**
	 * 股票代码
	 */
	@Column(name = "code")
	private String code;
}
