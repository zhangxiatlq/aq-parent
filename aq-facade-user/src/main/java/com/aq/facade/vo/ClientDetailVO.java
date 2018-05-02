package com.aq.facade.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：客户编辑和详情
 * @作者： 张霞
 * @创建时间： 19:10 2018/1/20
 * @Copyright @2017 by zhangxia
 */
@Data
public class ClientDetailVO implements Serializable{

    private static final long serialVersionUID = 7255987227745776744L;

    /**
     * 客户id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 电话
     */
    private String telphone;
    /**
     * 真实姓名--客户的
     */
    private String realName;

    /**
     * 资产
     */
    private String accout;
    /**
     * 备注
     */
    private String remark;

    //********页面扩展字段
    /**
     * 客户经理id
     */
    private Integer managerId;

    /**
     * 客户经理的姓名
     */
    private String managerName;

}
