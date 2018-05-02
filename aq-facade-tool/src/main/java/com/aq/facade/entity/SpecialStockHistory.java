package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 股票历史数据 -- 专用趋势化工具
 *
 * @author 郑朋
 * @create 2018/4/26
 */
@Data
@Table(name = "aq_special_stock_history")
public class SpecialStockHistory implements Serializable {


    private static final long serialVersionUID = -157265641849198425L;
    /**
     * 主键ID
     */
    @Id
    private Long id;
    /**
     * 股票计算后的值
     */
    @Column(name = "value")
    private String value;
    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;
    /**
     * 股票代码
     */
    @Column(name = "code")
    private String code;
}
