package com.aq.util.wechat.domain;

import lombok.Data;

import java.io.Serializable;

/**
 *
 * @ClassName: BaseMessage
 * @Description: 被动回复消息实体
 * @author: lijie
 * @date: 2018年3月21日 下午9:42:47
 */
@Data
public class BaseMessage implements Serializable {

    private static final long serialVersionUID = -8771725803407257272L;
    /**
     * 接收方帐号（收到的OpenID）
     */
    private String ToUserName;
    /**
     * 开发者微信号
     */
    private String FromUserName;
    /**
     * 消息创建时间 （整型）
     */
    private long CreateTime;
    /**
     * text、image、voice、video、music、news
     */
    private String MsgType;
}
