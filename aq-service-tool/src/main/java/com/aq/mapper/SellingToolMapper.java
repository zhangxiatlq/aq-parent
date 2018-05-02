package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.QueryToolDTO;
import com.aq.facade.entity.SellingTool;
import com.aq.facade.vo.SellingToolVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 
 * @ClassName: SellingToolMapper
 * @Description: 卖点工具
 * @author: lijie
 * @date: 2018年1月20日 下午3:24:21
 */
public interface SellingToolMapper extends BaseMapper<SellingTool>{
	/**
	 * 
	 * @Title: selectSellingTools
	 * @author: lijie 
	 * @Description: 查询列表数据
	 * @param userId
	 * @return
	 * @return: List<SellingToolVO>
	 */
	List<SellingToolVO> selectSellingTools(QueryToolDTO dto);
	/**
	 * 
	* @Title: updateByIds  
	* @Description: 批量修改状态 
	* @param @param list
	* @param @param operId
	* @param @param status
	* @param @return    参数  
	* @return int    返回类型  
	* @throws
	 */
	int updateByIds(@Param("list") List<Integer> list, @Param("operId") Integer operId, @Param("status") Byte status);
	/**
	 * 
	* @Title: selectBindByOwnApplyGridIds  
	* @Description: TODO 
	* @param: @param toolIds
	* @param: @return
	* @return List<Integer>
	* @author lijie
	* @throws
	 */
	List<Integer> selectBindByOwnApplySellingIds(@Param("list") List<Integer> list);

	/**
	 * @auth: zhangxia
	 * @desc: 通过ids批量查询卖点工具是否存在
	 * @methodName: selectListSellingToolsByIds
	 * @params: [list]
	 * @return: java.util.List<java.lang.Integer>
	 * @createTime: 2018/4/25 17:14
	 * @version:2.1.6
	 */
	List<Integer> selectListSellingToolsByIds(@Param("list") List<Integer> list);

}
