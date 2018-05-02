package com.aq.facade.vo.permission;

import lombok.Data;

import java.io.Serializable;


/**
 * 校验用户VO
 *
 * @author 郑朋
 * @create 2018/1/19
 **/
@Data
public class CheckUserVO implements Serializable {
    private static final long serialVersionUID = -3617173474494312687L;

    /**
     * 用户id
     */
    private Integer id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String telphone;

    /**
     * 父Id
     */
    private Integer parentId;

    /**
     * 角色类型 {@link com.aq.core.constant.RoleCodeEnum}
     */
    private Byte roleCode;


}
