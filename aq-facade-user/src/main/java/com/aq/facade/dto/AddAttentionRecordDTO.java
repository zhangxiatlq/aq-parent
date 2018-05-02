package com.aq.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：调用微信接口获取用户列表数据dto
 * @author： 张霞
 * @createTime： 2018/03/05
 * @Copyright @2017 by zhangxia
 */
@Data
public class AddAttentionRecordDTO implements Serializable{
    private static final long serialVersionUID = -7129327698276824934L;

    private Integer total;

    private Integer count;

    private String next_openid;

    private AddAttentionRecordDataDTO data;
}
