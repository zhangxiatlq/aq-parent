package com.aq.facade.service;

import java.util.List;

import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * 
 * @ClassName: IAdviserPositionService
 * @Description: 投顾当前持仓
 * @author: lijie
 * @date: 2018年3月9日 下午3:19:54
 */
public interface IAdviserPositionService {
	/**
	 * 
	* @Title: getAdviserPositionByPage  
	* @Description: 查询投顾资产/持仓信息数据 
	* @param: @param isQueryTrade 是否查询资产：0、否 1、是
	* @param: @param adviserId 投顾ID
	* @param: @param pageBean 分页信息
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	Result getAdviserPositionByPage(Byte isQueryTrade,Integer adviserId, PageBean pageBean);
	/**
	 * 
	* @Title: getRealTimeAdviserPosition  
	* @Description: 查询投顾资产/持仓信息实时数据  
	* @param: @param adviserId
	* @param: @param codes
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	Result getRealTimeAdviserPosition(Integer adviserId,List<String> codes);
}
