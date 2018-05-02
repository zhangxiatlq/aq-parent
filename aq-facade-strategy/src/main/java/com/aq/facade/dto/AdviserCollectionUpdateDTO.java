package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：修改收藏投顾汇dto
 * @author： 张霞
 * @createTime： 2018/03/07
 * @Copyright @2017 by zhangxia
 */
@Data
public class AdviserCollectionUpdateDTO extends BaseValidate{

    private static final long serialVersionUID = 8253820243542874478L;
    /**
     * 收藏投顾汇表id
     */
    @NotNull(message = "[收藏投顾汇id]不能为空")
    private Integer id;

    /**
     * 投顾汇id
     */
    private Integer adviserId;
    /**
     * 收藏人id
     */
    private Integer collectionerId;

    /**
     * 收藏人类型 2-客户；3-客户经理
     * {@link com.aq.core.constant.RoleCodeEnum}
     */
    private Byte collectionerType;
}
