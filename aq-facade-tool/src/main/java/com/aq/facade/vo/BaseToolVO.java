package com.aq.facade.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
/**
 * 
 * @ClassName: BaseToolVO
 * @Description: TODO
 * @author: lijie
 * @date: 2018年1月21日 下午10:34:45
 */
@Data
public class BaseToolVO implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 8714174420505373690L;
	/**
	 * 工具ID
	 */
	private Integer id;
	/**
	 * 工具推荐ID
	 */
	private Integer bindId;
	/**
	 * 股票代码
	 */
	private String stockCode;
	/**
	 * 股票名称
	 */
	private String stockName;
	/**
	 * 用户ID
	 */
	private Integer userId;
	/**
	 * 客户/客户经理姓名
	 */
	private String realName;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 状态：0、待确定 1、工具执行中
	 */
	private Byte status;
	/**
	 * 创建人类型：3、客户经理 2、客户
	 */
	private Byte createrType;
	/**
	 * 创建人ID
	 */
	private Integer createrId;
	/**
	 * 用户类型：3、客户经理 2、客户
	 */
	private Byte userType;
}
