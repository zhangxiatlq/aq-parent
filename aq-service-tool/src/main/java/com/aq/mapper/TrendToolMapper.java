package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.QueryToolDTO;
import com.aq.facade.entity.TrendTool;
import com.aq.facade.vo.TrendToolVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 
 * @ClassName: TrendToolMapper
 * @Description: 趋势化工具
 * @author: lijie
 * @date: 2018年1月20日 下午3:25:12
 */
public interface TrendToolMapper extends BaseMapper<TrendTool> {
	/**
	 * 
	 * @Title: selectTrendTools
	 * @author: lijie 
	 * @Description: TODO
	 * @param dto
	 * @return
	 * @return: List<TrendToolVO>
	 */
	List<TrendToolVO> selectTrendTools(QueryToolDTO dto);
	/**
	 * 
	* @Title: updateByIds  
	* @Description:批量修改状态  
	* @param @param updateToolIds
	* @param @param operId
	* @param @param status    参数  
	* @return void    返回类型  
	* @throws
	 */
	void updateByIds(@Param("list") List<Integer> list, @Param("operId") Integer operId, @Param("status") Byte status);
	/**
	 * 
	* @Title: selectToolByIds  
	* @Description：根据多个ID查询列表数据
	* @param @param ids
	* @param @return    参数  
	* @return List<TrendTool>    返回类型  
	* @throws
	 */
	List<TrendTool> selectToolByIds(List<Integer> list);
	/**
	 * 
	* @Title: selectBindByOwnApplyTrendIds  
	* @Description: TODO 
	* @param: @param toolIds
	* @param: @return
	* @return List<Integer>
	* @author lijie
	* @throws
	 */
	List<Integer> selectBindByOwnApplyTrendIds(@Param("list") List<Integer> list);

	/**
	 * @auth: zhangxia
	 * @desc: 通过ids批量查询卖点工具是否存在
	 * @methodName: selectListTrendToolsByIds
	 * @params: [list]
	 * @return: java.util.List<java.lang.Integer>
	 * @createTime: 2018/4/25 17:23
	 * @version:2.1.6
	 */
	List<Integer> selectListTrendToolsByIds(@Param("list") List<Integer> list);

}
