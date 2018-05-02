package com.aq.facade.service;

import com.aq.facade.dto.SelectClientDTO;
import com.aq.facade.dto.UpdateClientDTO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：客户接口
 * @作者： 张霞
 * @创建时间： 16:36 2018/1/19
 * @Copyright @2017 by zhangxia
 */
public interface IClientService {
	/**
	 * 
	 * @Title: getUserInfoByOpenId
	 * @author: lijie 
	 * @Description: 根据微信openId查询用户信息
	 * @param openId
	 * @return
	 * @return: UserInfo
	 */
	Result getUserInfoByOpenId(String openId);

	/**
	 * @author: 张霞
	 * @description：翻页获取客户列表
	 * @createDate: 17:31 2018/1/20
	 * @param pageBean
	 * @param selectClientDTO
	 * @return: com.aq.util.result.Result
	 * @version: 2.1
	 */
	Result getClientListByPage(PageBean pageBean, SelectClientDTO selectClientDTO);

	/**
	 * @author: 张霞
	 * @description：获取客户详情信息
	 * @createDate: 19:18 2018/1/20
	 * @param selectClientDTO
	 * @return: com.aq.util.result.Result
	 * @version: 2.1
	 */
	Result getClientDetail(SelectClientDTO selectClientDTO);
	/**
	 * 
	 * @Title: checkBind
	 * @author: lijie 
	 * @Description: 校验用户是否绑定
	 * @param openId
	 * @return
	 * @return: Result
	 */
	Result checkBind(String openId);

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
}
