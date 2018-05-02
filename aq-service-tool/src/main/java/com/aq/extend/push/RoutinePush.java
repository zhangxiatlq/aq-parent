package com.aq.extend.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aq.core.constant.ToolCategoryEnum;
import com.aq.extend.IndependentAffairService;
import com.aq.extend.push.data.ToolPushData;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: RoutinePush
 * @Description: 常规推送
 * @author: lijie
 * @date: 2018年4月17日 上午10:04:14
 */
@Slf4j
@Component("routinePush")
public class RoutinePush extends ToolPushTactics {

	@Autowired
	private IndependentAffairService affairService;
	
	@Override
	public Result push(ToolPushData data) {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		if (data.getType() == ToolCategoryEnum.SELLING || data.getType() == ToolCategoryEnum.TREND) {
			String lockKey = "PUSH_TOOL_" + data.getBindId();
			lock.lock(lockKey);
			try {
				String direction = toolPushMapper.selectToolPush(data.getBindId());
				if (direction(direction).equals(data.getDto().getDirection())) {
					log.info("绑定ID：" + data.getBindId() + "：不符合交叉推送,暂不推送");
					result.setMessage("不符合交叉推送,暂不推送");
					return result;
				}
			} finally {
				lock.unLock(lockKey);
			}
		}
		return affairService.handlePush(data);
	}

}
