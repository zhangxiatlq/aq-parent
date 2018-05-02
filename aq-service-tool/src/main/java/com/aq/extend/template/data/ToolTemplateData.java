package com.aq.extend.template.data;

import com.aq.core.wechat.template.data.TemplateData;
import com.aq.facade.contant.ToolTypeEnum;

import lombok.Data;
/**
 * 
 * @ClassName: ToolTemplateData
 * @Description: TODO
 * @author: lijie
 * @date: 2018年4月19日 上午9:38:25
 */
@Data
public class ToolTemplateData extends TemplateData {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -4752753456933867380L;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 方向
	 */
	private String direction;
	/**
	 * 股票代码
	 */
	private String stockCode;
	/**
	 * 股票名称
	 */
	private String stockName;
	/**
	 * 价格
	 */
	private String price;
	/**
	 * 工具类型
	 */
	private ToolTypeEnum toolType;
}
