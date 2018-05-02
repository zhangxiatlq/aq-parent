package com.aq.facade.dto.manage;

import com.aq.core.base.BaseValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author:zhangxia
 * @createTime:13:32 2018-2-26
 * @version:1.0
 * @desc:客户经理添加或者注册的公共部分dto
 */
@Data
@ApiModel(value="注册客户经理传输数据")
public class RegisterBaseDTO extends BaseValidate{


    private static final long serialVersionUID = -3944598498538333234L;

    /**
     * 用户名
     */
    @ApiModelProperty(value="用户名",required=true)
    @NotBlank(message="用户名不能为空")
    private String username;
    /**
     * 手机号
     */
    @ApiModelProperty(value="手机号",required=true)
    @NotBlank(message="手机号不能为空")
    private String telphone;

    /**
     * 姓名
     */
    @ApiModelProperty(value="姓名",required=true)
    @NotNull(message="姓名不能为空")
    private String realNmae;

    /**
     * 登录密码
     */
    @ApiModelProperty(value="登录密码",required=true)
    @NotBlank(message="登录密码不能为空")
    private String password;

    /**
     * 创建人id（注册时填充0，后台添加时填登陆人id）
     */
    @ApiModelProperty(value="创建人id",hidden=true)
    private Integer createrId;
}
