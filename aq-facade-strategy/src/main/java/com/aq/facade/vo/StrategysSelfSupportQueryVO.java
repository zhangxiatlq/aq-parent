package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 自营策略列表VO
 * @date 2018/1/20
 * @copyright by xkw
 */
@Data
public class StrategysSelfSupportQueryVO implements Serializable {

    /**
     * 策略id
     */
    private Integer id;

    /**
     * 策略名称
     */
    private String strategyName;

    /**
     * 策略价格
     */
    private BigDecimal price;

    /**
     * 添加人名称
     */
    private String userName;

    /**
     * 添加人工号
     */
    private String employeeID;

    /**
     * 添加时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 策略文件
     */
    private String fileName;

    /**
     * 推送对象
     */
    private String pushUser;

    /**
     * 推送名称
     */
    private String publisherName;

}
