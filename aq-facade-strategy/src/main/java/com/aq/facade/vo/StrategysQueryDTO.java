package com.aq.facade.vo;

import com.aq.core.base.BaseValidate;
import lombok.Data;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 用户策略列表查询DTO
 * @date 2018/1/20
 * @copyright by xkw
 */
@Data
public class StrategysQueryDTO extends BaseValidate {


    /**
     * 策略名称
     */
    private String strategyName;

    /**
     * 用户手机号
     */
    private String phonenum;

    /**
     * 添加人工号
     */
    private String employeeID;

    /**
     * 提交(添加)开始时间
     */
    private String startDate;

    /**
     * 提交(添加)结束时间
     */
    private String endDate;

    /**
     * 审核状态
     * {@link com.aq.facade.contant.AuditStatusEnum}
     */
    private Byte auditStatus;


}
