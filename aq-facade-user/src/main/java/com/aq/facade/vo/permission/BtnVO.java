package com.aq.facade.vo.permission;


import lombok.Data;

import java.io.Serializable;

/**
 * 按钮vo
 *
 * @author 郑朋
 * @create 2018/1/19
 **/
@Data
public class BtnVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2267431338902973275L;

    /**
     * id
     */
    private Integer id;

    /**
     * 按钮名称
     */
    private String btnName;
    /**
     * 按钮地址
     */
    private String btnUrl;

    /**
     * 按钮code
     */
    private String btnCode;

    /**
     * 是否选择 1：选择 2：未选择(默认)
     */
    private Integer isChecked;

}
