package com.aq.mapper.customer;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.SelectClientDTO;
import com.aq.facade.dto.customer.CustomerDTO;
import com.aq.facade.dto.customer.QueryCustomerDTO;
import com.aq.facade.entity.customer.Customer;
import com.aq.facade.vo.ClientDetailVO;
import com.aq.facade.vo.ClientListVO;
import com.aq.facade.vo.customer.CustomerInfoVO;
import com.aq.facade.vo.customer.QueryCustomerVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户  mapper
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Repository
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 根据条件查询客户
     * @param queryCustomerDTO
     * @return java.util.List<com.aq.facade.vo.customer.QueryCustomerVO>
     * @author 郑朋
     * @create 2018/2/8
     */
    List<QueryCustomerVO> selectCustomerList(QueryCustomerDTO queryCustomerDTO);

	/**
	 * 根据条件查询客户 -- 工具
	 *
	 * @param queryCustomerDTO
	 * @return java.util.List<com.aq.facade.vo.customer.QueryCustomerVO>
	 * @author 郑朋
	 * @create 2018/4/27
	 */
	List<QueryCustomerVO> selectCustomerListTool(QueryCustomerDTO queryCustomerDTO);
   /**
    * 
    * @Title: checkCustomerExists
    * @author: lijie 
    * @Description: 校验客户数据是否唯一
    * @param selectOne
    * @return
    * @return: List<Customer>
    */
	List<Customer> checkCustomerExists(Customer selectOne);
	/**
	 * 
	 * @Title: selectCustomerInfoByopenId
	 * @author: lijie 
	 * @Description: 根据openId查询客户信息数据
	 * @param openId
	 * @return
	 * @return: CustomerInfoVO
	 */
	CustomerInfoVO selectCustomerInfoByopenId(@Param("openId") String openId);
	/**
	 * 
	* @Title: updateVipExpire  
	* @Description: 修改vip到期状态 
	* @param @return    参数  
	* @return int    返回类型  
	* @throws
	 */
	int updateVipExpire();

	/**
	 * 根据手机号查询用户信息
	 *
	 * @param list
	 * @return java.util.List<com.aq.facade.vo.customer.CustomerInfoVO>
	 * @author 郑朋
	 * @create 2018/2/24 0024
	 */
	List<CustomerInfoVO> selectCustomerInfoList(@Param(value = "list") List<CustomerDTO> list);
	/**
	 * 
	* @Title: selectExpireTime  
	* @Description: 查询到期前的vip客户数据  
	* @param @return    参数  
	* @return List<Customer>    返回类型  
	* @throws
	 */
	List<Customer> selectExpireTime();

	/**
	 * @Creater: zhangxia
	 * @description：翻页获取客户列表
	 * @methodName: getClientListByPage
	 * @params: [selectClientDTO]
	 * @return: java.util.List<com.aq.facade.vo.ClientListVO>
	 * @createTime: 16:51 2018-2-28
	 */
	List<ClientListVO> getClientListByPage(SelectClientDTO selectClientDTO);

	/**
	 * @Creater: zhangxia
	 * @description：后台获取客户详情信息
	 * @methodName: getClientDetail
	 * @params: [selectClientDTO]
	 * @return: com.aq.facade.vo.ClientDetailVO
	 * @createTime: 10:21 2018-3-1
	 */
	ClientDetailVO getClientDetail(SelectClientDTO selectClientDTO);
	/**
	 *
	* @Title: updateOpenIdIsNull
	* @Description: TODO
	* @param: @param telphone
	* @return void
	* @author lijie
	* @throws
	 */
	void updateOpenIdIsNull(String telphone);
}