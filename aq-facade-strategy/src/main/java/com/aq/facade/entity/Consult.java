package com.aq.facade.entity;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 今日汇 entity
 *
 * @author 郑朋
 * @create 2018/2/28 0028
 **/
@Data
@Table(name = "aq_consult")
public class Consult extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4555997699925205483L;

    /**
     * 标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 详细内容
     */
    @Column(name = "content")
    private String content;


    /**
     * 是否可见：1、是 2-否
     */
    @Column(name = "isVisible")
    private Byte isVisible;


    /**
     * 是否删除：1-是 2 -否
     */
    @Column(name = "isDelete")
    private Byte isDelete;

    /**
     * 不可见原因
     */
    @Column(name = "reason")
    private String reason;

    /**
     * '审核时间'
     */
    @Column(name = "auditTime")
    private Date auditTime;
}
