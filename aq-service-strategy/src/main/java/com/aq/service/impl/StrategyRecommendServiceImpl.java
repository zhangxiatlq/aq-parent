package com.aq.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.IsDeleteEnum;
import com.aq.core.constant.PageConstant;
import com.aq.core.exception.BizException;
import com.aq.facade.contant.CollectionStateEnum;
import com.aq.facade.contant.PushStateEnum;
import com.aq.facade.dto.*;
import com.aq.facade.entity.StrategyCollection;
import com.aq.facade.entity.StrategyRecommend;
import com.aq.facade.service.IStrategyRecommendService;
import com.aq.facade.vo.StrategyCustomListVO;
import com.aq.mapper.StrategyCollectionMapper;
import com.aq.mapper.StrategyRecommendMapper;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author:zhangxia
 * @createTime:10:10 2018-2-12
 * @version:1.0
 * @desc:客户推荐策略service接口实现
 */
@Slf4j
@Service(version = "1.0.0", timeout = 60000)
public class StrategyRecommendServiceImpl implements IStrategyRecommendService {

    @Autowired
    StrategyCollectionMapper strategyCollectionMapper;

    @Autowired
    StrategyRecommendMapper strategyRecommendMapper;

    /**
     * @Creater: zhangxia
     * @description：客户经理收藏策略
     * @methodName: collectionStrategy
     * @params: [collectionStrategyDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 10:12 2018-2-12
     */
    @Override
    public Result collectionStrategy(CollectionStrategyDTO collectionStrategyDTO) {
        log.info("客户经理收藏策略service入参参数collectionStrategyDTO={}", JSON.toJSONString(collectionStrategyDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String message = collectionStrategyDTO.validateForm();
            if (StringUtils.isEmpty(message)){
                StrategyCollection strategyCollection = new StrategyCollection();
                strategyCollection.setManagerId(collectionStrategyDTO.getManagerId());
                strategyCollection.setStrategyId(collectionStrategyDTO.getStrategyId());
                List<StrategyCollection> strategyCollections = strategyCollectionMapper.select(strategyCollection);
                if (strategyCollections.size()>0){
                    //有记录时进行更新操作
                    strategyCollection = strategyCollections.get(0);
                    if (strategyCollection.getCollectionState().equals(CollectionStateEnum.COLLECTION.getCode())){
                        //已经收藏的
                        result.setMessage("请勿重复收藏");
                        return result;
                    }
                    strategyCollection.setCollectionState(CollectionStateEnum.COLLECTION.getCode());
                    strategyCollection.setUpdateTime(new Date());
                    if (strategyCollectionMapper.updateByPrimaryKey(strategyCollection)>0){
                        result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                    }
                } else {
                    //没有记录时进行添加操作
                    strategyCollection.setCreateTime(new Date());
                    strategyCollection.setCollectionState(CollectionStateEnum.COLLECTION.getCode());
                    if (strategyCollectionMapper.insert(strategyCollection)>0){
                        result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                    }else {
                        result.setMessage("客户经理收藏策略数据库插入失败");
                    }
                }

            }else {
                log.info("客户经理收藏策略service入参参数有误message={}",message);
                result.setMessage(message);
            }
        } catch (Exception e) {
            log.info("客户经理收藏策略service处理异常e={}",e);
        }
        log.info("客户经理收藏策略service处理结果result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @Creater: zhangxia
     * @description：更新客户经理收藏策略
     * @methodName: updateCollectionStrategy
     * @params: [dto]
     * @return: com.aq.util.result.Result
     * @createTime: 10:47 2018-2-12
     */
    @Override
    public Result updateCollectionStrategy(CollectionStrategyUpdateDTO dto) {
        log.info("更新客户经理收藏策略入参参数dto={}",JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String message = dto.validateForm();
            if (StringUtils.isEmpty(message)){
                StrategyCollection strategyCollection = new StrategyCollection();
                strategyCollection.setStrategyId(dto.getStrategyId());
                strategyCollection.setManagerId(dto.getManagerId());
//                strategyCollectionMapper.selectByPrimaryKey(dto.getId());
                strategyCollection = strategyCollectionMapper.selectOne(strategyCollection);
                if (Objects.isNull(strategyCollection)){
                    result.setMessage("数据查询不存在");
                }else {
                    strategyCollection.setCollectionState(CollectionStateEnum.NOT_COLLECTION.getCode());
                    strategyCollection.setUpdateTime(new Date());
                    if (strategyCollectionMapper.updateByPrimaryKey(strategyCollection)>0){
                        result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                    }else {
                        result.setMessage("数据库更新失败");
                    }
                }
            }else {
                result.setMessage(message);
            }
        } catch (Exception e) {
            log.info("更新客户经理收藏策略处理异常e={}",e);
        }
        log.info("更新客户经理收藏策略处理结果result={}",JSON.toJSONString(result));
        return result;
    }

    /**
     * @Creater: zhangxia
     * @description：客户经理推荐策略时获取客户信息列表
     * @methodName: getStrategyCustomers
     * @params: [dto,pageBean]
     * @return: com.aq.util.result.Result
     * @createTime: 14:55 2018-2-12
     */
    @Override
    public Result getStrategyCustomers(StrategyCustomListDTO dto, PageBean pageBean) {
        log.info("客户经理推荐策略时获取客户信息列表入参参数dto={},pageBean = {}",JSON.toJSONString(dto),JSON.toJSONString(pageBean));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (pageBean != null) {
                pageBean.setPageSize(pageBean.getPageSize() == 0 ? PageConstant.PAGE_SIZE : pageBean.getPageSize());
                pageBean.setPageNum(pageBean.getPageNum() == 0 ? PageConstant.PAGE_NUM : pageBean.getPageNum());
                PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            }
            String message = dto.validateForm();
            if (StringUtils.isEmpty(message)){
                List<StrategyCustomListVO> listVOS = strategyRecommendMapper.getStrategyCustomers(dto);
                PageInfo<StrategyCustomListVO> pageInfo = new PageInfo<>(listVOS);
                result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
            }else {
                result.setMessage(message);
            }
        } catch (Exception e) {
            log.info("客户经理推荐策略时获取客户信息列表处理异常e={}",e);
        }
        log.info("客户经理推荐策略时获取客户信息列表处理结果result={}",JSON.toJSONString(result));
        return result;
    }

    /**
     * @Creater: zhangxia
     * @description：客户经理推荐策略给客户
     * @methodName: recommendStrategy
     * @params: [strategyRecommendDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 15:48 2018-2-12
     */
    @Transactional
    @Override
    public Result recommendStrategy(StrategyRecommendDTO strategyRecommendDTO) {
        log.info("客户经理推荐策略给客户入参参数strategyRecommendDTO={}",JSON.toJSONString(strategyRecommendDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String message = strategyRecommendDTO.validateForm();
            if (StringUtils.isEmpty(message)){
                //进行数据的区分
                List<StrategyRecommend> inserts = new ArrayList<>();
                List<StrategyRecommend> updates = new ArrayList<>();
                List<StrategyCustomDTO> strategyCustomDTOS = strategyRecommendDTO.getStrategyCustomDTOS();
                StrategyCustomListDTO dto = new StrategyCustomListDTO();
                dto.setManagerId(strategyRecommendDTO.getManagerId());
                dto.setStrategyId(strategyRecommendDTO.getStrategyId());
                List<StrategyCustomListVO> listVOS = strategyRecommendMapper.getStrategyCustomers(dto);

                for (StrategyCustomDTO customDTO:
                        strategyCustomDTOS) {
                    if (customDTO.isPush()){
                        //进行推送的（存在就进行更新操作，不存在就进行插入操作）
                        boolean flag= false;
                        int id = 0;
                        //检验是否推送过
                        for (StrategyCustomListVO strategyCustomListVO:
                        listVOS) {
                            if (strategyCustomListVO.getCustomerId().intValue()==customDTO.getCustomerId() && (strategyCustomListVO.getId() != null)){
                                log.info("推送过的customDTO.getCustomerId={}",customDTO.getCustomerId());
                                flag = true;
                                id = strategyCustomListVO.getId();
                            }
                        }
                        if (flag){
                            //已经推荐过的进行价格更新操作
                            StrategyRecommend strategyRecommend = new StrategyRecommend();

                            strategyRecommend.setId(id);
                            strategyRecommend.setPushPrice(customDTO.getPushPrice());
                            strategyRecommend.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());

                            updates.add(strategyRecommend);
                        }else {
                            //没有推荐过的要进行推荐的进行插入操作
                            StrategyRecommend strategyRecommend = new StrategyRecommend();

                            strategyRecommend.setBeRecommendedId(customDTO.getCustomerId());
                            strategyRecommend.setBeRecommendedRoleType(strategyRecommendDTO.getBeRecommendedRoleType());
                            strategyRecommend.setPushPrice(customDTO.getPushPrice());
                            strategyRecommend.setCreateTime(new Date());
                            strategyRecommend.setIsDelete(IsDeleteEnum.IS_NOT_DELETE_ENUM.getCode());
                            strategyRecommend.setPushState(PushStateEnum.PUSH.getCode());
                            strategyRecommend.setStrategyId(strategyRecommendDTO.getStrategyId());
                            strategyRecommend.setRecommendedId(strategyRecommendDTO.getManagerId());
                            strategyRecommend.setPushDate(new Date());

                            inserts.add(strategyRecommend);
                        }

                    }else {
                        for (StrategyCustomListVO strategyCustomListVO:
                                listVOS) {
                            if (strategyCustomListVO.getCustomerId().intValue()==customDTO.getCustomerId()){
                                //已经推荐过的进行删除更新操作
                                log.info("已经推荐过的进行删除更新操作customDTO.getCustomerId()={}",customDTO.getCustomerId());
                                StrategyRecommend strategyRecommend = new StrategyRecommend();
                                strategyRecommend.setId(strategyCustomListVO.getId());
                                strategyRecommend.setPushPrice(customDTO.getPushPrice());
                                strategyRecommend.setIsDelete(IsDeleteEnum.IS_DELETE_ENUM.getCode());
                                updates.add(strategyRecommend);
                            }
                        }

                    }
                }

                log.info("inserts={}",inserts.size()>0?JSON.toJSONString(inserts):"没有需要插入的数据");
                log.info("updates= {}",updates.size()>0?JSON.toJSONString(updates):"没有需要更新的数据");
                //进行数据库操作--插入

                if(inserts.size()>0){
                    if (strategyRecommendMapper.insertList(inserts)>0){
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
                    if (strategyRecommendMapper.updateStrategyRecommendBatch(updates)>0){
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
     * @Creater: zhangxia
     * @description：获取策略推荐记录数据
     * @methodName: getRecommendStrategy
     * @params: [dto]
     * @return: com.aq.util.result.Result
     * @createTime: 16:57 2018-2-26
     */
    @Override
    public Result getRecommendStrategy(QueryStrategyRecommendDTO dto) {
        log.info("获取策略推荐记录数据入参参数dto={}",JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            StrategyRecommend strategyRecommend = new StrategyRecommend();
            strategyRecommend.setStrategyId(dto.getStrategyId());
            strategyRecommend.setRecommendedId(dto.getRecommendedId());
            strategyRecommend.setBeRecommendedId(dto.getBeRecommendedId());
            strategyRecommend.setBeRecommendedRoleType(dto.getBeRecommendedRoleType());
            List<StrategyRecommend> list = strategyRecommendMapper.select(strategyRecommend);
            if (list.size()>1){
                log.info("获取策略推荐记录数据存在多条数据，有误，入参={}",JSON.toJSONString(dto));
            }else {
                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
                result.setData(list.get(0));
            }
        } catch (Exception e) {
            log.info("获取策略推荐记录数据处理结果异常e={}",e);
        }
        return result;
    }
}
