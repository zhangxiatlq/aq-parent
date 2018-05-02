package com.aq.controller.statistics;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.base.BaseController;
import com.aq.facade.dto.manage.SelectManagerDTO;
import com.aq.facade.service.statistics.IUserStatisticsService;
import com.aq.facade.vo.permission.SessionUserVO;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

/**
 * @author:zhangxia
 * @createTime:14:01 2018-3-2
 * @version:1.0
 * @desc:我的用户 客户统计数据controller
 */
@Slf4j
@Controller
@RequestMapping(value = "/web/statisticsByUser/")
public class CustomerStatisticsByUserController extends BaseController{

    @Reference(version = "1.0.0")
    IUserStatisticsService userStatisticsService;

    /**
     * @Creater: zhangxia
     * @description：跳转到页面客户统计页面
     * @methodName: toPage
     * @params: []
     * @return: java.lang.String
     * @createTime: 16:58 2018-3-2
     */
    @RequestMapping(value = "page",method = RequestMethod.GET)
    public String toPage(ModelAndView modelAndView){
        return "statistics/customer_statistics_myself";
    }

    /**
     * @author: zhangxia
     * @desc:我的用户 整体统计各个指标数量
     * @params: []
     * @methodName:getCustomerStatisticsStableByUser
     * @date: 2018/3/22 0022 下午 17:00
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @RequestMapping(value = "customers",method = RequestMethod.GET)
    @ResponseBody
    public Result getCustomerStatisticsStableByUser(){
        log.info("统计员工自己维护下面 的客户各个指标的数据controllret");
        SessionUserVO sessionUserVO = getLoginUser();
        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.ILLEGAL_OPTION);
        }
        SelectManagerDTO selectManagerDTO = new SelectManagerDTO();
        selectManagerDTO.setUserId(sessionUserVO.getId());
        return userStatisticsService.getCustomerStatisticsStableByUser(selectManagerDTO);
    }


    /**
     * @author: zhangxia
     * @desc:统计员工自己维护下面 的客户用户各个指标数据
     * @params: [pageBean, selectManagerDTO]
     * @methodName:getManagerStatisticsListByUser
     * @date: 2018/3/22 0022 下午 17:04
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @RequestMapping(value = "list",method = RequestMethod.GET)
    @ResponseBody
    public Result getManagerStatisticsListByUser(PageBean pageBean, SelectManagerDTO selectManagerDTO){

        log.info("统计员工自己维护下面 的客户用户各个指标数据controller入参参数pageBean={},selectManagerDTO={}",JSON.toJSONString(pageBean), JSON.toJSONString(selectManagerDTO));
        SessionUserVO sessionUserVO = getLoginUser();
        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.ILLEGAL_OPTION);
        }
        selectManagerDTO.setUserId(sessionUserVO.getId());
        return userStatisticsService.getManagerStatisticsList(pageBean,selectManagerDTO);
    }



}
