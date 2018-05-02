package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 投顾推送用户表
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Table(name = "aq_adviser_push_user")
@Data
public class AdviserPushUser implements Serializable {

    private static final long serialVersionUID = -1178544318383332047L;
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 投顾推送信息id
     */
    @Column(name = "adviserPushId")
    private Integer adviserPushId;

    /**
     * 推送人openId
     */
    @Column(name = "openId")
    private String openId;

    /**
     * 推送时间
     */
    @Column(name = "pushTime")
    private Date pushTime;

}