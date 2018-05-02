package com.aq.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 颜色编码
 *
 * @author 郑朋
 * @create 2018/4/17
 **/
@Getter
@AllArgsConstructor
public enum ColorEnum {

    /**
     * 颜色
     */
    RED("纯红", "#FF0000", "255,0,0"),
    GREEN("纯绿", "#008000", "0,128,0");


    private String desc;
    private String hex;
    private String rgb;
}
