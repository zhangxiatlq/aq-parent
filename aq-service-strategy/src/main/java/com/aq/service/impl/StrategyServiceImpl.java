package com.aq.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.IsDeleteEnum;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.facade.contant.PushStateEnum;
import com.aq.facade.dto.*;
import com.aq.facade.dto.StrategysQueryDTO;
import com.aq.facade.entity.Strategy;
import com.aq.facade.entity.StrategyPurchase;
import com.aq.facade.entity.StrategyRecommend;
import com.aq.facade.service.IStrategyService;
import com.aq.facade.vo.*;
import com.aq.mapper.StrategyMapper;
import com.aq.mapper.StrategyPurchaseMapper;
import com.aq.mapper.StrategyRecommendMapper;
import com.aq.util.bean.VoDtoEntityUtil;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 策略接口实现类
 *
 * @author 熊克文
 * @createDate 2018\2\8 0008
 **/
@Service(version = "1.0.0")
@Slf4j
public class StrategyServiceImpl implements IStrategyService {

    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private StrategyPurchaseMapper strategyPurchaseMapper;

    @Autowired
    private StrategyRecommendMapper strategyRecommendMapper;

    @Override
    public Result listStrategyQueryVO(BoutiqueStrategyQueryDTO dto, PageBean page) {
        String message = dto.validateForm();

        if (message != null) {
            return ResultUtil.setResult(false, message);
        }
        log.info("分页查询精品列表参数为 dto:{},page:{}", JSON.toJSONString(dto), JSON.toJSONString(page));
        PageInfo<BoutiqueStrategyQueryVO> pageList;
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<BoutiqueStrategyQueryVO> list = this.strategyMapper.listStrategyQueryVO(dto);
        pageList = new PageInfo<>(list);
        log.info("分页查询精品列表 返回结果为 pageList:{}", JSON.toJSONString(pageList));
        return ResultUtil.getResult(RespCode.Code.SUCCESS, pageList.getList(), pageList.getTotal());
    }

    @Override
    public Result getStrategyDetailVO(StrategyDetailDTO dto) {
        String message = dto.validateForm();

        if (message != null) {
            return ResultUtil.setResult(false, message);
        }

        log.info("精品策略详情查询参数为 dto:{}", JSON.toJSONString(dto));
        List<StrategyPurchaseVO> strategyPurchaseVOList = this.strategyPurchaseMapper.listStrategyPurchaseVO(VoDtoEntityUtil.convert(dto, StrategyPurchaseDTO.class));

        //判断当前用户是否存在购买策略记录
        if (CollectionUtils.isEmpty(strategyPurchaseVOList)) {
            return ResultUtil.setResult(false, "用户尚未购买该策略");
        } else {
            //判断是否过期
            Integer existExpiry = DateUtils.truncatedCompareTo(strategyPurchaseVOList.get(0).getExpiryTime(), new Date(), Calendar.DATE);
            if (existExpiry < 1) {
                return ResultUtil.setResult(false, "用户购买的策略已经过期");
            }
        }

        StrategyDetailVO strategyDetailVO = this.strategyMapper.getStrategyDetailVO(dto);
        if (strategyDetailVO == null) {
            return ResultUtil.getResult(RespCode.Code.NOT_QUERY_DATA);
        }

        log.info("精品策略详情查询返回结果为 strategyDetailVO:{}", JSON.toJSONString(strategyDetailVO));
        return ResultUtil.getResult(RespCode.Code.SUCCESS, strategyDetailVO);
    }

    @Override
    public Result listStrategyPushQueryVO(Integer strategyId, PageBean page) {
        if (strategyId == null) {
            return ResultUtil.setResult(false, "策略id不能为空");
        }

        log.info("分页查询策略推送信息参数为 strategyId:{}，page:{}", strategyId, JSON.toJSONString(page));

        PageInfo<StrategyPushQueryVO> pageList;
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<StrategyPushQueryVO> list = this.strategyMapper.listStrategyPushQueryVO(
                CollectionUtils.toMap("id", strategyId, "purchaseState", 1));
        pageList = new PageInfo<>(list);

        log.info("分页查询策略推送信息 返回结果为 pageList:{}", JSON.toJSONString(pageList));
        return ResultUtil.getResult(RespCode.Code.SUCCESS, pageList.getList(), pageList.getTotal());
    }

