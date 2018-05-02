package com.aq.core.wechat.constant;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.aq.core.config.properties.WeChatCoreProperties;
import com.aq.core.wechat.WeChatMenuData;
import com.aq.util.bean.SpringUtil;
import com.esotericsoftware.minlog.Log;

import lombok.AllArgsConstructor;
import lombok.Getter;
/**
 * 
 * @ClassName: WheChatMenuTypeEnum
 * @Description: 微信菜单类型枚举
 * @author: lijie
 * @date: 2018年3月19日 下午4:40:01
 */
public enum WheChatMenuEnum {
	
	MENU {

		@Override
		public List<WeChatMenuData> menus() {
			List<WeChatMenuData> result = new ArrayList<>();
			List<MenuConfigEnum> menus = MenuConfigEnum.queryMenus();
			if (CollectionUtils.isNotEmpty(menus)) {
				WeChatMenuData data;
				List<WeChatMenuData> nexts;
				for (MenuConfigEnum m : menus) {
					if ("0".equals(m.getTopKey())) {
						data = initWeChatMenuData(m);
						nexts = nextMenus(m.getKey(), menus);
						Collections.sort(nexts);
						data.setSubmenus(nexts);
						result.add(data);
					}
				}
				Collections.sort(result);
			}
			return result;
		}

	};
	
	private static final WeChatCoreProperties WECHAT_CORE = SpringUtil.getBeanByClass(WeChatCoreProperties.class);
	
	private static List<WeChatMenuData> nextMenus(String topKey, List<MenuConfigEnum> menus) {
		List<WeChatMenuData> result = new ArrayList<>();
		WeChatMenuData menu;
		for (MenuConfigEnum m : menus) {
			if (topKey.equals(m.getTopKey())) {
				menu = initWeChatMenuData(m);
				menu.setSubmenus(nextMenus(m.getKey(), menus));
				result.add(menu);
			}
		}
		return result;
	}

	private static WeChatMenuData initWeChatMenuData(MenuConfigEnum menu) {
		WeChatMenuData data = new WeChatMenuData();
		data.setName(menu.getName());
		data.setKey(menu.getKey());
		data.setType(menu.getType());
		if ("view".equals(menu.getType()) && menu.isReplace) {
			data.setOption(WECHAT_CORE.getJumpAuthorize() + viewUrl(menu.getOption()));
		} else {
			data.setOption(menu.getOption());
		}
		data.setSort(menu.getSort());
		return data;
	}

	public static String viewUrl(String option) {
		String viewUrl = WECHAT_CORE.getViewUrl() + option;
		try {
			viewUrl = URLEncoder.encode(viewUrl, "utf-8");
		} catch (UnsupportedEncodingException e) {
			Log.error("url编码错误", e);
		}
		return viewUrl;
	}

	public abstract List<WeChatMenuData> menus();

	/**
	 * 
	 * @ClassName: DefMenuEnum
	 * @Description: 默认菜单
	 * @author: lijie
	 * @date: 2018年3月19日 下午5:28:21
	 */
	@Getter
	@AllArgsConstructor
	protected enum MenuConfigEnum {

		// 共用菜单
		/**
		 * 精彩回顾
		 */
		REVIEW("精彩回顾", "review", "view", "main_menu_three", 3, "http://mp.weixin.qq.com/mp/homepage?__biz=MzU0NTI4MDQ5Nw==&hid=1&sn=c20d0174ebea17d84995505a10bac58c&scene=18#wechat_redirect", false),
		/**
		 * AQ量化答疑:
		 */
		QUESTION("AQ量化答疑", "question", "media_id", "main_menu_three", 2, "6XgQe8kqAu-NP-mEO2VaI0KdF4EmzrKdxMdwrfWjcpM", true),
		/**
		 * AQ使用说明:
		 */
		EXPLAIN("AQ使用说明", "explain", "media_id", "main_menu_three", 1, "6XgQe8kqAu-NP-mEO2VaIy5-_4LcT-ZTfeLAsJ514ds", true),
		/**
		 * 量化课堂
		 */
		DEF_CLASSROOM("量化课堂", "main_menu_three", "click", "0", 3, "", false),
		/**
		 * 量投顾
		 */
		ADVISER("量投顾", "adviser", "view", "main_menu_two", 2, "adviserList.html", true),
		/**
		 * 策略精
		 */
		STRATEGY("策略精", "strategy", "view", "main_menu_two", 1, "strategyExquisiteList.html", true),
		/**
		 * 今日汇
		 */
		CONSULT("今日汇", "strategy", "view", "main_menu_two", 3, "adivserArticle.html", true),
		/**
		 * 量投汇
		 */
		QUANTITY_DEPARTMENT("量投汇", "main_menu_two", "click", "0", 2, "", false),

		// 个性化菜单
		/**
		 * 申请工具
		 */
		APPLY_TOOL("申请工具", "apply_tool", "view", "con_main_menu_one", 1, "toolsApply.html", true),
		/**
		 * 我的工具
		 */
		MY_TOOL("我的工具", "my_tool", "view", "con_main_menu_one", 2, "toolsByMine.html", true),
		/**
		 * 我的投顾
		 */
		MY_ADVISER("我的投顾", "my_adviser", "view", "con_main_menu_one", 3, "adviserListByMine.html", true),
		/**
		 * 我的策略
		 */
		MY_STRATEGY("我的策略", "my_strategy", "view", "con_main_menu_one", 4, "strategyList.html", true),
		/**
		 * 个人信息
		 */
		PERSONAL_INFO("个人信息", "personal_info", "view", "con_main_menu_one", 5, "vipMessage.html", true),
		/**
		 * 我的量化
		 */
		CLASSROOM("我的量化", "con_main_menu_one", "click", "0", 1, "", false);
		;
		private String name;
		/**
		 * key
		 */
		private String key;
		/**
		 * 类型
		 */
		private String type;
		/**
		 * 上级菜单key
		 */
		private String topKey;
		/**
		 * 排序序号
		 */
		private int sort;
		/**
		 * option
		 */
		private String option;
		/**
		 * 是否替换url
		 */
		private boolean isReplace;
		/**
		 * 
		* @Title: queryMenus  
		* @Description: 查询菜单数据 
		* @param: @param isConditional
		* @param: @return
		* @return List<MenuConfigEnum>
		* @author lijie
		* @throws
		 */
		public static List<MenuConfigEnum> queryMenus() {
			List<MenuConfigEnum> result = new ArrayList<>();
			// 第一个菜单
			result.add(MenuConfigEnum.CLASSROOM);
			result.add(MenuConfigEnum.PERSONAL_INFO);
			result.add(MenuConfigEnum.MY_STRATEGY);
			result.add(MenuConfigEnum.MY_TOOL);
			result.add(MenuConfigEnum.MY_ADVISER);
			result.add(MenuConfigEnum.APPLY_TOOL);
			// 第二个菜单
			result.add(MenuConfigEnum.QUANTITY_DEPARTMENT);
			result.add(MenuConfigEnum.CONSULT);
			result.add(MenuConfigEnum.STRATEGY);
			result.add(MenuConfigEnum.ADVISER);
			// 第三个菜单
			result.add(MenuConfigEnum.DEF_CLASSROOM);
			result.add(MenuConfigEnum.EXPLAIN);
			result.add(MenuConfigEnum.QUESTION);
			result.add(MenuConfigEnum.REVIEW);
			return result;
		}
	}
}
