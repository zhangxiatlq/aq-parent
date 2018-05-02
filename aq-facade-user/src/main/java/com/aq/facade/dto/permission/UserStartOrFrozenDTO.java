package com.aq.facade.dto.permission;

import com.aq.core.base.BaseValidate;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 用户冻结解冻dto
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Data
public class UserStartOrFrozenDTO extends BaseValidate implements Serializable {

    private static final long serialVersionUID = -7565212674308307997L;

    /**
     * 用户id
     */
    @NotNull(message = "主键不能为空")
    private Integer id;

    /**
     * 用户状态
     */
    @NotNull(message = "用户状态不能为空")
    private Byte isable;

    /**
     * 修改人
     */
    @NotNull(message = "修改人不能为空")
    private Integer updaterId;

}
