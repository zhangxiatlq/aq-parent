package com.aq.core.wechat.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
/**
 *
 * @ClassName: MessageCueEnum
 * @Description: 消息提示枚举
 * @author: lijie
 * @date: 2018年3月21日 下午10:03:21
 */
@Getter
@AllArgsConstructor
public enum MessageCueEnum {

	ONE("11","注册会员账号","userLogon.html","https://aq-images.oss-cn-shenzhen.aliyuncs.com/wechat/20180323/20180323103106737050548.jpg","点击免费注册成为AQ会员"),
	TWO("12","绑定会员账号","userRegister.html","https://aq-images.oss-cn-shenzhen.aliyuncs.com/wechat/20180323/20180323103135993696116.jpg","注册会员后，点击绑定推送即可获得信息实时推送"),
	THREE("13","个人信息","vipMessage.html","https://aq-images.oss-cn-shenzhen.aliyuncs.com/wechat/20180322/20180322180143418317962.jpg","点击查看个人信息及VIP到期日，支持在线续费"),
	FOUR("14","我的策略","strategyList.html","https://aq-images.oss-cn-shenzhen.aliyuncs.com/wechat/20180322/20180322175702063634336.jpg","点击查看已购买的精品策略"),
	FIVE("15","我的投顾","adviserListByMine.html","https://aq-images.oss-cn-shenzhen.aliyuncs.com/wechat/20180322/20180322175816342346409.jpg","点击查看已购买的投顾组合"),
	SIX("16","我的工具","toolsByMine.html","https://aq-images.oss-cn-shenzhen.aliyuncs.com/wechat/20180322/20180322175754057433735.jpg","点击查看已使用的量化工具"),
	SEVEN("17","申请工具","toolsApply.html","https://aq-images.oss-cn-shenzhen.aliyuncs.com/wechat/20180322/20180322180233546218949.jpg","点击可自主申请需要体验或使用的量化工具"),
	EIGHT("23","精品策略","strategyExquisiteList.html","https://aq-images.oss-cn-shenzhen.aliyuncs.com/wechat/20180322/20180322180254283461478.jpg","点击查看AQ所有精品策略"),
	NINE("22","投顾汇","adviserList.html","https://aq-images.oss-cn-shenzhen.aliyuncs.com/wechat/20180322/20180322180219811988798.jpg","点击查看AQ所有投顾组合"),
	TEN("21","今日汇","adivserArticle.html","https://aq-images.oss-cn-shenzhen.aliyuncs.com/wechat/20180322/20180322180204378887693.jpg","点击查看AQ今日汇投顾发布的所有信息");

	private String id;
	/**
	 * 备注
	 */
	private String title;
	/**
	 * 点击跳转url
	 */
	private String url;
	/**
	 * 图片url
	 */
	private String picUrl;
	/**
	 * 描述
	 */
	private String description;

	public static MessageCueEnum info(String id) {
		MessageCueEnum result = null;
		if (StringUtils.isNotBlank(id)) {
			MessageCueEnum[] ms = MessageCueEnum.values();
			for (MessageCueEnum m : ms) {
				if (id.equals(m.getId())) {
					result = m;
					break;
				}
			}
		}
		return result;
	}
}
