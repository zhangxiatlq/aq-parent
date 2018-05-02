package com.aq.mapper.manager;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.manage.SelectManagerDTO;
import com.aq.facade.entity.manager.Manager;
import com.aq.facade.vo.ManagerDetailVO;
import com.aq.facade.vo.customer.QueryCustomerVO;
import com.aq.facade.vo.manage.ManagerBaseInfoVO;
import com.aq.facade.vo.manage.ManagerListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户经理 mapper
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Repository
public interface ManagerMapper extends BaseMapper<Manager> {
    /**
     * @param check
     * @return
     * @Title: checkCustomerExists
     * @author: lijie
     * @Description: 校验客户经理信息数据
     * @return: List<Manager>
     */
    List<Manager> checkManageExists(Manager check);

    /**
     * 通过客户经理id查询客户经理基本信息(包含银行卡、实名认证）
     *
     * @param managerId
     * @return com.aq.facade.vo.manage.ManagerBaseInfoVO
     * @author 郑朋
     * @create 2018/2/10 0010
     */
    ManagerBaseInfoVO selectManagerInfoById(Integer managerId);

    /**
     * @param @return 参数
     * @return Integer    返回类型
     * @throws
     * @Title: selectMaxIdCode
     * @Description:查询客户经理数据
     */
    Integer selectMaxIdCode();

    /**
     * @Creater: zhangxia
     * @description：翻页获取客户经理列表
     * @methodName: getManagerListByPage
     * @params: [selectManagerDTO]
     * @return: java.util.List<com.aq.facade.vo.manage.ManagerListVO>
     * @createTime: 14:25 2018-2-26
     */
    List<ManagerListVO> getManagerListByPage(SelectManagerDTO selectManagerDTO);

    /**
     * @Creater: zhangxia
     * @description：后台获取客户经理详情信息
     * @methodName: getManagerDetail
     * @params: [selectManagerDTO]
     * @return: com.aq.facade.vo.ManagerDetailVO
     * @createTime: 15:02 2018-2-26
     */
    ManagerDetailVO getManagerDetail(SelectManagerDTO selectManagerDTO);

    /**
     * @param
     * @author: 张霞
     * @description：获取所有客户经理
     * @createDate: 21:21 2018/1/22
     * @return: java.util.List<com.aq.facade.vo.manage.ManagerListVO>
     * @version: 2.1
     */
    List<ManagerListVO> getAllManager();

    /**
     * 查询工具数量
     *
     * @param managerId
     * @param toolCategoryCode
     * @return com.aq.facade.vo.customer.QueryCustomerVO
     * @author 郑朋
     * @create 2018/3/6 0006
     */
    QueryCustomerVO selectToolPushNum(@Param("managerId") Integer managerId, @Param("toolCategoryCode") String toolCategoryCode);
    /**
     *
    * @Title: updateOpenIdIsNull
    * @Description: TODO
    * @param: @param telphone
    * @return void
    * @author lijie
    * @throws
     */
	void updateOpenIdIsNull(@Param("telphone") String telphone);
}