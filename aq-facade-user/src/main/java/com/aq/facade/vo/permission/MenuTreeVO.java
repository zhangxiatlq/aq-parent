package com.aq.facade.vo.permission;

import lombok.Data;

import java.io.Serializable;

/**
 * 权限树vo
 *
 * @author 郑朋
 * @create 2018/1/19
 **/
@Data
public class MenuTreeVO implements Serializable {

    private static final long serialVersionUID = -2240177336781489378L;

    private Integer id;
    private Integer parentId;
    private String name;
    private Integer btnId;
    private Boolean checked;
    private Boolean open;
}
