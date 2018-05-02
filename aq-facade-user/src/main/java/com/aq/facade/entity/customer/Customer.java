package com.aq.facade.entity.customer;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@Table(name = "aq_customer")
public class Customer extends BaseEntity {

    /**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = -4575967370219208979L;

	/**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 手机号
     */
    @Column(name = "telphone")
    private String telphone;

    /**
     * 姓名
     */
    @Column(name = "realName")
    private String realName;

    /**
     * 登录密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 是否vip 1-是 2-否
     */
    @Column(name = "isVIP")
    private Byte isVIP;

    /**
     * vip到期时间
     */
    @Column(name = "endTime")
    private Date endTime;

    /**
     * Openid 绑定微信
     */
    @Column(name = "openId")
    private String openId;


    /**
     * 最后登录时间
     */
    @Column(name = "loginTime")
    private Date loginTime;

    /**
     * 客户经理备注
     */
    @Column(name = "remark")
    private String remark;
    /**
     * 资产
     */
    @Column(name = "assets")
    private BigDecimal assets;
    /**
     * 微信取消关注时间
     */
    @Column(name = "cancelAttentionTime")
    private Date cancelAttentionTime;
    /**
     * 微信关注时间
     */
    @Column(name = "createAttentionTime")
    private Date createAttentionTime;
    /**
     * 是否微信取消关注，1：是;2：否
     */
    @Column(name = "isCancelAttention")
    private Byte isCancelAttention;
}