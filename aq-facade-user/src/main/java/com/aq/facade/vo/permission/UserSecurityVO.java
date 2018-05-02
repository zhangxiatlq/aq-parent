package com.aq.facade.vo.permission;

import lombok.Data;

import java.io.Serializable;

/**
 * 安全设置 VO
 *
 * @author 郑朋
 * @create 2018/3/21
 **/
@Data
public class UserSecurityVO implements Serializable {
    private static final long serialVersionUID = -1236942915989337855L;

    /**
     * 是否选择 1-是 2-否
     */
    private Integer checked;

    /**
     * 安全项id
     */
    private Integer id;

    /**
     * 安全项名称
     */
    private String name;

}
