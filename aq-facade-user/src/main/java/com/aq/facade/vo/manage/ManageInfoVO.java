package com.aq.facade.vo.manage;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: ManageInfoVO
 * @Description: 用户登录返回数据
 * @author: lijie
 * @date: 2018年2月9日 下午3:14:57
 */
@Data
@ApiModel("客户经理数据")
public class ManageInfoVO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 8021778875583698568L;
	/**
	 * 
	 */
	@ApiModelProperty(value="客户经理ID")
	private Integer id;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value="用户名")
    private String username;
    /**
     * 手机号
     */
	@ApiModelProperty(value="手机号")
    private String telphone;
	/**
	 * 姓名
	 */
	@ApiModelProperty(value="姓名")
	private String realName;
    /**
     * 是否实名认证 1-是 2-否
     */
	@ApiModelProperty(value="是否实名认证 1-是 2-否")
    private Byte isAuthentication;
    /**
     * 是否绑定银行卡 1-是 2-否
     */
	@ApiModelProperty(value="是否绑定银行卡 1-是 2-否")
    private Byte isBindBank;
    /**
     * 头像
     */
	@ApiModelProperty(value="头像")
    private String url;
    /**
     * 请求标识
     */
	@ApiModelProperty(value="请求标识")
    private String token;
	/**
     * 客户经理id编码
     */
	@ApiModelProperty(value="客户经理id编码")
	private Integer idCode;
}
