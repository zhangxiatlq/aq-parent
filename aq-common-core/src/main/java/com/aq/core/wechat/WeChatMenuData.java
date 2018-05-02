package com.aq.core.wechat;

import lombok.Data;

import java.util.List;
/**
 *
 * @ClassName: MenuData
 * @Description: 菜单数据
 * @author: lijie
 * @date: 2018年3月20日 上午11:05:30
 */
@Data
public class WeChatMenuData implements Comparable<WeChatMenuData>  {
	/**
	 * 菜单名称
	 */
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
	 * 操作项：url/素材ID
	 */
	private String option;
	/**
	 * 序号
	 */
	private int sort;
	/**
	 * 子菜单
	 */
	private List<WeChatMenuData> submenus;

	@Override
	public int compareTo(WeChatMenuData m) {
		if (this.sort > m.getSort()) {
			return 1;
		}
		if (this.sort < m.getSort()) {
			return -1;
		}
		return 0;
	}
}
