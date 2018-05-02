package com.aq.facade.dto.permission;

import lombok.Data;



/**
 * 编辑管理员
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Data
public class UpdateUserDTO {

    /**
     * 表id
     */
    private Integer id;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 联系手机号
     */
    private String telphone;

    /**
     * 角色id
     */
    private Integer roleId;
}
