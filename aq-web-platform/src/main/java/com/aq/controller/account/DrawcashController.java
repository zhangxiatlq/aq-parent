package com.aq.controller.account;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.base.BaseController;
import com.aq.facade.dto.account.QueryDrawcashDTO;
import com.aq.facade.dto.account.UpdateDrawcashDTO;
import com.aq.facade.service.account.IAccountApplyService;
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

import java.util.Objects;

/**
 * @author:zhangxia
 * @createTime:19:12 2018-2-23
 * @version:1.0
 * @desc:后台提现列表controller
 */
@Slf4j
@Controller
@RequestMapping(value = "/web/drawcash/")
public class DrawcashController extends BaseController{

    @Reference(version = "1.0.0")
    IAccountApplyService accountApplyService;

    /**
     * @Creater: zhangxia
     * @description：跳转到提现列表页面
     * @methodName: toListPage
     * @params: []
     * @return: java.lang.String
     * @createTime: 19:17 2018-2-23
     */
    @RequestMapping(value = "page",method = RequestMethod.GET)
    public String toListPage(){
        return "account/drawcash";
    }

    /**
     * @Creater: zhangxia
     * @description：后台分页获取提现申请列表
     * @methodName: getDrawcashListByPage
     * @params: [pageBean, queryDrawcashDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 19:22 2018-2-23
     */
    @RequestMapping(value = "list",method = RequestMethod.GET)
    @ResponseBody
    public Result getDrawcashListByPage(PageBean pageBean,QueryDrawcashDTO queryDrawcashDTO){
        log.info("后台分页获取提现申请列表controller层入参参数pageBean={}，queryDrawcashDTO={}", JSON.toJSONString(pageBean),JSON.toJSONString(queryDrawcashDTO));
        SessionUserVO sessionUserVO = getLoginUser();
        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.ILLEGAL_OPTION);
        }
        return accountApplyService.getDrawcashListByPage(pageBean,queryDrawcashDTO);
    }


    /**
     * @Creater: zhangxia
     * @description：更新提现申请记录审核
     * @methodName: authDrawcash
     * @params: [updateDrawcashDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 19:25 2018-2-23
     */
    @RequestMapping(value = "auth",method = RequestMethod.POST)
    @ResponseBody
    public Result authDrawcash(UpdateDrawcashDTO updateDrawcashDTO){
        log.info("更新提现申请记录审核controller入参参数updateDrawcashDTO={}",JSON.toJSONString(updateDrawcashDTO));
        SessionUserVO sessionUserVO = getLoginUser();
        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.ILLEGAL_OPTION);
        }
        if (updateDrawcashDTO.getAuthDesc().length()>50){
            Result result = ResultUtil.getResult(RespCode.Code.FAIL);
            result.setMessage("未通过原因不得超过50字");
            return result;
        }
        updateDrawcashDTO.setUpdaterId(sessionUserVO.getId());
        return accountApplyService.updateDrawcashAuthApply(updateDrawcashDTO);
    }

    /**
     * @Creater: zhangxia
     * @description：提现审核结果详情
     * @methodName: getAuthDrawcashDetail
     * @params: [queryDrawcashDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 19:31 2018-2-23
     */
    @RequestMapping(value = "authDetail", method = RequestMethod.POST)
    @ResponseBody
    public Result getAuthDrawcashDetail(QueryDrawcashDTO queryDrawcashDTO){
        log.info("提现审核结果详情controller入参参数queryDrawcashDTO={}",JSON.toJSONString(queryDrawcashDTO));
        SessionUserVO sessionUserVO = getLoginUser();
        if (Objects.isNull(sessionUserVO)){
            return ResultUtil.getResult(RespCode.Code.ILLEGAL_OPTION);
        }
        return accountApplyService.getDrawcashAuthDetail(queryDrawcashDTO);
    }


}
