package com.aq.facade.service;

import com.aq.util.result.Result;
/**
 * 
 * @ClassName: ILuckDrawService
 * @Description: 幸运抽奖
 * @author: lijie
 * @date: 2018年1月20日 下午11:28:23
 */
public interface ILuckDrawService {
	
	/**
	 * 
	 * @Title: startLuckDraw
	 * @author: lijie 
	 * @Description: 抽奖开始
	 * @param userId
	 * @return
	 * @return: Result
	 */
	Result startLuckDraw(Integer userId,String prizeCode);
	/**
	 * 
	 * @Title: addLuckDraw
	 * @author: lijie 
	 * @Description: 添加抽奖信息
	 * @param userId
	 * @return
	 * @return: Result
	 */
	Result addLuckDraw(Integer userId);
	/**
	 * 
	 * @Title: receivePrize
	 * @author: lijie 
	 * @Description: 领取奖励
	 * @param userId
	 * @param id
	 * @return
	 * @return: Result
	 */
	Result receivePrize(Integer userId,Integer id);
	/**
	 * 
	 * @Title: getLuckDrawNum
	 * @author: lijie 
	 * @Description: 查询领取次数
	 * @param userId
	 * @return
	 * @return: Result
	 */
	Result getLuckDrawNum(Integer userId);
	/**
	 * 
	 * @Title: vipExpireGive
	 * @author: lijie 
	 * @Description: vip到期赠送（供定时任务调度）
	 * @param userId
	 * @return
	 * @return: Result
	 */
	Result vipExpireGive();
}
