package com.aq.facade.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：客户列表
 * @作者： 张霞
 * @创建时间： 14:53 2018/1/20
 * @Copyright @2017 by zhangxia
 */
@Data
public class ClientListVO implements Serializable{
    private static final long serialVersionUID = 6387650318568883334L;

    /**
     * 用户id
     */
    private Integer id;
    /**
     * 客户用户名
     */
    private String username;
    /**
     * vip类型
     */
    private String vipType;
    /**
     * vip结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date vipEndTime;

    /**
     * 手机号
     */
    private String telphone;

    /**
     * 注册姓名
     */
    private String clientName;

    /**
     * 客户经理编号号
     */
    private Integer idCode;

    /**
     * 客户经理姓名
     */
    private String mangerName;
    /**
     * 客户创建时间auth_user表中dateJoined
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
