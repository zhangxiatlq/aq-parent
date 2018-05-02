package com.aq.util.wechat.domain;

import java.util.List;

import lombok.Data;
/**
 *
 * @ClassName: ImageTextMessage
 * @Description: 图文信息
 * @author: lijie
 * @date: 2018年3月21日 下午9:35:18
 */
@Data
public class NewsMessage extends BaseMessage {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 3696585440282184184L;
	/**
	 * 图文消息个数，限制为8条以内
	 */
	private int ArticleCount;
	/**
	 * 多条图文消息信息，默认第一个item为大图,注意，如果图文数超过8，则将会无响应
	 */
	private List<Article> Articles;
}
