package com.aq.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.IsDeleteEnum;
import com.aq.core.constant.PageConstant;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.exception.BizException;
import com.aq.facade.contant.CollectionStateEnum;
import com.aq.facade.contant.PushStateEnum;
import com.aq.facade.dto.*;
import com.aq.facade.entity.Adviser;
import com.aq.facade.entity.AdviserCollection;
import com.aq.facade.entity.AdviserPurchase;
import com.aq.facade.entity.AdviserRecommend;
import com.aq.facade.service.IAdviserRecommendService;
import com.aq.facade.vo.*;
import com.aq.mapper.AdviserCollectionMapper;
import com.aq.mapper.AdviserMapper;
import com.aq.mapper.AdviserPurchaseMapper;
import com.aq.mapper.AdviserRecommendMapper;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：
 * @author： 张霞
 * @createTime： 2018/03/07
 * @Copyright @2017 by zhangxia
 */
@Slf4j
@Service(version = "1.0.0")
public class AdviserRecommendServiceImpl implements IAdviserRecommendService {

    @Autowired
    private AdviserMapper adviserMapper;

    @Autowired
    private AdviserCollectionMapper adviserCollectionMapper;

    @Autowired
    private AdviserRecommendMapper adviserRecommendMapper;

    @Autowired
    private AdviserPurchaseMapper adviserPurchaseMapper;


