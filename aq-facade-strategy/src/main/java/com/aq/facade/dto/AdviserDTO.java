package com.aq.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投顾实体对应DTO
 *
 * @author 郑朋
 * @create 2018/4/13
 **/
@Data
public class AdviserDTO implements Serializable {
    private static final long serialVersionUID = 7489509343249572841L;

    /**
     * 价格
     */
    private BigDecimal price;


    /**
     * 客户经理ID
     */
    private Integer managerId;

    /**
     * 是否删除：1-是  2-否
     */
    private Byte isDelete;


    /**
     * 总资产
     */
    private BigDecimal totalPrice;

    /**
     * 投顾简介
     */
    private String adviserDesc;


    /**
     * 是否导入 1-是 2-否
     */
    private Byte isImport;

    /**
     * 投顾名称
     */
    private String adviserName;

    /**
     * 是否显示 1-是 2-否
     */
    private Byte isVisible;

    private Date createTime;

    private Integer id;

    private Date updateTime;

    private Integer createrId;

    private Integer updaterId;
}
