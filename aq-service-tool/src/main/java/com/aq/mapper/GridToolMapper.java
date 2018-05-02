package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.QueryToolDTO;
import com.aq.facade.entity.GridTool;
import com.aq.facade.vo.GridToolVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 
 * @ClassName: GridToolMapper
 * @Description: 网格工具
 * @author: lijie
 * @date: 2018年1月20日 下午3:24:10
 */
public interface GridToolMapper extends BaseMapper<GridTool> {
	/**
	 * 
	 * @Title: selectGridTools
	 * @author: lijie 
	 * @Description: 查询工具信息数据
	 * @param userId
	 * @return
	 * @return: List<GridToolVO>
	 */
	List<GridToolVO> selectGridTools(QueryToolDTO dto);
	/**
	 * 
	* @Title: updateByIds  
	* @Description: 批量修改状态  
	* @param @param list
	* @param @param operId
	* @param @param status    参数  
	* @return void    返回类型  
	* @throws
	 */
	int updateByIds(@Param("list") List<Integer> list, @Param("operId") Integer operId, @Param("status") Byte status);
	/**
	 * 
	* @Title: selectBindByOwnApplyGridIds  
	* @Description: 查询自己 
	* @param: @param toolIds
	* @param: @return
	* @return List<Integer>
	* @author lijie
	* @throws
	 */
	List<Integer> selectBindByOwnApplyGridIds(@Param("list") List<Integer> list);

	/**
	 * @auth: zhangxia
	 * @desc: 批量查询网格工具是否存在
	 * @methodName: selectListToolsByIds
	 * @params: [list]
	 * @return: java.util.List<java.lang.Integer>
	 * @createTime: 2018/4/25 16:59
	 * @version:2.1.6
	 */
	List<Integer> selectListGridToolsByIds(@Param("list") List<Integer> list);


}
