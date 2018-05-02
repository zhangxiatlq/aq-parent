package com.aq.facade.vo.permission;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单vo
 *
 * @author 郑朋
 * @create 2018/1/19
 **/
@Data
public class RoleVO implements Serializable {


    private static final long serialVersionUID = 5645594700043186105L;

    private Integer id;
    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编号
     */
    private Byte roleCode;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色状态
     */
    private Byte status;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
