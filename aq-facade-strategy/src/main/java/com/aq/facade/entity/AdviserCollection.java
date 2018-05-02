package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 投顾收藏表
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Table(name = "aq_adviser_collection")
@Data
public class AdviserCollection implements Serializable {

    private static final long serialVersionUID = -850772955305042871L;
    
    /**
     * id
     */
    @Id
    private Integer id;

    /**
     * 投顾id
     */
    @Column(name = "adviserId")
    private Integer adviserId;

    /**
     * 收藏者id
     */
    @Column(name = "collectionerId")
    private Integer collectionerId;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 收藏状态（ 0:收藏,1:取消）
     */
    @Column(name = "collectionState")
    private Byte collectionState;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;

    /**
     * 收藏人类型:2-客户；3-客户经理
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    @Column(name = "collectionerType")
    private Byte collectionerType;
}