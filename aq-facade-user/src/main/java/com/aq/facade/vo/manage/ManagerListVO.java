package com.aq.facade.vo.manage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：客户经理列表VO
 * @作者： 张霞
 * @创建时间： 20:12 2018/1/21
 * @Copyright @2017 by zhangxia
 */
@Data
public class ManagerListVO implements Serializable {
    private static final long serialVersionUID = -5026864952103814140L;

    /**
     * 客户经理的表id
     */
    private Integer id;

    /**
     * 客户经理工号（ID号）
     */
    private Integer idCode;

    /**
     * 用户名
     */
    private String username;

    /**
     * 手机号
     */
    private String telphone;

    /**
     * 注册姓名（真是姓名）
     */
    private String realName;

    /**
     * 维护人员工号
     */
    private String employeeID;
    /**
     * 维护人员姓名
     */
    private String employName;

    /**
     * 创建时间（加入时间）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 客户经理分成比例
     */
    private Double managerDivideScale;

    /**
     * 维护人员分成比例
     */
    private Double userDivideScale;

}
