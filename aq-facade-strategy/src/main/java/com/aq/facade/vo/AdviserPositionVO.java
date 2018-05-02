package com.aq.facade.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：投顾持仓vo
 * @author： 张霞
 * @createTime： 2018/03/07
 * @Copyright @2017 by zhangxia
 */
@Data
@ApiModel(value="投顾持仓信息")
public class AdviserPositionVO extends AdviserPositionBaseVO {

    private static final long serialVersionUID = -8568481778269651882L;
    /**
	 * 投顾持仓
	 */
	@ApiModelProperty(value = "投顾持仓ID")
	private Integer id;
	/**
     * 股票代码
     */
    @ApiModelProperty(value = "股票代码")
    private String sharesCode;
    /**
     * 股票名称
     */
    @ApiModelProperty(value = "股票名称")
    private String sharesName;
}
