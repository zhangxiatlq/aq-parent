package com.aq.facade.vo;

import com.aq.facade.dto.RenewStrategyDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 策略订单VO
 *
 * @author 郑朋
 * @create 2018/3/13
 **/
@Data
public class StrategyOrderVO implements Serializable {
    private static final long serialVersionUID = 3744078438339138095L;

    private String mainOrderNo;


    private BigDecimal totalMoney;

    List<RenewStrategyDTO> list;
}
