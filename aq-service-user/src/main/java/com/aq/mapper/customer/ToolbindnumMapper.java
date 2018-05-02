package com.aq.mapper.customer;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.customer.Toolbindnum;
/**
 * 
 * @ClassName: ToolbindnumMapper
 * @Description: 客户工具数据量设置
 * @author: lijie
 * @date: 2018年2月10日 下午3:29:24
 */
@Repository
public interface ToolbindnumMapper extends BaseMapper<Toolbindnum> {
	/**
	 * 
	* @Title: updateToolBindNumByCusId  
	* @Description: 批量修改客户工具推荐数据 
	* @param @param customerId
	* @param @return    参数  
	* @return int    返回类型  
	* @throws
	 */
	int updateToolBindNumByCusId(@Param("list") List<Integer> list, @Param("num") Integer num);
	
}