    @Override
    public Result listStrategyRecommendVO(Integer strategyId, Integer managerId, PageBean page) {
        if (strategyId == null) {
            return ResultUtil.setResult(false, "策略id不能为空");
        }

        if (managerId == null) {
            return ResultUtil.setResult(false, "经理id不能为空");
        }

        log.info("分页查询策略推荐参数为 strategyId:{}，managerId:{}，page:{}", strategyId, managerId, JSON.toJSONString(page));

        PageInfo<StrategyRecommendVO> pageList;
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<StrategyRecommendVO> list = this.strategyMapper.listStrategyRecommendVO(
                CollectionUtils.toMap("id", strategyId, "managerId", managerId));
        pageList = new PageInfo<>(list);


        log.info("分页查询策略推荐 返回结果为 pageList:{}", JSON.toJSONString(pageList));
        return ResultUtil.getResult(RespCode.Code.SUCCESS, pageList.getList(), pageList.getTotal());
    }

    @Override
    public Result listStrategyPurchaseVO(Integer strategyId, Integer managerId, PageBean page) {
        if (strategyId == null) {
            return ResultUtil.setResult(false, "策略id不能为空");
        }

        if (managerId == null) {
            return ResultUtil.setResult(false, "经理id不能为空");
        }

        log.info("分页查询策略购买参数为 strategyId:{}，managerId:{}，page:{}", strategyId, managerId, JSON.toJSONString(page));
        PageInfo<StrategyPurchaseVO> pageList;
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<StrategyPurchaseVO> list = this.strategyMapper.listStrategyPurchaseVO(
                CollectionUtils.toMap("id", strategyId, "managerId", managerId));
        pageList = new PageInfo<>(list);

        log.info("分页查询策略购买 返回结果为 pageList:{}", JSON.toJSONString(pageList));
        return ResultUtil.getResult(RespCode.Code.SUCCESS, pageList.getList(), pageList.getTotal());
    }

    @Override
    public Result listStrategyPositionVO(Integer strategyId, PageBean page) {
        if (strategyId == null) {
            return ResultUtil.setResult(false, "策略id不能为空");
        }

        log.info("分页查询策略持仓参数为 strategyId:{}，page:{}", strategyId, JSON.toJSONString(page));
        PageInfo<StrategyPositionVO> pageList;
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<StrategyPositionVO> list = this.strategyMapper.listStrategyPositionVO(
                CollectionUtils.toMap("id", strategyId));
        pageList = new PageInfo<>(list);

        log.info("分页查询策略持仓 返回结果为 pageList:{}", JSON.toJSONString(pageList));
        return ResultUtil.getResult(RespCode.Code.SUCCESS, pageList.getList(), pageList.getTotal());
    }

    @Override
    public Result pageStrategysSelfSupportQueryVO(PageBean pageBean, StrategysQueryDTO dto) {

        PageInfo<StrategysSelfSupportQueryVO> pageList;
        log.info("自营策略查询列表开始,参数为page:{},dto:{}", JSON.toJSONString(pageBean), JSON.toJSONString(dto));
        PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
        List<StrategysSelfSupportQueryVO> list = this.strategyMapper.listStrategysSelfSupportQueryVO(dto);
        pageList = new PageInfo<>(list);

        return ResultUtil.getResult(RespCode.Code.SUCCESS, pageList.getList(), pageList.getTotal());
    }


    @Override
    public Result pageStrategysPushVO(PageBean pageBean, StrategysPushQueryDTO dto) {
        PageInfo<StrategysPushQueryVO> pageList;
        log.info("策略推送对象,参数为page:{},dto:{}", JSON.toJSONString(pageBean), JSON.toJSONString(dto));
        PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
        List<StrategysPushQueryVO> list = this.strategyMapper.listStrategysPushVO(dto);
        pageList = new PageInfo<>(list);

        return ResultUtil.getResult(RespCode.Code.SUCCESS, pageList.getList(), pageList.getTotal());
    }

    @Override
    public Result strategysPushRoleType(Integer strategysId) {
        StrategyRecommend example = new StrategyRecommend();
        example.setStrategyId(strategysId);
        example.setRecommendedId(0);
        List<StrategyRecommend> strategyRecommend = this.strategyRecommendMapper.select(example);
        if (CollectionUtils.isNotEmpty(strategyRecommend)) {
            return ResultUtil.getResult(RespCode.Code.SUCCESS, strategyRecommend.get(0).getBeRecommendedRoleType());
        }
        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }

    @Override
    public Result addStrategys(StrategysDTO dto) {
        String validateMessage = dto.validateForm();
        if (validateMessage != null) {
            return ResultUtil.setResult(false, validateMessage);
        }

        Strategy strategy = VoDtoEntityUtil.convert(dto, Strategy.class);
        strategy.setBeginDate(new Date());
        strategy.setCreateTime(new Date());
        strategy.setStrategyDesc("");

        this.strategyMapper.insert(strategy);

        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }

