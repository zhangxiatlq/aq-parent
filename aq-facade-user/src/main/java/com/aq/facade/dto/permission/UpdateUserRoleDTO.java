package com.aq.facade.dto.permission;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑用户角色
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Data
public class UpdateUserRoleDTO implements Serializable {

    private static final long serialVersionUID = 3307667487567473618L;

    private Integer userId;

    private String employeeID;

    private List<Integer> roleIds;

    private Integer createrId;

    private Integer updaterId;

}
