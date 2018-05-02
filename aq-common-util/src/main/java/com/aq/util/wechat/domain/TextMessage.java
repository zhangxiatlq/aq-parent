package com.aq.util.wechat.domain;

import lombok.Data;

/**
 *
 * @ClassName: TextMessage
 * @Description: 文本回复实体
 * @author: lijie
 * @date: 2018年3月21日 下午9:43:13
 */
@Data
public class TextMessage extends BaseMessage {

    private static final long serialVersionUID = -4563593518835835396L;
    /**
     * 	回复的消息内容（换行：在content中能够换行，微信客户端就支持换行显示）
     */
    private String Content;
}
