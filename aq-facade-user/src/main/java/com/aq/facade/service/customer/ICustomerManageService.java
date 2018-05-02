package com.aq.facade.service.customer;

import com.aq.facade.dto.UpdateManagerDTO;
import com.aq.facade.dto.manage.*;
import com.aq.facade.dto.share.dto.LoginPasswordDTO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * 
 * @ClassName: ICustomerManageService
 * @Description: 客户经理服务
 * @author: lijie
 * @date: 2018年2月9日 下午2:09:32
 */
public interface ICustomerManageService {
	/**
	 * 
	 * @Title: login
	 * @author: lijie 
	 * @Description: 客户经理登录
	 * @param dto
	 * @return: Result
	 */
	Result login(LoginDTO dto);
	/**
	 * 
	 * @Title: registerCustomer
	 * @author: lijie 
	 * @Description: 注册传输数据
	 * @param dto
	 * @return: Result
	 */
	Result registerManage(RegisterDTO dto);

	/**
	 * @Creater: zhangxia
	 * @description：后台添加客户经理
	 * @methodName: webRegisterManager
	 * @params: [webRegisterDTO]
	 * @return: com.aq.util.result.Result
	 * @createTime: 11:21 2018-2-26
	 */
	Result webRegisterManager(WebRegisterDTO webRegisterDTO);
	/**
	 * 
	 * @Title: getManageById
	 * @author: lijie 
	 * @Description: 根据ID 得到信息
	 * @param id
	 * @return: Result
	 */
	Result getManageById(Integer id);
	/**
	 * 
	 * @Title: getManageByAccount
	 * @author: lijie 
	 * @Description: 根据手机号/用户名查询客户经理数据
	 * @param account
	 * @return: Result
	 */
	Result getManageByAccount(String account);
	/**
	 * 
	* @Title: updateLoginPwd  
	* @Description: 修改登录密码传输实体 
	* @param @param dto
	* @param @return    参数  
	* @return Result    返回类型  
	* @throws
	 */
	Result updateLoginPwd(LoginPasswordDTO dto);
	/**
	 * 
	* @Title: updatePayPwd  
	* @Description: TODO(这里用一句话描述这个方法的作用)  
	* @param @param dto
	* @param @return    参数  
	* @return Result    返回类型  
	* @throws
	 */
	Result updatePayPwd(PayPwdDTO dto);
	/**
	 * 
	* @Title: updateCustomerOpenId  
	* @Description: 修改客户经理openId 
	* @param @param account
	* @param @param openId
	* @param @return    参数  
	* @return Result    返回类型  
	* @throws
	 */
	Result updateCustomerManageOpenId(String account, String openId);

	/**
	 * @Creater: zhangxia
	 * @description：翻页获取客户经理列表
	 * @methodName: getManagerListByPage
	 * @params: [pageBean, selectManagerDTO]
	 * @return: com.aq.util.result.Result
	 * @createTime: 14:22 2018-2-26
	 */
	Result getManagerListByPage(PageBean pageBean, SelectManagerDTO selectManagerDTO);


	/**
	 * @Creater: zhangxia
	 * @description：后台获取客户经理的详细信息
	 * @methodName: getManagerDetail
	 * @params: [selectManagerDTO]
	 * @return: com.aq.util.result.Result
	 * @createTime: 15:00 2018-2-26
	 */
	Result getManagerDetail(SelectManagerDTO selectManagerDTO);

	/**
	 * @Creater: zhangxia
	 * @description：客户经理重置密码
	 * @methodName: resetPWD
	 * @params: [updateManagerDTO]
	 * @return: com.aq.util.result.Result
	 * @createTime: 9:41 2018-2-28
	 */
	Result resetPWD(UpdateManagerDTO updateManagerDTO);

	/**
	 * @Creater: zhangxia
	 * @description：后台编辑客户经理信息
	 * @methodName: editManager
	 * @params: [updateManagerDTO]
	 * @return: com.aq.util.result.Result
	 * @createTime: 10:08 2018-2-28
	 */
	Result editManager(UpdateManagerDTO updateManagerDTO);
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
	 * @author: 张霞
	 * @description：获取所有客户经理
	 * @createDate: 21:13 2018/1/22
	 * @param
	 * @return: com.aq.util.result.Result
	 * @version: 2.1
	 */
	Result getAllManager();
	/**
	 * 
	* @Title: getManagerByOpenId  
	* @Description: 根据openId查询客户经理信息 
	* @param: @param openId
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	Result getManagerByOpenId(String openId);
	/**
	 *
	* @Title: updateWeChatAttention
	* @Description: 客户经理修改关注状态
	* @param: @param openId
	* @param: @param status
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	Result updateWeChatAttention(String openId, Byte status);


	/**
	 * @author: zhangxia
	 * @desc: vip分成比例修改
	 * @params: [updateManagerDTO]
	 * @methodName:updateDivideScalse
	 * @date: 2018/3/23 0023 上午 11:26
	 * @return: com.aq.util.result.Result
	 * @version:2.1.2
	 */
	Result updateDivideScalse(UpdateManagerDTO updateManagerDTO);
}
