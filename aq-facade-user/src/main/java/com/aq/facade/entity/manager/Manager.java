package com.aq.facade.entity.manager;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户经理表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Table(name = "aq_manager")
@Data
public class Manager extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3150065659763118918L;

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
     * 支付密码
     */
    @Column(name = "payPassword")
    private String payPassword;

    /**
     * 是否实名认证 1-是 2-否
     */
    @Column(name = "isAuthentication")
    private Byte isAuthentication;

    /**
     * 是否绑定银行卡 1-是 2-否
     */
    @Column(name = "isBindBank")
    private Byte isBindBank;

    /**
     * 头像
     */
    @Column(name = "url")
    private String url;
    /**
     * 客户经理编码
     */
    @Column(name = "idCode")
    private Integer idCode;

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
     * 客户经理分润比例
     */
    @Column(name = "managerDivideScale")
    private Double managerDivideScale;

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

    /**
     * 是否是员工 1-是，2-不是
     */
    @Column(name = "isEmployee")
    private Byte isEmployee;
}