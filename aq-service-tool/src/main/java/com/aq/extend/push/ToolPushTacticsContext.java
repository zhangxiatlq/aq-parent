package com.aq.extend.push;

import com.aq.extend.push.data.ToolPushData;
import com.aq.facade.contant.ToolTypeEnum;
import com.aq.util.bean.SpringUtil;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
/**
 * 
 * @ClassName: ToolPushTacticsContext
 * @Description: 推送（采用简单工厂与策略模式结合）
 * @author: lijie
 * @date: 2018年4月17日 上午11:29:35
 */
public class ToolPushTacticsContext {

	private ToolPushTactics toolPushTactics;

	public ToolPushTacticsContext(ToolTypeEnum type) {
		switch (type) {
		case ROUTINE:
			toolPushTactics = SpringUtil.getBeanByName("routinePush", RoutinePush.class);
			break;
		case SPECIAL_PURPOSE:
			toolPushTactics = SpringUtil.getBeanByName("specialPurposePush", SpecialPurposePush.class);
			break;
		default:
			break;
		}
	}
	/**
	 * 
	* @Title: push  
	* @Description: 执行推送 
	* @param: @param data
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	public Result push(ToolPushData data) {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		if (null != toolPushTactics) {
			result = toolPushTactics.push(data);
		} else {
			result.setMessage("没有获取到推送相关（策略）对象");
		}
		return result;
	}
}