    @Override
    public Result getStrategys(Integer id) {
        if (id == null) {
            return ResultUtil.getResult(RespCode.Code.NOT_QUERY_DATA);
        }

        Strategy strategy = this.strategyMapper.selectByPrimaryKey(id);

        if (strategy == null) {
            return ResultUtil.getResult(RespCode.Code.NOT_QUERY_DATA);
        }

        return ResultUtil.getResult(RespCode.Code.SUCCESS, strategy);
    }

    @Override
    public Result editStrategys(StrategysDTO dto) {
        String validateMessage = dto.validateForm();

        log.info("策略修改开始,参数为dto:{}", JSON.toJSONString(dto));

        if (dto.getId() == null) {
            return ResultUtil.setResult(false, "修改Id不能为空");
        }

        this.strategyMapper.updateByPrimaryKeySelective(VoDtoEntityUtil.convert(dto, Strategy.class));

        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }

    @Override
    public Result auditStrategys(StrategysOperateDTO dto) {

        log.info("策略审核开始,参数为dto:{}", JSON.toJSONString(dto));

        if (dto.getTrademodelStrategysId() == null) {
            return ResultUtil.setResult(false, "审核id不能为空");
        }


        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }

    @Override
    public Result delStrategys(Integer id) {

        log.info("策略删除开始,id:{}", id);

        if (id == null) {
            return ResultUtil.setResult(false, "删除Id不能为空");
        }

        StrategyPurchase strategyPurchase = new StrategyPurchase();
        strategyPurchase.setStrategyId(id);
        int purchaseCount = this.strategyPurchaseMapper.selectCount(strategyPurchase);

        if (purchaseCount > 0) {
            return ResultUtil.setResult(false, "当前策略已被购买,无法删除");
        }

        StrategyRecommend strategyRecommend = new StrategyRecommend();
        strategyRecommend.setStrategyId(id);
        int recommendCount = this.strategyRecommendMapper.selectCount(strategyRecommend);

        if (recommendCount > 0) {
            return ResultUtil.setResult(false, "当前策略已被推荐,无法删除");
        }

        this.strategyMapper.deleteByPrimaryKey(id);

        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }

    @Transactional
    @Override
    public Result pushStrategys(Integer strategysId, RoleCodeEnum roleCodeEnum, String[] userIds, Boolean allChecked) {

        log.info("推荐策略开始,strategysId:{},userIds:{},userTypeEnum:{},allChecked:{}", strategysId, userIds, JSON.toJSONString(roleCodeEnum), allChecked);

        if (roleCodeEnum == null) {
            return ResultUtil.setResult(false, "用户类型不存在");
        }

        if (strategysId == null) {
            return ResultUtil.setResult(false, "策略id不能为空");
        }

        //删除之前所有推荐的记录
        StrategyRecommend strategyRecommend = new StrategyRecommend();
        strategyRecommend.setRecommendedId(0);
        strategyRecommend.setStrategyId(strategysId);
        this.strategyRecommendMapper.delete(strategyRecommend);
        //策略价格
        Strategy strategy = this.strategyMapper.selectByPrimaryKey(strategysId);
        if (strategy == null) {
            return ResultUtil.setResult(false, "策略不存在");
        }

        //全选 全部推送
        if (allChecked) {
            this.strategyRecommendMapper.insertStrategyRecommend(roleCodeEnum.getCode(), strategysId, strategy.getPrice());
        } else {

            if (userIds.length != 0) {

                if ("".equals(userIds[0]) && userIds.length == 1) {
                    return ResultUtil.getResult(RespCode.Code.SUCCESS);
                }

                Date now = new Date();
                List<StrategyRecommend> recommendList = new ArrayList<>(userIds.length);

                for (String userId : userIds) {

                    if ("".equals(userId)) {
                        continue;
                    }

                    StrategyRecommend addRecommend = new StrategyRecommend();
                    addRecommend.setStrategyId(strategysId);
                    addRecommend.setRecommendedId(0);
                    addRecommend.setBeRecommendedId(Integer.valueOf(userId));
                    addRecommend.setBeRecommendedRoleType(roleCodeEnum.getCode());
                    addRecommend.setPushPrice(strategy.getPrice());
                    addRecommend.setPushState(PushStateEnum.PUSH.getCode());
                    addRecommend.setPushDate(now);
                    addRecommend.setCreateTime(now);
                    addRecommend.setUpdateTime(now);
                    addRecommend.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());
                    recommendList.add(addRecommend);
                }

                this.strategyRecommendMapper.insertList(recommendList);
            }
        }


        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }

}
