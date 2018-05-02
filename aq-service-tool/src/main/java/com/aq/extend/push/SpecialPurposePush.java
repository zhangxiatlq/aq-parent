package com.aq.extend.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aq.extend.IndependentAffairService;
import com.aq.extend.push.data.ToolPushData;
import com.aq.facade.contant.ToolStatusEnum.Pushdirection;
import com.aq.facade.entity.ToolPush;
import com.aq.util.date.DateUtil;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: SpecialPurposePush
 * @Description: 专用工具推送
 * @author: lijie
 * @date: 2018年4月17日 上午11:37:50
 */
@Slf4j
@Component("specialPurposePush")
public class SpecialPurposePush extends ToolPushTactics {

	@Autowired
	private IndependentAffairService affairService;

	@Override
	public Result push(ToolPushData data) {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		// 专用工具：只推看多、看空
		final String direction = data.getDto().getDirection();
		boolean flag = (Pushdirection.PURCHASE.getDirection().equals(direction)
				|| Pushdirection.SELL_OUT.getDirection().equals(direction));
		if (!flag) {
			result.setMessage("专用工具：只推看多、看空");
			return result;
		}
		String lockKey = "PUSH_TOOL_" + data.getBindId();
		lock.lock(lockKey);
		try {
			// 得到最新一条推送数据
			ToolPush toolPush = toolPushMapper.selectToolPushByNewest(data.getBindId());
			if (null != toolPush) {
				// 如果是当天已推送过则不再推送（就算是交叉，也要等到第二天再推送）
				if (DateUtil.isToday(toolPush.getCreateTime())) {
					log.info("专用工具当天已推送过则不再推送（就算是交叉，也要等到第二天再推送）");
					result.setMessage("专用工具当天已推送过则不再推送（就算是交叉，也要等到第二天再推送）");
					return result;
				}
				// 如果是看空则不用管，如果是看多则判断是否是持续看多
				if (direction.equals(Pushdirection.PURCHASE.getDirection())) {
					// 如果推送过的数据方向是看多则不推送（标识持续看多）
					if (Pushdirection.PURCHASE.getStatus().equals(toolPush.getDirection())) {
						log.info("专用工具推送已推送过看多，不再推送看多");
						result.setMessage("专用工具推送已推送过看多，不再推送看多");
						return result;
					}
				}
			}
		} finally {
			lock.unLock(lockKey);
		}
		return affairService.handlePush(data);
	}
}
