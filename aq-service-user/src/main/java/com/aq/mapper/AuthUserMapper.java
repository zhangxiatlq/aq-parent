package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.SelectClientDTO;
import com.aq.facade.dto.manage.SelectManagerDTO;
import com.aq.facade.entity.AuthUser;
import com.aq.facade.vo.ClientDetailVO;
import com.aq.facade.vo.ClientListVO;
import com.aq.facade.vo.ClientUserInfoVO;
import com.aq.facade.vo.ManagerDetailVO;
import com.aq.facade.vo.manage.ManagerListVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：AuthUser实体的mapper
 * @作者： 张霞
 * @创建时间： 14:18 2018/1/19
 * @Copyright @2017 by zhangxia
 */
@Repository
public interface AuthUserMapper extends BaseMapper<AuthUser> {

    /**
     * @author: 张霞
     * @description：翻页获取客户列表
     * @createDate: 17:34 2018/1/20
     * @param selectClientDTO
     * @return: java.util.List<com.aq.facade.vo.ClientListVO>
     * @version: 2.1
     */
    List<ClientListVO> getClientListByPage(SelectClientDTO selectClientDTO);
    /**
     * 
     * @Title: selectUserInfoByOpenId
     * @author: lijie 
     * @Description: 根据用户ID查询用户信息
     * @param openId
     * @return
     * @return: ClientUserInfoVO
     */
	ClientUserInfoVO selectUserInfoByOpenId(String openId);

    /**
     * @author: 张霞
     * @description：获取客户详情信息
     * @createDate: 19:18 2018/1/20
     * @param selectClientDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
	ClientDetailVO getClientDetail(SelectClientDTO selectClientDTO);


    /**
     * @author: 张霞
     * @description：翻页获取客户经理列表信息
     * @createDate: 20:20 2018/1/21
     * @param selectManagerDTO
     * @return: java.util.List<com.aq.facade.vo.manage.ManagerListVO>
     * @version: 2.1
     */
    List<ManagerListVO> getManagerListByPage(SelectManagerDTO selectManagerDTO);

    /**
     * @author: 张霞
     * @description：后台获取客户经理详情信息
     * @createDate: 23:26 2018/1/21
     * @param selectManagerDTO
     * @return: com.aq.facade.vo.ManagerDetailVO
     * @version: 2.1
     */
    ManagerDetailVO getManagerDetail(SelectManagerDTO selectManagerDTO);

    /**
     * @author: 张霞
     * @description：获取所有客户经理
     * @createDate: 21:21 2018/1/22
     * @param
     * @return: java.util.List<com.aq.facade.vo.manage.ManagerListVO>
     * @version: 2.1
     */
    List<ManagerListVO> getAllManager();


}
