package com.aq.core.wechat;

import com.aq.core.config.properties.WeChatCoreProperties;
import com.aq.util.bean.SpringUtil;

/**
 * 
 * @ClassName: WeChatConfig
 * @Description: 得到微信签名相关配置
 * @author: lijie
 * @date: 2018年1月20日 下午2:19:49
 */
public class WeChatConfig {
	
	private static final WeChatCoreProperties WECHAT_CORE = SpringUtil.getBeanByClass(WeChatCoreProperties.class);
	
	/**
	 * 服务号应用密码：正式:fd3f7bac2b400651805b82f61f41bd3e
	 * 开发环境：f954953df8321614966dd3e2afd508da
	 * 测试环境：abe14e4cdbed951997680a4f84e3ef18
	 */
	public static final String SECRET = WECHAT_CORE.getSecret();
	/**
	 * 服务号应用ID 正式:wxc35edf919dc57299
	 * 开发环境：wx3ed063a4b31f3231
	 * 测试环境：wx94bc4d5a5d381251
	 */
	public static final String APPID = WECHAT_CORE.getAppid();
	
	public static final String GRANT_TYPE = "client_credential";
	
	public static final String TICKET_TYPE = "jsapi";
	/**
	 * js sdk需要的微信签名地址
	 */
	public static final String SIGN_URL = "http://mp.weixin.qq.com";
	/**
	 * access_token
	 */
	public static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token";
	/**
	 * ticket_url
	 */
	public static final String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
	/**
     * 商户号
     */
    public final static String MCH_ID = WECHAT_CORE.getMchid();
    /**
     * API密钥(商户密钥)
     */
    public final static String API_KEY = WECHAT_CORE.getApikey();
    /**
     * 微信推送url
     */
    public final static String PUSH_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send";
    /**
     * 微信网页授权地址
     */
    public final static String WEB_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize";
    /**
     * 微信网页授权回调地址
     */
    public final static String WEB_AUTHORIZE_BACKURL = "https://www.acequant.cn/wechat/authorize/code/";
    /**
     * 微信授权access token 请求地址
     */
    public final static String AUTHORIZE_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	/**
	 * 公众号可通过本接口来获取帐号的关注者列表,关注者列表由一串OpenID
	 */
	public final static String GET_USER_OPENIDS = "https://api.weixin.qq.com/cgi-bin/user/get";
	/**
	 * 查询微信用户信息连接
	 */
	public final static String GET_UEER_INGFO = "https://api.weixin.qq.com/cgi-bin/user/info";
	/**
	 * 二维码生成地址
	 */
	public static final String QR_CODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create";
	/**
	 * 生成二维码
	 */
	public static final String GEN_CODE_IMG = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
	/**
	 * 创建菜单地址
	 */
	public static final String CREATE_MENU = " https://api.weixin.qq.com/cgi-bin/menu/create";
	/**
	 * 查询菜单
	 */
	public static final String GET_MENU = "https://api.weixin.qq.com/cgi-bin/menu/get";
	/**
	 * 查询已创建的标签
	 */
	public static final String GET_TAGS = "https://api.weixin.qq.com/cgi-bin/tags/get";
	/**
	 * 创建标签
	 */
	public static final String CREATE_TAG = "https://api.weixin.qq.com/cgi-bin/tags/create";
	/**
	 * 个性化菜单
	 */
	public static final String CONDITIONAL_MENU = "https://api.weixin.qq.com/cgi-bin/menu/addconditional";
	/**
	 * 删除个性化菜单
	 */
	public static final String DEL_CONDITIONAL_MENU = "https://api.weixin.qq.com/cgi-bin/menu/delconditional";
	/**
	 * 删除菜单
	 */
	public static final String DEL_MENU = "https://api.weixin.qq.com/cgi-bin/menu/delete";
	/**
	 * 创建素材
	 */
	public static final String ADD_MATERIAL = "https://api.weixin.qq.com/cgi-bin/material/add_news";
}
