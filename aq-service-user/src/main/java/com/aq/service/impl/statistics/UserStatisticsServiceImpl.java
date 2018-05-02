package com.aq.service.impl.statistics;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.PageConstant;
import com.aq.facade.dto.manage.SelectManagerDTO;
import com.aq.facade.entity.permission.UserSecurity;
import com.aq.facade.enums.permission.SecurityBaseSettingEnum;
import com.aq.facade.service.statistics.IUserStatisticsService;
import com.aq.facade.vo.statistics.CustomerStatisticsPreManagerListVO;
import com.aq.facade.vo.statistics.CustomerStatisticsVO;
import com.aq.mapper.customer.CustomerManagerMapper;
import com.aq.mapper.permission.UserSecurityMapper;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.aq.util.string.StringTools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author:zhangxia
 * @createTime:11:29 2018-3-2
 * @version:1.0
 * @desc:后台客户统计数据service实现类
 */
@Slf4j
@Service(version = "1.0.0",timeout = 60000)
public class UserStatisticsServiceImpl implements IUserStatisticsService{

    @Autowired
    CustomerManagerMapper customerManagerMapper;

    @Autowired
    UserSecurityMapper userSecurityMapper;

    /**
     * @Creater: zhangxia
     * @description：统计客户各个指标的数据
     * @methodName: getUserStaticsStable
     * @params: []
     * @return: com.aq.util.result.Result
     * @createTime: 11:23 2018-3-2
     */
    @Override
    public Result getCustomerStatisticsStable() {
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            CustomerStatisticsVO customerStatisticsVO = customerManagerMapper.getCustomerStatisticsStable();
            result = ResultUtil.getResult(RespCode.Code.SUCCESS,customerStatisticsVO);
        } catch (Exception e) {
            log.info("统计客户各个指标的数据处理异常e={}",e);
        }
        log.info("统计客户各个指标的数据处理结果result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @Creater: zhangxia
     * @description：统计客户经理下的客户用户各个指标数据
     * @methodName: getManagerStatisticsList
     * @params: [selectManagerDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 11:27 2018-3-2
     */
    @Override
    public Result getManagerStatisticsList(PageBean pageBean,SelectManagerDTO selectManagerDTO) {
        log.info("统计客户经理下的客户用户各个指标数据入参参数pageBean={},selectManagerDTO={}",JSON.toJSONString(pageBean), JSON.toJSONString(selectManagerDTO));
        int userId = selectManagerDTO.getUserId();
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            //是否有查看所有客户的权限
            UserSecurity userSecurity = new UserSecurity();
            userSecurity.setUserId(selectManagerDTO.getUserId());
            userSecurity.setSecurityId(SecurityBaseSettingEnum.ALL_MANAGER.getId());
            if (userSecurityMapper.selectCount(userSecurity)>0){
                log.info("员工id={}，有查看所有客户经理的权限",userId);
                selectManagerDTO.setUserId(null);
            }
            if (pageBean != null) {
                pageBean.setPageSize(pageBean.getPageSize() == 0 ? PageConstant.PAGE_SIZE : pageBean.getPageSize());
                pageBean.setPageNum(pageBean.getPageNum() == 0 ? PageConstant.PAGE_NUM : pageBean.getPageNum());
                PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            }
            List<CustomerStatisticsPreManagerListVO> listVOS = customerManagerMapper.getCustomerStatisticsOfManagerList(selectManagerDTO);
            //是否有查看电话号码的权限
            userSecurity.setSecurityId(SecurityBaseSettingEnum.MANAGER_TEL_PHONE.getId());
            if (!(userSecurityMapper.selectCount(userSecurity)>0)){
                log.info("员工id={},没有具备查看手机号的权限",userId);
                for (CustomerStatisticsPreManagerListVO listVO:
                listVOS) {
                    listVO.setTelphone(StringTools.telphoneChange(listVO.getTelphone()));
                }
            }
            PageInfo<CustomerStatisticsPreManagerListVO> pageInfo = new PageInfo<>(listVOS);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());

        } catch (Exception e) {
            log.info("统计客户经理下的客户用户各个指标数据处理异常e={}",e);
        }
        log.info("统计客户经理下的客户用户各个指标数据处理结果result={}",JSON.toJSONString(result));
        return result;
    }

    /**
     * @author: zhangxia
     * @desc:我的用户 整体统计各个指标数量
     * @params: [dto]
     * @methodName:getCustomerStatisticsStableByUser
     * @date: 2018/3/22 0022 下午 16:53
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result getCustomerStatisticsStableByUser(SelectManagerDTO dto) {
        log.info("我的用户 整体统计各个指标数量 入参参数dto={}",JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            CustomerStatisticsVO customerStatisticsVO = customerManagerMapper.getStatisticsByUser(dto);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS,customerStatisticsVO);
        } catch (Exception e) {
            log.info("我的用户 整体统计各个指标数量 处理异常e={}",e);
        }
        log.info("我的用户 整体统计各个指标数量 处理结果result={}", JSON.toJSONString(result));
        return result;
    }
}
