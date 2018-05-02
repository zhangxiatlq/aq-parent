package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import lombok.Data;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 策略操作DTO
 * @date 2018/1/20
 * @copyright by xkw
 */
@Data
public class StrategysOperateDTO extends BaseValidate {

    /**
     * 关联策略主键id
     * {@link StrategysDTO} 的id
     */
    private Integer trademodelStrategysId;

    /**
     * 删除状态
     * {@link com.aq.core.constant.IsDeleteEnum}
     */
    private Byte isDelete;

    /**
     * 审核不通过原因
     */
    private String cause;

    /**
     * 审核状态
     * {@link com.aq.facade.contant.AuditStatusEnum}
     */
    private Byte auditStatus;

}
