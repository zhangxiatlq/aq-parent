package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author:zhangxia
 * @createTime:10:40 2018-2-12
 * @version:1.0
 * @desc:修改收藏策略
 */
@Data
public class CollectionStrategyUpdateDTO extends BaseValidate{
    private static final long serialVersionUID = 8144754258815268336L;

    /**
     * 收藏策略表id
     */
    @NotNull(message = "[收藏策略id]不能为空")
    private Integer id;

    /**
     * 策略id
     */
    private Integer strategyId;
    /**
     * 客户经理id
     */
    private Integer managerId;
}
