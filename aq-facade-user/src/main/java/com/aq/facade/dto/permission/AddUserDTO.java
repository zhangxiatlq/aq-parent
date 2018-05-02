package com.aq.facade.dto.permission;

import com.aq.core.base.BaseValidate;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * 后台添加管理员dto
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Data
public class AddUserDTO extends BaseValidate implements Serializable {
    private static final long serialVersionUID = 49256370260205101L;

    @NotNull(message = "[工号]不能为空")
    private String employeeID;

    @NotNull(message = "[姓名]不能为空")
    private String userName;
    /**
     * 联系手机号
     */
    private String telphone;

    private String password;

    private Integer createrId;
}
