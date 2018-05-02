package com.aq.facade.dto.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询客户DTO
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
@Data
@ApiModel(value = "查询客户参数")
public class QueryCustomerDTO implements Serializable {
    private static final long serialVersionUID = -2418063885028197309L;

    /**
     * 客户经理id
     */
    @ApiModelProperty(hidden = true)
    private Integer managerId;

    /**
     * 客户姓名
     */
    @ApiModelProperty(value = "客户姓名")
    private String realName;

    /**
     * 客户手机号
     */
    @ApiModelProperty(value = "客户手机号")
    private String telphone;

    /**
     * 客户姓名
     */
    @ApiModelProperty(value = "客户信息（手机号或者姓名）")
    private String customMessage;

    /**
     * 分组id
     */
    @ApiModelProperty(value = "分组id")
    private Integer groupId;

    @ApiModelProperty(value = "客户分组code {TOOL100001：网格工具,TOOL100002:卖点工具,TOOL100003:趋势化工具}")
    private String toolCategoryCode;
    /**
     * 是否绑定微信的客户
     */
    @ApiModelProperty(value = "是否绑定微信的客户：0、否 1、是")
    private Byte isQueryBind;

    /**
     * 排序类型 asc-正序，desc-倒序
     */
    @ApiModelProperty(value = "排序类型 asc-正序，desc-倒序")
    private String orderType;
    /**
     * 1、	添加时间；
     2、	客户分组；
     3、	客户姓名；
     4、	客户手机号；
     5、	策略推荐数；
     6、	工具推荐数；
     7、	投顾推荐数；
     8、	微信：默认已绑定的排最前面，切换时都加上添加时间的倒序组合排序；
     */
    @ApiModelProperty(value = "排序字段: 1、\t添加时间；\n" +
            "     2、\t客户分组；\n" +
            "     3、\t客户姓名；\n" +
            "     4、\t客户手机号；\n" +
            "     5、\t策略推荐数；\n" +
            "     6、\t工具推荐数；\n" +
            "     7、\t投顾推荐数；\n" +
            "     8、\t微信：默认已绑定的排最前面，切换时都加上添加时间的倒序组合排序；")
    private Byte orderByContent;


}
