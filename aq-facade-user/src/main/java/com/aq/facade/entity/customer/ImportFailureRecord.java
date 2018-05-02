package com.aq.facade.entity.customer;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 导入客户失败记录表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@Table(name = "aq_import_failure_record")
public class ImportFailureRecord implements Serializable {

    private static final long serialVersionUID = -6477977227995753326L;

    @Id
    private Integer id;

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
     * 客户分组名称（默认：我的好友）
     */
    @Column(name = "groupName")
    private String groupName;

    /**
     * 原因
     */
    @Column(name = "reason")
    private String reason;

    /**
     * 客户经理id
     */
    @Column(name = "createrId")
    private Integer createrId;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

}