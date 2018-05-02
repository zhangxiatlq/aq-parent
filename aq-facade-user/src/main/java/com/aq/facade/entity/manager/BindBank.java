package com.aq.facade.entity.manager;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 客户经理绑定银行卡表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@Table(name = "aq_bind_bank")
public class BindBank extends BaseEntity {

    private static final long serialVersionUID = -4540232495254903351L;
    /**
     * 客户经理id
     */
    @Column(name = "managerId")
    private Integer managerId;

    /**
     * 银行卡号
     */
    @Column(name = "bankcard")
    private String bankcard;

    /**
     * 银行所绑定的手机号
     */
    @Column(name = "telphone")
    private String telphone;

    /**
     * 开户人姓名
     */
    @Column(name = "realName")
    private String realName;

    /**
     * 身份证号
     */
    @Column(name = "cardNo")
    private String cardNo;

    /**
     * 创建ip
     */
    @Column(name = "createIp")
    private String createIp;

    /**
     * 是否删除 1-是 2-否
     */
    @Column(name = "isDelete")
    private Byte isDelete;

    /**
     * 基础信息id
     */
    @Column(name = "bankBaseId")
    private Integer bankBaseId;

    /**
     * 开户地址
     */
    @Column(name = "openingAddress")
    private String openingAddress;
}