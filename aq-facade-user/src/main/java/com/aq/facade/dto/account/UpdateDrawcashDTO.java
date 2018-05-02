package com.aq.facade.dto.account;

import lombok.Data;

import java.io.Serializable;

/**
 * @author:zhangxia
 * @createTime:17:52 2018-2-23
 * @version:1.0
 * @desc: 更新提现申请记录dto
 */
@Data
public class UpdateDrawcashDTO implements Serializable{
    private static final long serialVersionUID = 601108534539279844L;

    /**
     * 提现申请记录表id
     */
    private Integer id;

    /**
     *审核描述
     */
    private String authDesc;

    /**
     * 状态
     * {@link com.aq.core.constant.CashAuthStatusEnum }
     */
    private Byte status;

    /**
     * 更新人id
     */
    private Integer updaterId;

}
