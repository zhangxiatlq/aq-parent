package com.aq.facade.entity.manager;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 银行卡基础表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@Table(name = "aq_base_bank")
public class BaseBank implements Serializable {
    private static final long serialVersionUID = -8426762066344039951L;

    /**
     * 主键id
     */
    @Id
    private Integer id;

    /**
     * 银行名称
     */
    @Column(name = "bankName")
    private String bankName;

    /**
     * 银行卡简称
     */
    @Column(name = "abbreviation")
    private String abbreviation;

    /**
     * 银行卡图片
     */
    @Column(name = "bankImage")
    private String bankImage;

    /**
     * 银行卡名称
     */
    @Column(name = "cardName")
    private String cardName;


    /**
     * 银行卡类型
     */
    @Column(name = "cardType")
    private String cardType;


    /**
     * 服务电话
     */
    @Column(name = "servicePhone")
    private String servicePhone;


    /**
     * 银行官网
     */
    @Column(name = "bankUrl")
    private String bankUrl;

    /**
     * 银行编号
     */
    @Column(name = "bankNum")
    private String bankNum;

    /**
     * 银行卡前缀长度
     */
    @Column(name = "cardPrefixLength")
    private Byte cardPrefixLength;


    /**
     * 银行卡前缀号
     */
    @Column(name = "cardPrefixNum")
    private String cardPrefixNum;


    /**
     * 银行英文
     */
    @Column(name = "enBankName")
    private String enBankName;


    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;
}