package com.aq.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.aq.facade.dto.TrendToolDTO;
/**
 * 
 * @ClassName: IToolImportService
 * @Description: 工具导入数据服务
 * @author: lijie
 * @date: 2018年4月16日 下午12:36:55
 */
public interface IToolImportService {
	/**
	 * 
	* @Title: getTrendToolImportDatas  
	* @Description: 趋势量化工具导入 
	* @param: @param request
	* @param: @return
	* @return List<TrendToolDTO>
	* @author lijie
	* @throws
	 */
	List<TrendToolDTO> getTrendToolImportDatas(HttpServletRequest resuest);
}
