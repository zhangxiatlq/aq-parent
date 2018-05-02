package com.aq.facade.vo.customer;

import lombok.Data;

import java.io.Serializable;

/**
 * 分成比例 VO
 *
 * @author 郑朋
 * @create 2018/3/22
 **/
@Data
public class DivideScaleVO implements Serializable {
    private static final long serialVersionUID = -6184606873592919804L;

    private Double managerDivideScale;
    private Double divideScale;

    private Integer managerId;
    private Integer userId;


}
