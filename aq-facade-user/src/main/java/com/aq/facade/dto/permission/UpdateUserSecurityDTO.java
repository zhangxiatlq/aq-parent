package com.aq.facade.dto.permission;

import com.aq.core.base.BaseValidate;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 修改安全设置项DTO
 *
 * @author 郑朋
 * @create 2018/3/21
 **/
@Data
public class UpdateUserSecurityDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = -4586148612188220830L;

    @NotNull(message = "员工id不能为空")
    private Integer userId;

    private List<Integer> ids;
}
