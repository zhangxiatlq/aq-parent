package com.aq.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.ToolPush;
import com.aq.facade.vo.ToolPushVO;
/**
 * 
 * @ClassName: ToolPushMapper
 * @Description: 工具数据交付层
 * @author: lijie
 * @date: 2018年2月11日 下午9:13:34
 */
@Repository
public interface ToolPushMapper extends BaseMapper<ToolPush> {
	/**
	 * 
	* @Title: selectToolPushs  
	* @Description: 根据绑定ID查询推送记录  
	* @param @param bindId
	* @param @return    参数  
	* @return List<ToolPushVO>    返回类型  
	* @throws
	 */
	List<ToolPushVO> selectToolPushs(@Param("bindId") Integer bindId);
	/**
	 * 
	* @Title: selectToolPush  
	* @Description: 根据绑定ID查询最新推送的方向 
	* @param @param bindId
	* @param @param code
	* @param @return    参数  
	* @return int    返回类型  
	* @throws
	 */
	String selectToolPush(@Param("bindId") Integer bindId);
	/**
	 * 
	* @Title: selectToolPushByNewest  
	* @Description: 查询最新一条推送数据 
	* @param: @param bindId
	* @param: @return
	* @return ToolPush
	* @author lijie
	* @throws
	 */
	ToolPush selectToolPushByNewest(@Param("bindId") Integer bindId);
	/**
	 * 
	* @Title: selectToolPushBySellOut  
	* @Description: 查询当天是否已推送过看空数据 
	* @param: @param bindId
	* @param: @return
	* @return int
	* @author lijie
	* @throws
	 */
	int selectToolPushBySellOut(@Param("bindId") Integer bindId);
	/**
	 * 
	* @Title: selectToolPushByPurchase  
	* @Description: TODO 
	* @param: @param bindId
	* @param: @return
	* @return int
	* @author lijie
	* @throws
	 */
	int selectToolPushByPurchase(@Param("bindId") Integer bindId);
	
}