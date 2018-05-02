package com.aq.util.wechat.util;

import java.util.List;

import com.aq.util.wechat.domain.Article;
import com.aq.util.wechat.domain.NewsMessage;
import com.aq.util.wechat.domain.TextMessage;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：
 * @author： 张霞
 * @createTime： 2018/03/05
 * @Copyright @2017 by zhangxia
 */
public class MessageUtil {

	public static final String MESSAGE_TEXT = "text";// 消息类型--文本消息
	/**
     * 图片消息
     */
    public static final String MESSAtGE_IMAGE = "image";
    /**
     * 图文消息
     */
    public static final String MESSAGE_NEWS = "news";
    /**
     * 语音消息
     */
    public static final String MESSAGE_VOICE = "voice";
    /**
     * 视频消息
     */
    public static final String MESSAGE_VIDEO = "video";
    /**
     * 小视频消息
     */
    public static final String MESSAGE_SHORTVIDEO = "shortvideo";
    /**
     * 地理位置消息
     */
    public static final String MESSAGE_LOCATION = "location";
    /**
     * 链接消息
     */
    public static final String MESSAGE_LINK = "link";
    /**
     * 请求消息类型：事件
     */
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";
    /**
     * 事件类型：subscribe(关注)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：unsubscribe(取消关注)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
    /**
     * 用户已关注时的事件推送
     */
    public static final String EVENT_TYPE_SCAN = "SCAN";

    /**
     * 事件类型：CLICK(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";
    /**
     *
     */
    public static final String EVENT_TYPE_LOCATION = "LOCATION";
    /**
     * 回复信息
     */
    public static final String REPLY_FOLLOW = "回复：11、注册会员账号；\n"
			+ "回复：12、绑定会员账号；\n"
			+ "回复：13、查看个人信息；\n"
			+ "回复：14、查看我的策略；\n"
			+ "回复：15、查看我的投顾；\n"
			+ "回复：16、查看我的工具；\n"
			+ "回复：17、申请工具；\n"
			+ "回复：23、查看精品策略；\n"
			+ "回复：22、查看投顾汇；\n"
			+ "回复：21、查看今日汇；\n";

    public static final String CUSTOMER = "会员";

    public static final String MANAGER = "机构用户";
    /**
     * 回复绑定成功
     */
    public static final String REPLY_SUCCESS = "恭喜，您已成功绑定%s账号%s。";

    public static final String REPLY_FAIL = "微信异常，本次绑定%s账号失败，请稍后重试。";

    public static final String REPLY_OPENID_EXISTS = "当前微信已绑定其它%s账号%s，不能再次绑定。";

    public static final String REPLY_ACCOUNT_EXISTS = "%s账号：%s，已绑定其它微信（%s），不能再次绑定。";

	/**
	 *
	* @Title: sendTextMsg
	* @Description: 发送文本信息
	* @param: @param toUserName
	* @param: @param fromUserName
	* @param: @param content
	* @param: @return
	* @return String
	* @author lijie
	* @throws
	 */
	public static String getTextMsg(String toUserName, String fromUserName, String content) {
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);
		text.setToUserName(fromUserName);
		text.setMsgType(MESSAGE_TEXT);
		text.setCreateTime(System.currentTimeMillis());
		text.setContent(content);
		return XmlUtil.textMessageToXml(text);
	}
	/**
	 *
	* @Title: getImageTextMsg
	* @Description: 图文信息
	* @param: @param toUserName
	* @param: @param fromUserName
	* @param: @param Articles
	* @param: @return
	* @return String
	* @author lijie
	* @throws
	 */
	public static String getImageTextMsg(String toUserName, String fromUserName,List<Article> articles){
		NewsMessage message = new NewsMessage();
		message.setFromUserName(toUserName);
		message.setToUserName(fromUserName);
		message.setMsgType(MESSAGE_NEWS);
		message.setCreateTime(System.currentTimeMillis());
		message.setArticleCount(articles.size());
		message.setArticles(articles);
		return XmlUtil.newsMessageToXml(message);
	}
}
