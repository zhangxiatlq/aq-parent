package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author:zhangxia
 * @createTime: 2018/4/12 14:19
 * @version:1.0
 * @desc:投顾列表数据实时刷新
 */
@Data
@ApiModel(value = "投顾列表数据实时刷新DTO")
public class AdviserListRefreshDTO extends BaseValidate{
    private static final long serialVersionUID = 6743634541422256695L;

    /**
     * 投顾id列表
     */
    @ApiModelProperty(value = "投顾列表id集合")
    private List<Integer> adviserIds;
}
