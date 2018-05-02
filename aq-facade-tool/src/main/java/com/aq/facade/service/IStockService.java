package com.aq.facade.service;

import com.aq.util.result.Result;

/**
 * 
 * @ClassName: IStockService
 * @Description: 股票相关服务
 * @author: lijie
 * @date: 2018年1月20日 下午6:42:57
 */
public interface IStockService {
	/**
	 * 
	 * @Title: getStockInfo
	 * @author: lijie 
	 * @Description: 根据股票代码得到股票信息
	 * @param stockCode
	 * @return
	 * @return: Result
	 */
	Result getStockInfo(String stockCode);
}
