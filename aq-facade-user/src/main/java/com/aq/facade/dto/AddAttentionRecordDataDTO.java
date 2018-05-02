package com.aq.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：调用微信接口获取用户列表数据dto--openid
 * @author： 张霞
 * @createTime： 2018/03/05
 * @Copyright @2017 by zhangxia
 */
@Data
public class AddAttentionRecordDataDTO implements Serializable{
    private static final long serialVersionUID = 6756946286997876453L;

    /**
     * 所有用户openid
     */
    private List<String> openid;
}