    /**
     * @author: zhangxia
     * @desc:客户经理分页查询投顾汇列表接口
     * @params: [adviserQueryDTO, pageBean]
     * @methodName:getListAdviserPage
     * @date: 2018/3/7 0007 上午 11:24
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result getListAdviserPage(AdviserQueryDTO adviserQueryDTO, PageBean pageBean) {
        log.info("分页查询精品列表参数为 adviserQueryDTO:{},pageBean:{}", JSON.toJSONString(adviserQueryDTO), JSON.toJSONString(pageBean));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (pageBean != null) {
                pageBean.setPageSize(pageBean.getPageSize() == 0 ? PageConstant.PAGE_SIZE : pageBean.getPageSize());
                pageBean.setPageNum(pageBean.getPageNum() == 0 ? PageConstant.PAGE_NUM : pageBean.getPageNum());
                PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            }
            String message = adviserQueryDTO.validateForm();
            if (message != null) {
                return ResultUtil.setResult(false, message);
            }
            if (StringUtils.isEmpty(message)) {
                List<AdviserListVO> listVOS = adviserMapper.listAdviserPage(adviserQueryDTO);
                PageInfo<AdviserListVO> pageInfo = new PageInfo<>(listVOS);
                result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
            } else {
                result.setMessage(message);
            }
        } catch (Exception e) {
            log.info("客户经理分页查询投顾汇列表处理异常e={}", e);
        }
        log.info("客户经理分页查询投顾汇列表处理结果result={}", JSON.toJSONString(result));
        return result;
    }


    /**
     * @author: zhangxia
     * @desc: 查询投顾详情
     * @params: [dto]
     * @methodName:getAdviserDetailVO
     * @date: 2018/3/7 0007 下午 16:04
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result getAdviserDetailVO(AdviserDetailDTO dto) {
        log.info("查询投顾详情 service 入参参数={}", JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String message = dto.validateForm();
            if (StringUtils.isEmpty(message)) {
                //判断是否是自己创建的
                Adviser adviser = adviserMapper.selectByPrimaryKey(dto.getAdviserId());
                if (!(adviser.getManagerId().intValue() == dto.getPurchaserId().intValue()
                        && dto.getPurchaserType().equals(RoleCodeEnum.MANAGER.getCode()))){
                    //判断当前用户是否购买过投顾记录
                    AdviserPurchase adviserPurchase = new AdviserPurchase();
                    adviserPurchase.setPurchaserId(dto.getPurchaserId());
                    adviserPurchase.setPurchaserType(dto.getPurchaserType());
                    adviserPurchase.setAdviserId(dto.getAdviserId());
                    List<AdviserPurchase> adviserPurchases = adviserPurchaseMapper.select(adviserPurchase);
                    if (CollectionUtils.isEmpty(adviserPurchases)){
                        return ResultUtil.setResult(false, "用户尚未购买该投顾");
                    }else {
                        //判断是否过期
                        Integer existExpiry = DateUtils.truncatedCompareTo(adviserPurchases.get(0).getExpiryTime(), new Date(), Calendar.DATE);
                        if (existExpiry < 1) {
                            return ResultUtil.setResult(false, "用户购买的投顾已经过期");
                        }
                    }
                }
                log.info("投顾id={}，是角色rolecode={}，的managerId={} 自己创建的",dto.getAdviserId(),dto.getPurchaserType(),dto.getPurchaserId());
                AdviserDetailVO adviserDetailVO = adviserMapper.getAdviserDetailVO(dto);
                if (Objects.isNull(adviserDetailVO)){
                    result.setMessage("用户尚未购买此投顾");
                }else {
                    result = ResultUtil.getResult(RespCode.Code.SUCCESS, adviserDetailVO);
                }
            } else {
                result.setMessage(message);
            }
        } catch (Exception e) {
            log.info("查询投顾详情 处理异常e={}", e);
        }
        log.info("查询投顾详情 处理结果result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @author: zhangxia
     * @desc: 收藏者收藏投顾汇
     * @params: [dto]
     * @methodName:collectionAdviser
     * @date: 2018/3/7 0007 下午 18:19
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result collectionAdviser(AdviserCollectionDTO dto) {

        log.info("收藏者收藏投顾service入参参数collectionStrategyDTO={}", JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String message = dto.validateForm();
            if (StringUtils.isEmpty(message)) {
                AdviserCollection adviserCollection = new AdviserCollection();
                adviserCollection.setAdviserId(dto.getAdviserId());
                adviserCollection.setCollectionerId(dto.getCollectionerId());
                adviserCollection.setCollectionerType(dto.getCollectionerType());
                List<AdviserCollection> adviserCollections = adviserCollectionMapper.select(adviserCollection);
                if (adviserCollections.size() > 0) {
                    //有记录时进行更新操作
                    adviserCollection = adviserCollections.get(0);
                    if (adviserCollection.getCollectionState().equals(CollectionStateEnum.COLLECTION.getCode())) {
                        //已经收藏的
                        result.setMessage("收藏者收藏投顾，请勿重复收藏");
                        return result;
                    }
                    adviserCollection.setCollectionState(CollectionStateEnum.COLLECTION.getCode());
                    adviserCollection.setUpdateTime(new Date());
                    if (adviserCollectionMapper.updateByPrimaryKey(adviserCollection) > 0) {
                        result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                    }
                } else {
                    //没有记录时进行添加操作
                    adviserCollection.setCreateTime(new Date());
                    adviserCollection.setCollectionState(CollectionStateEnum.COLLECTION.getCode());
                    if (adviserCollectionMapper.insert(adviserCollection) > 0) {
                        result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                    } else {
                        result.setMessage("收藏者收藏投顾数据库插入失败");
                    }
                }

            } else {
                log.info("收藏者收藏投顾service入参参数有误message={}", message);
                result.setMessage(message);
            }
        } catch (Exception e) {
            log.info("收藏者收藏投顾service处理异常e={}", e);
        }
        log.info("收藏者收藏投顾service处理结果result={}", JSON.toJSONString(result));
        return result;
    }


    /**
     * @author: zhangxia
     * @desc: 更新收藏者收藏投顾的数据
     * @params: [dto]
     * @methodName:updateCollectionAdviser
     * @date: 2018/3/7 0007 下午 18:24
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result updateCollectionAdviser(AdviserCollectionUpdateDTO dto) {

        log.info("更新收藏者收藏投顾入参参数dto={}", JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String message = dto.validateForm();
            if (StringUtils.isEmpty(message)) {
                AdviserCollection adviserCollection = new AdviserCollection();
                adviserCollection.setCollectionerType(dto.getCollectionerType());
                adviserCollection.setCollectionerId(dto.getCollectionerId());
                adviserCollection.setAdviserId(dto.getAdviserId());
//                strategyCollectionMapper.selectByPrimaryKey(dto.getId());
                adviserCollection = adviserCollectionMapper.selectOne(adviserCollection);
                if (Objects.isNull(adviserCollection)) {
                    result.setMessage("更新收藏者收藏投顾,数据查询不存在");
                } else {
                    adviserCollection.setCollectionState(CollectionStateEnum.NOT_COLLECTION.getCode());
                    adviserCollection.setUpdateTime(new Date());
                    if (adviserCollectionMapper.updateByPrimaryKey(adviserCollection) > 0) {
                        result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                    } else {
                        result.setMessage("更新收藏者收藏投顾,数据库更新失败");
                    }
                }
            } else {
                result.setMessage(message);
            }
        } catch (Exception e) {
            log.info("更新收藏者收藏投顾处理结果处理异常e={}", e);
        }
        log.info("更新收藏者收藏投顾处理结果result={}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public Result getRecommendAdviser(AdviserQueryRecommendDTO adviserQueryRecommendDTO) {
        log.info("获取投顾推荐记录数据，adviserQueryRecommendDTO={}", JSON.toJSONString(adviserQueryRecommendDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            AdviserRecommend adviserRecommend = new AdviserRecommend();
            BeanUtils.copyProperties(adviserQueryRecommendDTO,adviserRecommend);
            adviserRecommend.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());
            List<AdviserRecommend> list = adviserRecommendMapper.select(adviserRecommend);
            if (list.size() != 1) {
                log.info("获取投顾推荐记录存在多条数据，有误");
            } else {
                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                result.setData(list.get(0));
            }
        } catch (Exception e) {
            log.error("获取策略推荐记录数据处理结果异常e={}", e);
        }
        return result;
    }

    /**
     * @author: zhangxia
     * @desc: 客户经理推荐投顾时获取客户列表
     * @params: [dto]
     * @methodName:recommendAdviserGetCustomers
     * @date: 2018/3/8 0008 下午 14:23
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result recommendAdviserGetCustomers(AdviserCustomListDTO dto,PageBean pageBean) {
        log.info("客户经理推荐投顾时获取客户信息列表入参参数dto={},pageBean = {}",JSON.toJSONString(dto),JSON.toJSONString(pageBean));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (pageBean != null) {
                pageBean.setPageSize(pageBean.getPageSize() == 0 ? PageConstant.PAGE_SIZE : pageBean.getPageSize());
                pageBean.setPageNum(pageBean.getPageNum() == 0 ? PageConstant.PAGE_NUM : pageBean.getPageNum());
                PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            }
            String message = dto.validateForm();
            if (StringUtils.isEmpty(message)){
                List<AdviserCustomListVO> listVOS = adviserRecommendMapper.getAdviserCustomers(dto);
                PageInfo<AdviserCustomListVO> pageInfo = new PageInfo<>(listVOS);
                result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
            }else {
                result.setMessage(message);
            }
        } catch (Exception e) {
            log.info("客户经理推荐投顾时获取客户信息列表处理异常e={}",e);
        }
        log.info("客户经理推荐投顾时获取客户信息列表处理结果result={}",JSON.toJSONString(result));
        return result;
    }

    /**
     * @author: zhangxia
     * @desc: 客户经理推荐投顾给客户
     * @params: [dto]
     * @methodName:recommendAdviser
     * @date: 2018/3/8 0008 下午 15:43
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Transactional
    @Override
    public Result recommendAdviser(AdviserRecommendDTO dto) {

        log.info("客户经理推荐投顾给客户入参参数dto={}",JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String message = dto.validateForm();
            if (StringUtils.isEmpty(message)){
                //进行数据的区分
                List<AdviserRecommend> inserts = new ArrayList<>();
                List<AdviserRecommend> updates = new ArrayList<>();
                List<AdviserRecommendCustomDTO> adviserRecommendCustomDTOS = dto.getAdviserRecommendCustomDTOS();

                AdviserCustomListDTO adviserCustomListDTO = new AdviserCustomListDTO();
                adviserCustomListDTO.setManagerId(dto.getManagerId());
                adviserCustomListDTO.setAdviserId(dto.getAdviserId());
                List<AdviserCustomListVO> listVOS = adviserRecommendMapper.getAdviserCustomers(adviserCustomListDTO);

                for (AdviserRecommendCustomDTO customDTO:
                        adviserRecommendCustomDTOS) {
                    if (customDTO.isPush()){
                        //进行推送的（存在就进行更新操作，不存在就进行插入操作）
                        boolean flag= false;
                        int id = 0;
                        //检验是否推送过
                        for (AdviserCustomListVO adviserCustomListVO:
                                listVOS) {
                            //判断是否给客户推送的
                            if (adviserCustomListVO.getCustomerId().intValue()==customDTO.getCustomerId()
                                    && (adviserCustomListVO.getId() != null)
                                    && adviserCustomListVO.getBeRecommendedRoleType().equals(customDTO.getBeRecommendedRoleType())){
                                log.info("推送过的客户customDTO.getCustomerId={}",customDTO.getCustomerId());
                                flag = true;
                                id = adviserCustomListVO.getId();
                            }
                            //是否给客户经理推送的
                            if (adviserCustomListVO.getCustomerId().intValue()==customDTO.getCustomerId()
                                    && (adviserCustomListVO.getId() != null)
                                    && adviserCustomListVO.getBeRecommendedRoleType().equals(customDTO.getBeRecommendedRoleType())){
                                log.info("推送过的客户经理customDTO.getCustomerId={}",customDTO.getCustomerId());
                                flag = true;
                                id = adviserCustomListVO.getId();
                            }

                        }
                        if (flag){
                            //已经推荐过的进行价格更新操作
                            AdviserRecommend adviserRecommend = new AdviserRecommend();

                            adviserRecommend.setId(id);
                            adviserRecommend.setPushPrice(customDTO.getPushPrice());
                            adviserRecommend.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());

                            updates.add(adviserRecommend);
                        }else {
                            //没有推荐过的要进行推荐的进行插入操作
                            AdviserRecommend adviserRecommend = new AdviserRecommend();

                            adviserRecommend.setBeRecommendedId(customDTO.getCustomerId());
                            adviserRecommend.setBeRecommendedRoleType(customDTO.getBeRecommendedRoleType());
                            adviserRecommend.setPushPrice(customDTO.getPushPrice());
                            adviserRecommend.setCreateTime(new Date());
                            adviserRecommend.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());
                            adviserRecommend.setPushState(PushStateEnum.PUSH.getCode());
                            adviserRecommend.setAdviserId(dto.getAdviserId());
                            adviserRecommend.setRecommendedId(dto.getManagerId());
                            adviserRecommend.setPushDate(new Date());

                            inserts.add(adviserRecommend);
                        }

                    }else {
                        for (AdviserCustomListVO adviserCustomListVO:
                                listVOS) {
                            if (adviserCustomListVO.getCustomerId().intValue()==customDTO.getCustomerId()){
                                //已经推荐过的进行删除更新操作
                                log.info("已经推荐过的进行删除更新操作customDTO.getCustomerId()={}",customDTO.getCustomerId());
                                AdviserRecommend adviserRecommend = new AdviserRecommend();
                                adviserRecommend.setId(adviserCustomListVO.getId());
                                adviserRecommend.setPushPrice(customDTO.getPushPrice());
                                adviserRecommend.setIsDelete(IsDeleteEnum.IS_DELETE_ENUM.getCode());
                                updates.add(adviserRecommend);
                            }
                        }

                    }
                }

                log.info("inserts={}",inserts.size()>0?JSON.toJSONString(inserts):"没有需要插入的数据");
                log.info("updates= {}",updates.size()>0?JSON.toJSONString(updates):"没有需要更新的数据");
                //进行数据库操作--插入

                if(inserts.size()>0){
                    if (adviserRecommendMapper.insertList(inserts)>0){
                        log.info("客户经理推荐客户数据插入操作成功");
                        result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                    }else {
                        throw new BizException("客户经理推荐客户数据插入操作失败");
                    }
                }else {
                    log.info("客户经理推荐客户数据插入操作没有数据");
                    result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                }
                //进行数据库操作--更新推荐记录
                if (updates.size()>0){
                    if (adviserRecommendMapper.updateAdviserRecommendBatch(updates)>0){
                        log.info("客户经理推荐客户数据更新操作成功");
                        result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                    }else {
                        throw new BizException("客户经理推荐客户数据更新操作失败");
                    }
                }else {
                    log.info("客户经理推荐客户数据更新操作没有数据");
                    result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                }
            }else {
                result.setMessage(message);
            }
        } catch (Exception e) {
            log.info("客户经理推荐策略给客户处理异常={}",e);
            result.setMessage(e.getMessage());
            throw new BizException(e.getMessage());
        }
        log.info("客户经理推荐策略给客户处理结果={}",JSON.toJSONString(result));
        return result;
    }


    /**
     * @author: zhangxia
     * @desc: 投顾推荐记录更新微信推送状态
     * @params: [dto]
     * @methodName:updateRecommendAdviserPushstate
     * @date: 2018/3/8 0008 下午 18:16
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result updateRecommendAdviserPushstate(AdviserRecommendUpdateDTO dto) {
        log.info("投顾推荐记录更新微信推送状态service 入参参数dto={}",JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String msg = dto.validateForm();
            if (StringUtils.isEmpty(msg)){
                AdviserRecommend adviserRecommend = adviserRecommendMapper.selectByPrimaryKey(dto.getId());
                if (Objects.isNull(adviserRecommend)){
                    result.setMessage("传入参数有误，没有相关数据");
                    log.info("投顾推荐记录更新微信推送状态:{} 传入参数有误，没有相关数据",JSON.toJSONString(dto));
                }else {
                    adviserRecommend.setPushState(dto.getPushState());
                    adviserRecommend.setUpdateTime(new Date());
                    if (adviserRecommendMapper.updateByPrimaryKeySelective(adviserRecommend)>0){
                        result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                    }else {
                        log.info("投顾推荐记录更新微信推送状态:{} 更新失败",JSON.toJSONString(dto));
                    }
                }
            }else {
                result.setMessage(msg);
            }
        } catch (Exception e) {
            log.info("投顾推荐记录更新微信推送状态处理结果异常e={}",e);
        }
        log.info("投顾推荐记录更新微信推送状态处理结果result={}",JSON.toJSONString(result));
        return result;
    }

    /**
     * @author: zhangxia
     * @desc: 查询推荐投顾给客户列表
     * @params: [dto]
     * @methodName:recommendAdviserList
     * @date: 2018/3/9 0009 下午 14:05
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result recommendAdviserList(AdviserQueryRecommendDTO dto) {
        log.info("查询推荐投顾给客户列表 service 入参参数dto={}",JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);

        try {
            if (Objects.isNull(dto.getRecommendedId())){
                result.setMessage("查询参数有误，推荐人id不能为空");
            }else {
                List<AdviserRecommendVO> adviserRecommendVOS = adviserRecommendMapper.recommendAdviserList(dto);
                result = ResultUtil.getResult(RespCode.Code.SUCCESS,adviserRecommendVOS);
            }
        } catch (Exception e) {
            log.info("查询推荐投顾给客户列表 处理异常e={}",e);
        }
        log.info("查询推荐投顾给客户列表 处理结果result={}",JSON.toJSONString(result));
        return result;
    }


    /**
     * @author: zhangxia
     * @desc: 删除客户经理推荐给客户的投顾
     * @params: [updateDTO]
     * @methodName:updateRecommendAdviser
     * @date: 2018/3/9 0009 下午 16:18
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    public Result updateRecommendAdviserDelete(AdviserRecommendUpdateDTO updateDTO){
        log.info("删除客户经理推荐给客户的投顾 入参参数updateDTO={}",JSON.toJSONString(updateDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            AdviserRecommend adviserRecommend = adviserRecommendMapper.selectByPrimaryKey(updateDTO.getId());
            if (Objects.isNull(adviserRecommend)){
                result.setMessage("请勿非法操作，没有相关数据");
            }else {
                adviserRecommend.setIsDelete(IsDeleteEnum.IS_DELETE_ENUM.getCode());
                adviserRecommend.setUpdateTime(new Date());
                if (adviserRecommendMapper.updateByPrimaryKeySelective(adviserRecommend)>0){
                    result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                }else {
                    result.setMessage("更新操作失败");
                }
            }
        } catch (Exception e) {
            log.info("删除客户经理推荐给客户的投顾 处理结果异常e={}",e);
        }
        log.info("删除客户经理推荐给客户的投顾 处理结果result={}",JSON.toJSONString(result));
        return result;
    }

    /**
     * @auth: zhangxia
     * @desc: 实时刷新查询投顾今日收益数据
     * @methodName: listAdviserRefresh
     * @params: [dto]
     * @return: com.aq.util.result.Result
     * @createTime: 2018/4/12 14:43
     * @version:2.1.6
     */
    @Override
    public Result listAdviserRefresh(AdviserListRefreshDTO dto) {
        log.info("实时刷新查询投顾今日收益数据 service 入参参数dto={}",JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            List<AdviserListRefreshVO> adviserListRefreshVOS = adviserMapper.listAdviserRefreshVO(dto);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS,adviserListRefreshVOS);
        } catch (Exception e) {
            log.info("实时刷新查询投顾今日收益数据 service处理异常e={}",e);
            result.setMessage("实时刷新查询投顾今日收益数据 获取数据异常");
        }
        log.info("实时刷新查询投顾今日收益数据 service 处理结果result={}",JSON.toJSONString(result));
        return result;
    }
}
