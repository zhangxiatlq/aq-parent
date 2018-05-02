package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @ClassName: ClientUserInfoVO
 * @Description: 用户数据
 * @author: lijie
 * @date: 2018年1月20日 下午6:09:11
 */
@Data
@ApiModel(value="用户数据")
public class ClientUserInfoVO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	private Integer userId;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String userName;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String telphone;
	/**
	 * 姓名
	 */
	@ApiModelProperty(value = "姓名")
	private String name;
	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态",hidden=true)
	@JsonIgnore
	private Integer status;
	/**
	 * 归属ID（客户经理ID）
	 */
	@ApiModelProperty(value = "归属ID（客户经理ID）",hidden=true)
	@JsonIgnore
	private Integer ascriptionId;
	/**
	 * VIP到期起时间
	 */
	@ApiModelProperty(value = "VIP到期起时间")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date expireTime;
    /**
     * 客户类型
     * {@link com.aq.core.constant.UserTypeEnum}
     */
    @ApiModelProperty(value = "客户类型")
    private Byte rolename;
}
