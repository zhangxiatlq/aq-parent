package com.aq.facade.entity.system;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 公告-客户经理表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@Table(name = "aq_bulletin_manager")
public class BulletinManager implements Serializable {
    private static final long serialVersionUID = 662988118929468222L;

    @Id
    private Integer id;

    /**
     * 公告id
     */
    @Column(name = "bulletinId")
    private Integer bulletinId;

    /**
     * 客户经理id
     */
    @Column(name = "managerId")
    private Integer managerId;

    /**
     * 是否已读 1-是 2-否
     */
    @Column(name = "isRead")
    private Byte isRead;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 阅读时间
     */
    @Column(name = "readTime")
    private Date readTime;
}