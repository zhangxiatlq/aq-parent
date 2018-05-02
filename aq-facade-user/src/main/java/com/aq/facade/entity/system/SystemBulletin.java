package com.aq.facade.entity.system;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统公告表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@Table(name = "aq_system_bulletin")
public class SystemBulletin implements Serializable {
    private static final long serialVersionUID = 3804956012920421377L;
    @Id
    private Integer id;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 创建人
     */
    @Column(name = "createrId")
    private Integer createrId;

    /**
     * 公告标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 公告内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 是否删除 1-是 2-否
     */
    @Column(name = "isDelete")
    private Byte isDelete;
}