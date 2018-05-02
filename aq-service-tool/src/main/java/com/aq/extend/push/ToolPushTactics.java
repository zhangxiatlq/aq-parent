package com.aq.extend.push;

import org.springframework.beans.factory.annotation.Autowired;

import com.aq.core.lock.RedisDistributionLock;
import com.aq.extend.push.data.ToolPushData;
import com.aq.mapper.ToolPushMapper;
import com.aq.util.result.Result;
/**
 * 
 * @ClassName: ToolPush
 * @Description: 推送抽象接口
 * @author: lijie
 * @date: 2018年4月17日 上午10:02:24
 */
public abstract class ToolPushTactics {
	
	@Autowired
	protected ToolPushMapper toolPushMapper;
	
	@Autowired
	protected RedisDistributionLock lock;
	/**
	 * 
	* @Title: push  
	* @Description: 执行推送的方法 
	* @param: @param pdto
	* @param: @param bindId
	* @param: @param dto
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	public abstract Result push(ToolPushData data);
	
	/**
	 * 
	* @Title: direction  
	* @Description: 方向值转换(数据库转换成python对应值，可参考Pushdirection枚举)
	* @param: @param direction
	* @param: @return
	* @return String
	* @author lijie
	* @throws
	 */
	protected String direction(String direction) {
		if ("0".equals(direction)) {
			direction = "3";
		} else if ("3".equals(direction)) {
			direction = "4";
		}
		return direction == null ? "" : direction;
	}
}
