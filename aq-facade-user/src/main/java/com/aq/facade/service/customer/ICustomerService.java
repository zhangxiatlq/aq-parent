package com.aq.facade.service.customer;

import com.aq.facade.dto.AddAttentionRecordDTO;
import com.aq.facade.dto.SelectClientDTO;
import com.aq.facade.dto.UpdateClientDTO;
import com.aq.facade.dto.customer.*;
import com.aq.facade.dto.share.dto.LoginPasswordDTO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * 客户service
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
public interface ICustomerService {

    /**
     * 根据条件查询客户
     *
     * @param pageBean
     * @param queryCustomerDTO
     * @return com.aq.util.result.Result{@link com.aq.facade.vo.customer.QueryCustomerVO}
     * @author 郑朋
     * @create 2018/2/8 0008
     */
    Result getCustomerByPage(PageBean pageBean, QueryCustomerDTO queryCustomerDTO);

    /**
     * 根据条件查询客户(工具）
     *
     * @param pageBean
     * @param queryCustomerDTO
     * @return com.aq.util.result.Result{@link com.aq.facade.vo.customer.QueryCustomerVO}
     * @author 郑朋
     * @create 2018/2/8
     */
    Result getToolCustomerByPage(PageBean pageBean, QueryCustomerDTO queryCustomerDTO);


    /**
     * 新增客户（绑定客户）
     *
     * @param bindCustomerDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/8 0008
     */
    Result bindCustomer(BindCustomerDTO bindCustomerDTO);


    /**
     * 导入客户
     *
     * @param importCustomerDTO
     * @return com.aq.util.result.Result {@link com.aq.facade.vo.customer.ImportCustomerVO}
     * @author 郑朋
     * @create 2018/2/8 0008
     */
    Result importCustomer(ImportCustomerDTO importCustomerDTO);


    /**
     * 修改客户
     *
     * @param updateBindCustomerDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/8 0008
     */
    Result updateBindCustomer(UpdateBindCustomerDTO updateBindCustomerDTO);

    /**
     * @param dto
     * @return
     * @Title: addCustomer
     * @author: lijie
     * @Description: 注册用户
     * @return: Result
     */
    Result registerCustomer(AddCustomerDTO dto);

    /**
     * @param dto
     * @return
     * @Title: customerLogin
     * @author: lijie
     * @Description: 客户登录
     * @return: Result
     */
    Result customerLogin(CustomerLoginDTO dto);

    /**
     * @param openId
     * @return
     * @Title: getCustomerByOpenId
     * @author: lijie
     * @Description: 根据openId获取客户信息
     * @return: Result
     */
    Result getCustomerByOpenId(String openId);
    /**
     * 
     * @Title: getCustomerByAccount
     * @author: lijie 
     * @Description: 根据手机号/用户名获取客户信息
     * @param account
     * @return: Result
     */
    Result getCustomerByAccount(String account);
    /**
     * 
    * @Title: updateCustomerLoginPwd  
    * @Description: 客户修改/找回密码 
    * @param @param dto
    * @param @return    参数  
    * @return Result    返回类型  
    * @throws
     */
    Result updateCustomerLoginPwd(LoginPasswordDTO dto);
    /**
     * 
    * @Title: updateCustomer  
    * @Description: 修改客户信息
    * @param @param dto
    * @param @return    参数  
    * @return Result    返回类型  
    * @throws
     */
    Result updateCustomer(UpdateCustomerDTO dto);

    /**
     * 
    * @Title: updateCustomerOpenId  
    * @Description: 绑定客户openId  
    * @param @param account
    * @param @param openId
    * @param @return    参数  
    * @return Result    返回类型  
    * @throws
     */
	Result updateCustomerOpenId(String account, String openId);
	/**
	 * 
	* @Title: timingUpdateCustomerVip  
	* @Description: 定时修改vip到期状态  
	* @param @return    参数  
	* @return Result    返回类型  
	* @throws
	 */
	Result timingUpdateCustomerVip();

    /**
     * 校验 openId 是否被注册
     *
     * @param openId
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/24 0024
     */
	Result checkOpenId(String openId);
	/**
	 * 
	* @Title: getAllOpend  
	* @Description:获取所有openID
	* @param @return    参数  
	* @return Result    返回类型  
	* @throws
	 */
	Result getAllOpend();

    /**
     * @Creater: zhangxia
     * @description：后台翻页获取客户列表
     * @methodName: getClientListByPage
     * @params: [pageBean, selectClientDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 16:47 2018-2-28
     */
    Result getCustomerListByPage(PageBean pageBean, SelectClientDTO selectClientDTO);


    /**
     * @Creater: zhangxia
     * @description：后台获取客户详情信息
     * @methodName: getCustomerDetail
     * @params: [selectClientDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 10:19 2018-3-1
     */
    Result getCustomerDetail(SelectClientDTO selectClientDTO);

    /**
     * @author: 张霞
     * @description：客户编辑信息
     * @createDate: 11:13 2018/1/21
     * @param updateClientDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    Result editClient(UpdateClientDTO updateClientDTO);

    /**
     * @author: 张霞
     * @description：客户重置密码
     * @createDate: 14:54 2018/1/22
     * @param
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    Result resetPWD(UpdateClientDTO updateClientDTO);

    /**
     * @author: zhangxia
     * @desc: 定时记录每天微信的关注客户数据
     * @params: [updateClientDTO]
     * @methodName:resetPWD
     * @date: 2018/3/6 0006 下午 15:52
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result recordAttention(AddAttentionRecordDTO addAttentionRecordDTO);
    /**
      * @author: zhangxia
      * @desc:定时器每天更新微信关注客户状态
      * @params: []
      * @methodName:updateCustomerAttention
      * @date: 2018/3/6 0006 下午 12:45
      * @return: com.aq.util.result.Result
      * @version:2.1.2
      */
   Result updateCustomerAttention();

    /**
     * @author: zhangxia
     * @desc: 每天定时统计每个客户经理下客户昨天的关注微信情况记录
     * @params: []
     * @methodName:statisticsAttentionRecord
     * @date: 2018/3/6 0006 下午 16:54
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
   Result statisticsAttentionRecord();
   /**
    * 
   * @Title: updateWeChatAttention  
   * @Description: 修改关注状态 
   * @param: @param openId
   * @param: @param status
   * @param: @return
   * @return Result
   * @author lijie
   * @throws
    */
   Result updateWeChatAttention(String openId,Byte status);
}
