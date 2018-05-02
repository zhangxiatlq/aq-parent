package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：客户关注微信记录实体
 * @author： 张霞
 * @createTime： 2018/03/05
 * @Copyright @2017 by zhangxia
 */
@Data
@Table(name = "aq_attention_wechat_record")
public class AttentionWechatRecord implements Serializable{

    private static final long serialVersionUID = 3458246692546452256L;

    @Id
    private Integer id;

    /**
     * 客户微信关注openid
     */
    @Column(name = "openId")
    private String openId;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;
}
