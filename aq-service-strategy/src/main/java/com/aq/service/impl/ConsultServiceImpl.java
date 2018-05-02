package com.aq.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.IsDeleteEnum;
import com.aq.core.exception.BizException;
import com.aq.facade.contant.ConsultEnum;
import com.aq.facade.dto.ConsultAddDTO;
import com.aq.facade.dto.ConsultQueryDTO;
import com.aq.facade.dto.ConsultRemoveDTO;
import com.aq.facade.entity.Consult;
import com.aq.facade.service.IConsultService;
import com.aq.facade.vo.ConsultManagerVO;
import com.aq.facade.vo.ConsultQueryVO;
import com.aq.facade.vo.ConsultWeChatVO;
import com.aq.mapper.ConsultMapper;
import com.aq.util.date.DateUtil;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 今日汇 service.impl
 *
 * @author 郑朋
 * @create 2018/2/28 0028
 **/
@Service(version = "1.0.0")
@Slf4j
public class ConsultServiceImpl implements IConsultService {

    @Autowired
    private ConsultMapper consultMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result addConsult(ConsultAddDTO consultAddDTO) {
        log.info("新增今日汇入参，consultAddDTO={}", JSON.toJSONString(consultAddDTO));
        Result result;
        String message = consultAddDTO.validateForm();
        try {
            if (StringUtils.isNotBlank(message)) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                result.setMessage(message);
                return result;
            }
            Consult consult = new Consult();
            BeanUtils.copyProperties(consultAddDTO, consult);
            consult.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());
            consult.setIsVisible(ConsultEnum.YEW_VISIBLE.getCode());
            consult.setCreaterId(consultAddDTO.getManagerId());
            consult.setCreateTime(new Date());
            consultMapper.insert(consult);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("新增今日汇返回值，result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("新增今日汇异常，e={}", e);
            throw new BizException(RespCode.Code.INTERNAL_SERVER_ERROR.getMsg());
        }
    }

    @Override
    public Result getConsultPage(PageBean pageBean, ConsultQueryDTO consultQueryDTO) {
        log.info("分页查询今日汇列表入参：pageBean={},consultQueryDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(consultQueryDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            if (StringUtils.isBlank(consultQueryDTO.getOrderType())) {
                consultQueryDTO.setOrderType("desc");
            } else {
                consultQueryDTO.setOrderType(consultQueryDTO.getOrderType().toLowerCase());
            }
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<ConsultQueryVO> list = consultMapper.selectConsultList(consultQueryDTO);
            PageInfo<ConsultQueryVO> pageInfo = new PageInfo<>(list);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
        } catch (Exception e) {
            log.error("分页查询今日汇列表异常：e={}", e);
        }
        log.info("分页查询今日汇列表返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result removeConsult(ConsultRemoveDTO consultRemoveDTO) {
        log.info("删除今日汇入参，consultRemoveDTO={}", JSON.toJSONString(consultRemoveDTO));
        Result result;
        String message = consultRemoveDTO.validateForm();
        try {
            if (StringUtils.isNotBlank(message)) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                result.setMessage(message);
                return result;
            }
            Consult consult = new Consult();
            consult.setId(consultRemoveDTO.getId());
            consult.setIsDelete(IsDeleteEnum.IS_DELETE_ENUM.getCode());
            consult.setUpdaterId(consultRemoveDTO.getManagerId());
            consult.setUpdateTime(new Date());
            consultMapper.updateByPrimaryKeySelective(consult);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("删除今日汇返回值，result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("删除今日汇汇异常，e={}", e);
            throw new BizException(RespCode.Code.INTERNAL_SERVER_ERROR.getMsg());
        }
    }

    @Override
    public Result getWeChatConsult(PageBean pageBean, Integer customerId) {
        log.info("微信--今日汇列表（分页）入参：pageBean={},customerId={}", JSON.toJSONString(pageBean), customerId);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            ConsultManagerVO consultManagerVO = consultMapper.selectAdviserWeChatList(customerId);
            long count = 0L;
            if (null != consultManagerVO) {
                PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
                List<ConsultWeChatVO> list = consultMapper.selectConsultWeChatList(consultManagerVO.getManagerId());
                PageInfo<ConsultWeChatVO> pageInfo = new PageInfo<>(list);
                if (CollectionUtils.isNotEmpty(list)) {
                    String now = DateUtils.formatDate(new Date(), DateUtil.YYYYMMDD);
                    for (ConsultWeChatVO consultWeChatVO : pageInfo.getList()) {
                        String[] time = consultWeChatVO.getCreateTime().split(" ");
                        if (now.equals(time[0])) {
                            consultWeChatVO.setCreateTime(time[1]);
                        }
                    }
                }
                consultManagerVO.setList(pageInfo.getList());
                count = pageInfo.getTotal();
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, consultManagerVO, count);
        } catch (Exception e) {
            log.error("微信--今日汇列表（分页）异常：e={}", e);
        }
        log.info("微信--今日汇列表（分页）返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public Result getWeChatConsultById(Integer consultId, Integer customerId) {
        log.info("微信--通过今日汇id 获取详情入参：consultId={},customerId={}", consultId, customerId);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            ConsultManagerVO consultManagerVO = consultMapper.selectAdviserWeChatList(customerId);
            if (null != consultManagerVO) {
                Consult consult = consultMapper.selectByPrimaryKey(consultId);
                if (null != consult) {
                    ConsultWeChatVO vo = new ConsultWeChatVO();
                    BeanUtils.copyProperties(consult, vo);
                    vo.setCreateTime(DateUtils.formatDate(new Date(), DateUtil.YYYYMMDDHHMMSS));
                    List<ConsultWeChatVO> list = new ArrayList<>();
                    list.add(vo);
                    consultManagerVO.setList(list);
                }
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, consultManagerVO);
        } catch (Exception e) {
            log.error("微信--通过今日汇id 获取详情异常：e={}", e);
        }
        log.info("微信--通过今日汇id 获取详情返回值：result={}", JSON.toJSONString(result));
        return result;
    }

}
