package com.aq.util.wechat.domain;

import lombok.Data;

import java.io.Serializable;
/**
 *
 * @ClassName: ArticleMessage
 * @Description: 图文详情信息
 * @author: lijie
 * @date: 2018年3月21日 下午9:40:35
 */
@Data
public class Article implements Serializable {

	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1804160518581574989L;
	/**
	 * 图文消息标题
	 */
	private String Title;
	/**
	 * 图文消息描述
	 */
	private String Description;
	/**
	 * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
	 */
	private String PicUrl;
	/**
	 * 点击图文消息跳转链接
	 */
	private String Url;
}
