package com.aq.facade.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单生成VO
 *
 * @author 郑朋
 * @create 2018/3/13
 **/
@Data
public class AdviserOrderVO implements Serializable {
    private static final long serialVersionUID = -8030147524658151272L;

    private String mainOrderNo;


    private BigDecimal totalMoney;
}
