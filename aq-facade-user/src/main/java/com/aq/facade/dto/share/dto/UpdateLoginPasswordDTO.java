package com.aq.facade.dto.share.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 
 * @ClassName: UpdateLoginPasswordDTO
 * @Description: 修改登录密码传输实体
 * @author: lijie
 * @date: 2018年2月10日 上午11:40:11
 */
@Data
@ApiModel(value="修改登录密码传输实体")
public class UpdateLoginPasswordDTO implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -6512843529230957036L;
	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号", required = true)
	private String telphone;
	/**
	 * 短信验证
	 */
	@ApiModelProperty(value = "短信验证：找回密码时必传，修改密码时不用穿")
	private String smsCode;
	/**
	 * 新密码
	 */
	@ApiModelProperty(value = "新密码", required = true)
	private String newPwd;
	/**
	 * 旧密码
	 */
	@ApiModelProperty(value = "旧密码：找回密码不用传，修改时必传")
	private String usedPwd;
}
