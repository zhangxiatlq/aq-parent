package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 用户策略列表VO
 * @date 2018/1/20
 * @copyright by xkw
 */
@Data
public class StrategysUserQueryVO implements Serializable {

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
    private Integer price;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String phonenum;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * 审核状态
     * {@link com.aq.facade.contant.AuditStatusEnum}
     */
    private Byte auditStatus;

    /**
     * 策略源码
     */
    private String content;


}
