package com.aq.controller.adviser;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.base.BaseController;
import com.aq.facade.dto.AdviserDTO;
import com.aq.facade.dto.AdviserPageDTO;
import com.aq.facade.service.IAdviserService;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author:zhangxia
 * @createTime: 2018/4/13 10:10
 * @version:1.0
 * @desc:投顾web列表管理
 */
@Slf4j
@Controller
@RequestMapping(value = "/web/adviser")
public class AdviserController extends BaseController{

    @Reference(version = "1.0.0")
    IAdviserService adviserService;

    /**
     * @auth: zhangxia
     * @desc: 跳转到投顾列表页面
     * @methodName: toPage
     * @params: []
     * @return: java.lang.String
     * @createTime: 2018/4/13 10:12
     * @version:2.1.6
     */
    @RequestMapping("/page")
    public String toPage(){
        return "adviser/adviserList";
    }

    /**
     * @auth: zhangxia
     * @desc: 后台分页获取投顾列表
     * @methodName: adviserListPage
     * @params: [pageBean, dto]
     * @return: com.aq.util.result.Result
     * @createTime: 2018/4/13 10:16
     * @version:2.1.6
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Result adviserListPage(PageBean pageBean, AdviserPageDTO dto){
        log.info("后台分页获取投顾列表 controller入参参数pageBean={}，dto={}", JSON.toJSONString(pageBean),JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (StringUtils.isNotEmpty(dto.getCreateTimeStart())) {
                dto.setCreateTimeStart(dto.getCreateTimeStart() + " 00:00:00");
            }
            if (StringUtils.isNotEmpty(dto.getCreateTimeEnd())) {
                dto.setCreateTimeEnd(dto.getCreateTimeEnd() + " 23:59:59");
            }
            result = adviserService.getAdviserList(dto,pageBean);
        } catch (Exception e) {
            log.info("后台分页获取投顾列表 处理异常e={}",e);
            result.setMessage("处理异常");
        }
        log.info("后台分页获取投顾列表 处理结果result={}",JSON.toJSONString(result));
        return result;
    }

    /**
     * @auth: zhangxia
     * @desc: 通过投顾id修改投顾是否显示信息
     * @methodName: adviserIsVisible
     * @params: [dto]
     * @return: com.aq.util.result.Result
     * @createTime: 2018/4/13 11:06
     * @version:2.1.6
     */
    @RequestMapping(value = "/isVisible")
    @ResponseBody
    public Result adviserIsVisible(AdviserDTO dto){
        log.info("通过投顾id修改投顾是否显示信息 controller 入参参数dto={}",JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            result = adviserService.modifyAdviserById(dto);
        } catch (Exception e) {
            log.info("通过投顾id修改投顾是否显示信息 处理异常e={}",e);
            result.setMessage("处理异常");
        }
        log.info("通过投顾id修改投顾是否显示信息 controller 处理结果result={}",JSON.toJSONString(result));
        return result;

    }
}
