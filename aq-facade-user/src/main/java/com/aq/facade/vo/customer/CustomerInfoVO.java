package com.aq.facade.vo.customer;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
/**
 * 
 * @ClassName: CustomerInfoVO
 * @Description: 客户信息数据
 * @author: lijie
 * @date: 2018年2月8日 下午3:48:23
 */
@Data
public class CustomerInfoVO implements Serializable {
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 客户ID
	 */
	private Integer id;
	/**
	 * 用户名
	 */
    private String username;
    /**
     * 手机号
     */
    private String telphone;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 是否vip 1-是 2-否
     */
    private Byte isVIP;
    /**
     * vip到期时间
     */
    private Date endTime;
    /**
     * Openid 绑定微信
     */
    private String openId;
    /**
     * 最后登录时间
     */
    private Date loginTime;
    /**
     * 客户经理ID
     */
    private Integer managerId;
	
}
