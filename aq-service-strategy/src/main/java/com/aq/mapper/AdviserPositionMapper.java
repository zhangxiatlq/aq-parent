package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.AdviserPosition;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 投顾持仓mapper
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Repository
public interface AdviserPositionMapper extends BaseMapper<AdviserPosition> {
	/**
	 * 
	* @Title: selectAdviserPositionByPage  
	* @Description: 根据投顾ID 查询当前持仓列表数据 
	* @param @param adviserId
	* @param @return    参数  
	* @return List<AdviserPosition>    返回类型  
	* @throws
	 */
	List<AdviserPosition> selectAdviserPositionByPage(@Param("adviserId") Integer adviserId);

	/**
	 * 修改持仓数量
	 *
	 * @param adviserPosition
	 * @return int
	 * @author 郑朋
	 * @create 2018/3/16
	 */
	int updateAdviserPosition(AdviserPosition adviserPosition);

	/**
	 * 查询持仓数量为0的所有数据
	 *
	 * @param
	 * @return java.util.List<com.aq.facade.entity.AdviserPosition>
	 * @author 郑朋
	 * @create 2018/3/16
	 */
	List<AdviserPosition> selectAdviserPositionZero();
    /**
     * 
    * @Title: selectAdviserPositionByIds  
    * @Description: 根据多个持仓id查询持仓信息 
    * @param: @param list
    * @param: @return
    * @return List<AdviserPosition>
    * @author lijie
    * @throws
     */
	List<AdviserPosition> selectAdviserPositionByIds(@Param("list") List<Integer> list);
}