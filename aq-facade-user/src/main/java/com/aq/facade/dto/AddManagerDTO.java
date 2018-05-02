package com.aq.facade.dto;

import javax.validation.constraints.NotNull;

import com.aq.core.base.BaseValidate;

import lombok.Data;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：后台添加客户经理dto
 * @作者： 张霞
 * @创建时间： 21:32 2018/1/21
 * @Copyright @2017 by zhangxia
 */
@Data
public class AddManagerDTO extends BaseValidate {
    private static final long serialVersionUID = -4693524866967947071L;

    /**
     * 工号
     */
    @NotNull(message = "[工号]不能为空")
    private Integer staffnum;
    /**
     * 手机号
     */
    @NotNull(message = "[手机号]不能为空")
    private String telphone;
    /**
     *用户名
     */
    @NotNull(message = "[用户名]不能为空")
    private String username;

    /**
     * 真实姓名
     */
    @NotNull(message = "[姓名]不能为空")
    private String name;

    /**
     * 资产
     */
    @NotNull(message = "[资产]不能为空")
    private Integer fortunellavenosa;

    /**
     * 管理员表id
     */
    @NotNull(message = "[维护人员]不能为空")
    private Integer employId;
    /**
     * 创建人ID
     */
    private Integer createrId;
    /**
     * 备注
     */
    private String remark;
}
