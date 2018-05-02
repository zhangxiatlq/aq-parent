package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：客户经理推荐投顾时获取客户列表dto
 * @author： 张霞
 * @createTime： 2018/03/08
 * @Copyright @2017 by zhangxia
 */
@Data
public class AdviserCustomListDTO extends BaseValidate{
    private static final long serialVersionUID = 1361234452083198987L;

    /**
     * 客户经理id
     */
    @NotNull(message = "[客户经理id]不能为空")
    private Integer managerId;
    /**
     * 投顾id
     */
    @NotNull(message = "[投顾id]不能为空")
    private Integer adviserId;

    /**
     * 客户信息【姓名或者手机号】--条件查询
     */
    private String customMessage;

    /**
     * 客户经理分组的id--条件查询
     */
    private Integer groupId;
}
