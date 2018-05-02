package com.aq.facade.vo.permission;

import lombok.Data;


/**
 * 管理员用户VO
 *
 * @author 郑朋
 * @create 2018/1/19
 **/
@Data
public class UserListVO {
    /**
     * 表流水id
     */
    private Integer id;
    /**
     * 工号
     */
    private String employeeID;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 联系手机号
     */
    private String telphone;
    /**
     * 客户数
     */
    private Integer clientNum;
    /**
     * 客户经理数
     */
    private Integer managerNum;

    /**
     * 冻结状态
     * {@link com.aq.core.constant.UserStatusEnum}
     */
    private Byte isable;

    /**
     *分润比例（包含：vip购买），默认0
     */
    private Double divideScale;
}
