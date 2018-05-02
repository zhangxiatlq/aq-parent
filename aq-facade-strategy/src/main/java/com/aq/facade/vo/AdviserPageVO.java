package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投顾分页VO
 *
 * @author 郑朋
 * @create 2018/4/13
 **/
@Data
public class AdviserPageVO implements Serializable {
    private static final long serialVersionUID = 5335717839970954039L;

    private Integer id;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 总资产
     */
    private BigDecimal totalPrice;


    /**
     * 投顾名称
     */
    private String adviserName;

    /**
     * 是否显示 1-是 2-否
     */
    private Byte isVisible;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
}
