package com.aq.facade.contant;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 * @ClassName: ToolStatusEnum
 * @Description: 工具状态
 * @author: lijie
 * @date: 2018年1月20日 下午3:29:24
 */
public enum ToolStatusEnum {
	
	NORMAL((byte)1,"正常"),
	TO_BE_CONFIRMED((byte)0,"待确定 "),
	
	NOT_DELETED((byte)0,"未删除 "),
	UNDELETED((byte)1,"已删除 "),
	
	CUSTOMER((byte)2,"客户 "), // 0
	CUSTOMER_MANAGER((byte)3,"客户经理 "),// 1
	
	PUSH((byte)1,"是"),
	NO_PUSH((byte)0,"不推送"),
	
	SYNCHRO((byte)1,"已同步"),
	NOT_SYNCHRO((byte)0,"未同步"),
	
	;
	
	@AllArgsConstructor
	@Getter
	public static enum Pushdirection{
		/**
		 * 看多、买入
		 */
		PURCHASE((byte) 1, "1", "看多、买入"),
		/**
		 * 看空、卖出
		 */
		SELL_OUT((byte) 0, "3", "看空、卖出"),
		/**
		 * 持续看多
		 */
		CONTINUED_PURCHASE((byte) 2, "2", "持续看多"),
		/**
		 * 观望
		 */
		LOOK_ON((byte) 3, "4", "观望");
		/**
		 * 数据库状态
		 */
		private Byte status;
		/**
		 * python传入的值
		 */
		private String direction;
		
		private String desc;
	}
	
	@Getter
	private Byte status;
	@Getter
	private String desc;
	
	ToolStatusEnum(Byte status, String desc) {
		this.status = status;
		this.desc = desc;
	}
	/**
	 * 
	* @Title: getStatus  
	* @Description: 根据python方向得到数据库方向状态  
	* @param @param desc
	* @param @return    参数  
	* @return Byte    返回类型  
	* @throws
	 */
	public static Byte getStatus(String direction) {
		Byte status = null;
		if (StringUtils.isNotBlank(direction)) {
			Pushdirection[] ts = Pushdirection.values();
			for (Pushdirection t : ts) {
				if (direction.trim().equals(t.getDirection())) {
					status = t.getStatus();
					break;
				}
			}
		}
		Assert.notNull(status, "tool status is null");
		return status;
	}
	/**
	 * 
	* @Title: getDirection  
	* @Description: 根据数据库方向状态  得到python方向
	* @param: @param status
	* @param: @return
	* @return Byte
	* @author lijie
	* @throws
	 */
	public static String getDirection(Byte status) {
		String direction = null;
		if (null != status) {
			Pushdirection[] ts = Pushdirection.values();
			for (Pushdirection t : ts) {
				if (status.equals(t.getStatus())) {
					direction = t.getDirection();
					break;
				}
			}
		}
		Assert.notNull(direction, "tool direction is null");
		return direction;
	}
}
