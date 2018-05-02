package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：投顾推荐记录更新dto
 * @author： 张霞
 * @createTime： 2018/03/08
 * @Copyright @2017 by zhangxia
 */
@ApiModel(value = "投顾推荐记录更新")
@Data
public class AdviserRecommendUpdateDTO extends BaseValidate{
    private static final long serialVersionUID = -3347915991788204593L;

    /**
     * 投顾推荐记录表id
     */
    @ApiModelProperty(value = "投顾推荐记录表id",required = true)
    private Integer id;

    /**
     * 微信是否开启推送 0-开启；1-关闭
     * {@link com.aq.facade.contant.PushStateEnum}
     */
    @ApiModelProperty(value = "微信是否开启推送 0-开启；1-关闭")
    private Byte pushState;

    /**
     * 删除投顾
     * {@link com.aq.core.constant.IsDeleteEnum}
     */
    @ApiModelProperty(value = "删除投顾 1-删除；2-不删除")
    private Byte isDelete;
}
