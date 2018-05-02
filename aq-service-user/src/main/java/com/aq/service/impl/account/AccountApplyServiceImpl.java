package com.aq.service.impl.account;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.CashAuthStatusEnum;
import com.aq.core.constant.PageConstant;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.constant.TransCodeEnum;
import com.aq.facade.dto.account.QueryDrawcashDTO;
import com.aq.facade.dto.account.UpdateBalanceDTO;
import com.aq.facade.dto.account.UpdateDrawcashDTO;
import com.aq.facade.entity.account.DrawcashApply;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.service.account.IAccountApplyService;
import com.aq.facade.vo.account.DrawcashAuthDetailVO;
import com.aq.facade.vo.account.DrawcashListVO;
import com.aq.mapper.account.DrawcashApplyMapper;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author:zhangxia
 * @createTime:17:33 2018-2-23
 * @version:1.0
 * @desc:
 */
@Slf4j
@Service(version = "1.0.0")
public class AccountApplyServiceImpl implements IAccountApplyService {

    @Autowired
    DrawcashApplyMapper drawcashApplyMapper;

    @Autowired
    BalanceServiceImpl balanceService;

    /**
     * @Creater: zhangxia
     * @description：后台分页获取提现申请列表
     * @methodName: getDrawcashListByPage
     * @params: [pageBean, queryDrawcashDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 17:34 2018-2-23
     */
    @Override
    public Result getDrawcashListByPage(PageBean pageBean, QueryDrawcashDTO queryDrawcashDTO) {
        log.info("后台分页获取提现申请列表service入参参数pageBean={}，queryDrawcashDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(queryDrawcashDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (pageBean != null) {
                pageBean.setPageSize(pageBean.getPageSize() == 0 ? PageConstant.PAGE_SIZE : pageBean.getPageSize());
                pageBean.setPageNum(pageBean.getPageNum() == 0 ? PageConstant.PAGE_NUM : pageBean.getPageNum());
                PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());

                List<DrawcashListVO> drawcashListVOList = drawcashApplyMapper.getDrawcashListByPage(queryDrawcashDTO);
                PageInfo<DrawcashListVO> pageInfo = new PageInfo<>(drawcashListVOList);
                result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
            }
        } catch (Exception e) {
            log.info("后台分页获取提现申请列表service处理结果异常e={}", e);
        }
        log.info("后台分页获取提现申请列表service处理结果result={}", JSON.toJSONString(result));
        return result;
    }


    /**
     * @Creater: zhangxia
     * @description：更新提现申请记录审核
     * @methodName: updateDrawcashApply
     * @params: [updateDrawcashDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 18:04 2018-2-23
     */
    @Transactional
    @Override
    public Result updateDrawcashAuthApply(UpdateDrawcashDTO updateDrawcashDTO) {
        log.info("更新提现申请记录审核service入参参数updateDrawcashDTO={}",JSON.toJSONString(updateDrawcashDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            DrawcashApply drawcashApply = drawcashApplyMapper.selectByPrimaryKey(updateDrawcashDTO.getId());
            if (Objects.nonNull(drawcashApply)){
                drawcashApply.setAuthDesc(updateDrawcashDTO.getAuthDesc());
                drawcashApply.setStatus(updateDrawcashDTO.getStatus());
                drawcashApply.setUpdaterId(updateDrawcashDTO.getUpdaterId());
                drawcashApply.setUpdateTime(new Date());
                if (drawcashApplyMapper.updateByPrimaryKeySelective(drawcashApply)>0){
                    log.info("更新提现申请记录审核成功");
                    if (CashAuthStatusEnum.CASH_REJECT_ENUM.getCode().equals(updateDrawcashDTO.getStatus())){
                        //审核未通过需要进行回款
                        UpdateBalanceDTO updateBalanceDTO = new UpdateBalanceDTO();
                        updateBalanceDTO.setChangeBalance(drawcashApply.getPrice());
                        updateBalanceDTO.setManagerId(drawcashApply.getCreaterId());
                        updateBalanceDTO.setOrderNo(drawcashApply.getOrderNo());
                        updateBalanceDTO.setTransCodeEnum(TransCodeEnum.BALANCE_DRAW_CASH_BACK);
                        updateBalanceDTO.setRemark(updateDrawcashDTO.getAuthDesc());
                        updateBalanceDTO.setRoleType(RoleCodeEnum.MANAGER.getCode());
                        if (balanceService.modifyBalance(updateBalanceDTO).isSuccess()){
                            log.info("审核未通过需要进行回款 成功");
                            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                        }else {
                            log.info("审核未通过需要进行回款 失败");
                            throw new UserBizException("审核未通过需要进行回款 失败");
                        }
                    }else {
                        result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                    }
                }else {
                    log.info("更新提现申请记录审核失败");
                }
            }else {
                result.setMessage("数据不存在，请勿非法操作");
            }
        } catch (Exception e) {
            log.info("更新提现申请记录审核service处理结果异常e={}",e);
            throw new UserBizException("更新提现申请记录审核service处理结果异常");
        }
        log.info("更新提现申请记录审核service处理结果result={}",JSON.toJSONString(result));
        return result;
    }

    /**
     * @Creater: zhangxia
     * @description：提现审核结果详情
     * @methodName: getDrawcashAuthDetail
     * @params: [queryDrawcashDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 19:01 2018-2-23
     */
    @Override
    public Result getDrawcashAuthDetail(QueryDrawcashDTO queryDrawcashDTO) {
        log.info("提现审核结果详情service入参参数queryDrawcashDTO={}",JSON.toJSONString(queryDrawcashDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            DrawcashAuthDetailVO drawcashAuthDetailVO = drawcashApplyMapper.getDrawcashAuthDetail(queryDrawcashDTO);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS,drawcashAuthDetailVO);
        } catch (Exception e) {
            log.info("提现审核结果详情service处理结果异常e={}",e);
        }
        log.info("提现审核结果详情service处理结果result={}",JSON.toJSONString(result));
        return result;
    }
}
