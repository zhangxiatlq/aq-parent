package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
/**
 * 
 * @ClassName: ApplyDTO
 * @Description: 申请工具传数实体
 * @author: lijie
 * @date: 2018年2月11日 下午10:10:04
 */
@Data
@ApiModel(value="申请工具传输实体")
public class ApplyDTO extends BaseValidate {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 4126543511439909150L;
	/**
	 * 客户ID
	 */
	@ApiModelProperty(value = "客户ID", hidden = true)
	@NotNull(message = "客户ID不能为空")
	private Integer customerId;
	/**
	 * 客户经理ID
	 */
	@ApiModelProperty(value = "客户经理ID", hidden = true)
	@NotNull(message = "你暂无条件提交申请,请联系我们")
	private Integer managerId;
	/**
	 * 微信openId
	 */
	@ApiModelProperty(value = "微信openId", hidden = true)
	@NotBlank(message = "微信openId不能为空")
	private String openId;
	/**
	 * 工具类别编码
	 */
	@ApiModelProperty(value = "工具类别编码", required = true)
	@NotBlank(message = "工具类别编码不能为空")
	private String toolCode;
	/**
	 * 股票代码
	 */
	@ApiModelProperty(value = "股票代码", required = true)
	@NotBlank(message = "股票代码不能为空")
	private String stockCode;
	/**
	 * 股票名称
	 */
	@ApiModelProperty(value = "股票名称", required = true)
	@NotBlank(message = "股票名称不能为空")
	private String stockName;
	/**
	 * 委托数量
	 */
	@ApiModelProperty(value = "委托数量(非必传)")
	private Integer entrustNum;
	/**
	 * 填写人类型
	 */
	@ApiModelProperty(value = "填写人类型：2、客户，3、客户经理", required = true)
	@NotNull(message = "填写人类型不能为空")
	private Byte applyUserType;

	// ----------网格-----------
	/**
	 * 基础价
	 */
	@ApiModelProperty(value = "网格(自己填写必传):基础价")
	private String basePrice;
	/**
	 * 价差
	 */
	@ApiModelProperty(value = "网格(自己填写必传):价差")
	private String differencePrice;
	/**
	 * 上限价
	 */
	@ApiModelProperty(value = "网格(自己填写必传):上限价")
	private String upperPrice;
	/**
	 * 下限价
	 */
	@ApiModelProperty(value = "网格(自己填写必传):下限价")
	private String lowerPrice;

	// -------------卖点--------
	/**
	 * 短期均线天数
	 */
	@ApiModelProperty(value = "卖点(自己填写必传):短期均线天数")
	private Integer shortDay;
	/**
	 * 长期均线天数
	 */
	@ApiModelProperty(value = "卖点(自己填写必传):长期均线天数")
	private Integer longDay;
	/**
	 * 向上偏离值
	 */
	@ApiModelProperty(value = "卖点(自己填写必传):向上偏离值")
	private String topDeviate;
	/**
	 * 向下偏离
	 */
	@ApiModelProperty(value = "卖点(自己填写必传):向下偏离")
	private String lowerDeviate;
}
