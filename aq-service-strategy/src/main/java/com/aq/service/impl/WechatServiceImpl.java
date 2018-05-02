package com.aq.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.ColorEnum;
import com.aq.core.constant.OrderStateEnum;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.wechat.constant.WechatTemplateEnum;
import com.aq.core.wechat.dto.WechatPushTemplateDTO;
import com.aq.core.wechat.template.WeChatPushTemplateEnum;
import com.aq.core.wechat.util.WeChatPushUtil;
import com.aq.core.wechat.util.WeChatSignatureUtil;
import com.aq.facade.contant.DirectionEnum;
import com.aq.facade.dto.AddPayTradeDTO;
import com.aq.facade.dto.RenewStrategyDTO;
import com.aq.facade.dto.StrategyDetailDTO;
import com.aq.facade.dto.WechatStrategyQueryDTO;
import com.aq.facade.entity.*;
import com.aq.facade.service.IWechatStrategyService;
import com.aq.facade.vo.StrategyOrderVO;
import com.aq.facade.vo.StrategyWechatDetailVO;
import com.aq.facade.vo.WechatStrategyQueryVO;
import com.aq.mapper.*;
import com.aq.util.date.DateUtil;
import com.aq.util.order.IdGenerator;
import com.aq.util.order.OrderBizCode;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 微信接口实现类
 * @date 2018/1/22
 * @copyright by xkw
 */
@Slf4j
@Service(version = "1.0.0")
public class WechatServiceImpl implements IWechatStrategyService {

    @Autowired
    private WechatStrategyMapper wechatStrategyMapper;

    @Autowired
    private StrategyPushMapper strategyPushMapper;

    @Autowired
    private StrategyPushUserMapper strategyPushUserMapper;


    @Autowired
    private StrategyOrderMapper orderMapper;

    @Autowired
    private StrategyPayTradeMapper payTradeMapper;

    @Autowired
    private StrategyPurchaseMapper strategyPurchaseMapper;

    @Autowired
    private StrategyMapper strategyMapper;

    @Override
    public Result pushWechantTemplete(String pushIds, String templateId) throws Exception {
        log.info("策略微信推送消息templateId={},pushIds={} ", templateId, pushIds);
        if (pushIds == null) {
            return ResultUtil.setResult(false, "推送信息id不能为空");
        }

        if (templateId == null) {
            return ResultUtil.setResult(false, "模板id不能为空");
        }

        Example example = new Example(StrategyPushUser.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(pushIds.split(",")));

        List<StrategyPush> strategyPush = this.strategyPushMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(strategyPush)) {
            return ResultUtil.setResult(false, "不存在的推送信息");
        }

        Map<String, List<StrategyPush>> map = new HashMap<>(16);
        List<StrategyPush> list;
        for (StrategyPush push : strategyPush) {
            list = map.get(push.getStrategyId().toString());
            if (CollectionUtils.isNotEmpty(list)) {
                list.add(push);
            } else {
                list = new ArrayList<>();
                list.add(push);
            }
            map.put(push.getStrategyId().toString(), list);
        }

        List<StrategyPushUser> pushUsers = new ArrayList<>();
        String accessToken = WeChatSignatureUtil.getAccessToken();

        for (String strategyId : map.keySet()) {
            List<String> openIds = this.wechatStrategyMapper.selectStrategyPushIds(Integer.valueOf(strategyId));
            if (CollectionUtils.isEmpty(openIds)) {
                continue;
            }

            WechatPushTemplateDTO wechatPushTemplateDTO = new WechatPushTemplateDTO();
            wechatPushTemplateDTO.setTemplate_id(templateId);
            if (WechatTemplateEnum.FIVE.getTemplateId().equals(templateId)) {
                Strategy strategy = strategyMapper.selectByPrimaryKey(Integer.valueOf(strategyId));
                WechatPushTemplateDTO.Data first = WechatPushTemplateDTO.Data.builder()
                        .value("策略名称: " + strategy.getStrategyName());
                WechatPushTemplateDTO.Data remark = WechatPushTemplateDTO.Data.builder()
                        .value(WeChatPushTemplateEnum.PROMPT.getMessage());
                WechatPushTemplateDTO.Data keyword3 = WechatPushTemplateDTO.Data.builder().
                        value(DateUtils.formatDate(new Date(), DateUtil.YYYYMMDD));
                WechatPushTemplateDTO.Data keyword1 = WechatPushTemplateDTO.Data.builder().color(ColorEnum.RED.getHex());
                WechatPushTemplateDTO.Data keyword2 = WechatPushTemplateDTO.Data.builder().color(ColorEnum.GREEN.getHex());
                List<String> transferred = new ArrayList<>();
                List<String> callOut = new ArrayList<>();
                for (StrategyPush pushValue : map.get(strategyId)) {
                    String pushString = pushValue.getSharesName()
                            .concat("(")
                            .concat(pushValue.getSharesCode())
                            .concat(")");
                    if (pushValue.getDirection().equals(DirectionEnum.SELL.getCode())) {
                        callOut.add(pushString);
                    } else {
                        transferred.add(pushString);
                    }
                }
                if (CollectionUtils.isNotEmpty(transferred)) {
                    keyword1.setValue(String.join(",", transferred));
                } else {
                    keyword1.setValue("-");
                }
                if (CollectionUtils.isNotEmpty(callOut)) {
                    keyword2.setValue(String.join(",", callOut));
                } else {
                    keyword2.setValue("-");
                }
                Map<String, WechatPushTemplateDTO.Data> dataMap = new HashMap<>();
                dataMap.put("first", first);
                dataMap.put("remark", remark);
                dataMap.put("keyword1", keyword1);
                dataMap.put("keyword2", keyword2);
                dataMap.put("keyword3", keyword3);
                wechatPushTemplateDTO.setData(dataMap);
            } else {
                return ResultUtil.setResult(false, "不存在的推送模板");
            }

            for (String openId : openIds) {
                wechatPushTemplateDTO.setTouser(openId);

                if (WeChatPushUtil.pushWechat(accessToken, wechatPushTemplateDTO)) {
                    for (StrategyPush push : map.get(strategyId)) {
                        StrategyPushUser strategyPushUser = new StrategyPushUser();
                        strategyPushUser.setPushTime(new Date());
                        strategyPushUser.setStrategyPushId(push.getId());
                        strategyPushUser.setOpenId(openId);
                        pushUsers.add(strategyPushUser);
                    }

                } else {
                    log.error("推送失败,推送dto为:{}", JSON.toJSONString(wechatPushTemplateDTO));
                }
            }

        }

        if (CollectionUtils.isNotEmpty(pushUsers)) {
            this.strategyPushUserMapper.insertList(pushUsers);
        }

        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }

    @Override
    public Result listWechatStrategyQueryVO(WechatStrategyQueryDTO dto, PageBean page) {

        if (dto.getOpenId() == null) {
            return ResultUtil.setResult(false, "openId不能为空");
        }
        log.info("微信 我的策略列表 openId:{},page:{}", dto.getOpenId(), JSON.toJSONString(page));
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<WechatStrategyQueryVO> list = wechatStrategyMapper.listWechatStrategyQueryVO(dto);
        log.info("微信 我的策略列表 查询结果={}", JSON.toJSONString(list));
        PageInfo<WechatStrategyQueryVO> pageList = new PageInfo<>(list);
        log.info("微信 我的策略列表 返回结果为 pageList:{}", JSON.toJSONString(pageList));
        return ResultUtil.getResult(RespCode.Code.SUCCESS, pageList.getList(), pageList.getTotal());
    }

    @Override
    public Result getWechantStrategyDetail(StrategyDetailDTO detailDTO) {
        log.info("微信 策略详情 detailDTO:{}", JSON.toJSONString(detailDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        detailDTO.setIsLimit("true");//需要限制
        StrategyPurchase purchase = new StrategyPurchase();
        purchase.setPurchaserId(detailDTO.getPurchaserId());
        purchase.setStrategyId(detailDTO.getStrategyId());
        purchase.setPurchaserType(detailDTO.getPurchaserType());
        List<StrategyPurchase> list = strategyPurchaseMapper.select(purchase);
        if (list.size() <= 0) {
            log.info("微信端 获取策略详情时，此策略没有购买，无法查看详情");
            result.setMessage("请先购买策略后查看");
            return result;
        }
        if (new Date().getTime() > list.get(0).getExpiryTime().getTime()) {
            log.info("微信端 获取策略详情时，此策略购买后已经到期，无法查看详情");
            result.setMessage("请先续费策略后查看");
            return result;
        }

        StrategyWechatDetailVO vo = this.wechatStrategyMapper.getStrategyWechatDetailVO(detailDTO);
        log.info("微信 策略详情 返回数据", vo);
        if (vo == null) {
            return ResultUtil.getResult(RespCode.Code.NOT_QUERY_DATA);
        }

        return ResultUtil.getResult(RespCode.Code.SUCCESS, vo);
    }


    /**
     * @Creater: zhangxia
     * @description：客户或者客户经理(微信端或者pc端)对策略进行续费或者购买
     * @methodName: renewStrategyByCustomer
     * @params: [renewStrategyDTOS]
     * @return: com.aq.util.result.Result
     * @createTime: 9:24 2018-2-13
     */
    @Override
    public Result renewStrategyByCustomer(List<RenewStrategyDTO> renewStrategyDTOS) {
        log.info("客户或者客户经理(微信端或者pc端)对策略进行续费或者购买service层入参参数renewStrategyDTO={}", JSON.toJSONString(renewStrategyDTOS));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String mainOrderNo = "";
            List<StrategyOrder> strategyOrders = new ArrayList<>();
            BigDecimal totalMoney = new BigDecimal("0");
            //由于暂时只支持单一的购买或者续费，所以取其一进行抽查是否是购买还是续费
            StrategyPurchase strategyPurchase = new StrategyPurchase();
            strategyPurchase.setPurchaserType(renewStrategyDTOS.get(0).getPurchaserType());
            strategyPurchase.setStrategyId(renewStrategyDTOS.get(0).getStrategyId());
            strategyPurchase.setPurchaserId(renewStrategyDTOS.get(0).getPurchaserId());
            List<StrategyPurchase> purchases = strategyPurchaseMapper.select(strategyPurchase);//用于判断是否已经购买过
            if (RoleCodeEnum.CUSTOMER.getCode().equals(renewStrategyDTOS.get(0).getPurchaserType())) {
                //客户进行后买或者续费
                //主订单号
                if (purchases.size() > 0) {
                    //客户进行续费策略
                    mainOrderNo = IdGenerator.nextId(OrderBizCode.CUSTOMR_RENEW_MD);
                } else {
                    //客户进行购买策略
                    mainOrderNo = IdGenerator.nextId(OrderBizCode.CUSTOMR_BUY_MD);

                }
                //数据封装
                //封装list
                for (RenewStrategyDTO dto :
                        renewStrategyDTOS) {
                    totalMoney = totalMoney.add(dto.getPurchaseMoney());
                    String orderNo = IdGenerator.nextId(purchases.size() > 0 ? OrderBizCode.CUSTOMR_RENEW_DD : OrderBizCode.CUSTOMR_BUY_DD);
                    StrategyOrder strategyOrder = new StrategyOrder();
                    BeanUtils.copyProperties(dto, strategyOrder);
                    strategyOrder.setCreateTime(new Date());
                    strategyOrder.setMainOrderNo(mainOrderNo);
                    strategyOrder.setOrderNo(orderNo);
                    strategyOrder.setOrderState(OrderStateEnum.PAYING.getCode());
                    strategyOrder.setPurchaseTime(new Date());
                    strategyOrders.add(strategyOrder);
                }


            } else if (RoleCodeEnum.MANAGER.getCode().equals(renewStrategyDTOS.get(0).getPurchaserType())) {
                //客户经理进行购买和续费
                //主订单号
                if (purchases.size() > 0) {
                    //客户经理进行续费策略
                    mainOrderNo = IdGenerator.nextId(OrderBizCode.MANAGER_RENEW_MD);
                } else {
                    //客户经理进行购买策略
                    mainOrderNo = IdGenerator.nextId(OrderBizCode.MANAGER_BUY_MD);

                }
                //数据封装
                //封装list
                for (RenewStrategyDTO dto :
                        renewStrategyDTOS) {
                    totalMoney = totalMoney.add(dto.getPurchaseMoney());
                    String orderNo = IdGenerator.nextId(purchases.size() > 0 ? OrderBizCode.MANAGER_RENEW_DD : OrderBizCode.MANAGER_BUY_DD);
                    StrategyOrder strategyOrder = new StrategyOrder();
                    BeanUtils.copyProperties(dto, strategyOrder);
                    strategyOrder.setCreateTime(new Date());
                    strategyOrder.setMainOrderNo(mainOrderNo);
                    strategyOrder.setOrderNo(orderNo);
                    strategyOrder.setOrderState(OrderStateEnum.PAYING.getCode());
                    strategyOrder.setPurchaseTime(new Date());
                    strategyOrders.add(strategyOrder);
                }

            }

            //存入数据库
            if (orderMapper.insertList(strategyOrders) > 0) {
                log.info("客户或者客户经理(微信端或者pc端)对策略进行续费或者购买时，主订单号={}，订单生成成功,strategyOrders={}", mainOrderNo, JSON.toJSONString(strategyOrders));
                //存入redis缓存

                StrategyOrderVO strategyOrderVO = new StrategyOrderVO();
                strategyOrderVO.setList(renewStrategyDTOS);
                strategyOrderVO.setMainOrderNo(mainOrderNo);
                strategyOrderVO.setTotalMoney(totalMoney);
                result = ResultUtil.getResult(RespCode.Code.SUCCESS, strategyOrderVO);
            } else {
                log.info("数据库生成订单失败");
                result.setMessage("续费订单失败");
            }
        } catch (Exception e) {
            log.info("客户或者客户经理(微信端或者pc端)对策略进行续费或者购买处理结果异常e={}", e);
        }
        log.info("客户或者客户经理(微信端或者pc端)对策略进行续费或者购买处理结果result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @Creater: zhangxia
     * @description：第三方支付回调后进行支付记录
     * @methodName: addStrategyPayTrade
     * @params: [addPayTradeDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 12:26 2018-2-13
     */
    @Override
    public Result addStrategyPayTrade(AddPayTradeDTO addPayTradeDTO) {
        log.info("第三方支付回调后进行支付记录service入参参数addPayTradeDTO={}", JSON.toJSONString(addPayTradeDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            log.info("第三方支付回调后进行支付记录service处理结果result={}", JSON.toJSONString(result));
            StrategyPayTrade strategyPayTrade = new StrategyPayTrade();
            BeanUtils.copyProperties(addPayTradeDTO, strategyPayTrade);
            strategyPayTrade.setCreaterId(0);
            strategyPayTrade.setCreateTime(new Date());
            strategyPayTrade.setRoleType(RoleCodeEnum.SYSTEM.getCode());
            if (payTradeMapper.insert(strategyPayTrade) > 0) {
                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            } else {
                result.setMessage("数据库记录失败");
            }

        } catch (Exception e) {
            log.info("第三方支付回调后进行支付记录service处理异常e={}", e);
        }
        return result;
    }

    /**
     * @Creater: zhangxia
     * @description：客户或者客户经理续费已经购买的策略
     * @methodName: updateRenewStrategy
     * @params: [renewStrategyDTOS]
     * @return: com.aq.util.result.Result
     * @createTime: 10:10 2018-2-22
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result updateRenewStrategy(List<RenewStrategyDTO> renewStrategyDTOS) {
        log.info("客户或者客户经理续费已经购买的策略入参参数renewStrategyDTOS={}", renewStrategyDTOS);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            int resultNum = strategyPurchaseMapper.updateRenewStrategyPurchase(renewStrategyDTOS);
            if (resultNum > 0) {
                log.info("客户或者客户经理续费已经购买的策略更新数据成功");
                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            }
        } catch (Exception e) {
            log.error("客户或者客户经理续费已经购买的策略更新数据异常 e={}", e);
        }
        log.info("客户或者客户经理续费已经购买的策略处理结果 result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @Creater: zhangxia
     * @description：客户或者客户经理购买策略
     * @methodName: insertBuyStrategy
     * @params: [renewStrategyDTOS]
     * @return: com.aq.util.result.Result
     * @createTime: 17:34 2018-2-24
     */
    @Override
    public Result insertBuyStrategy(List<RenewStrategyDTO> renewStrategyDTOS) {
        log.info("客户或者客户经理购买策略购买策略入参参数renewStrategyDTOS={}", JSON.toJSONString(renewStrategyDTOS));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);

        try {
            List<StrategyPurchase> strategyPurchases = new ArrayList<>();
            for (RenewStrategyDTO renewStrategyDTO :
                    renewStrategyDTOS) {
                StrategyPurchase strategyPurchase = new StrategyPurchase();
                strategyPurchase.setCreateTime(new Date());
                strategyPurchase.setExpiryTime(DateUtil.addMonth(new Date(), 1));//到期时间
                strategyPurchase.setPurchaserType(renewStrategyDTO.getPurchaserType());
                strategyPurchase.setStrategyId(renewStrategyDTO.getStrategyId());
                strategyPurchase.setPurchaserId(renewStrategyDTO.getPurchaserId());
                if (RoleCodeEnum.MANAGER.getCode().equals(renewStrategyDTO.getPurchaserType())) {
                    strategyPurchase.setManagerId(0);
                } else if (RoleCodeEnum.CUSTOMER.getCode().equals(renewStrategyDTO.getPurchaserType())) {
                    strategyPurchase.setManagerId(renewStrategyDTO.getRecommendId());
                }
                strategyPurchases.add(strategyPurchase);
            }
            if (strategyPurchaseMapper.insertList(strategyPurchases) > 0) {
                log.info("批量插入购买策略记录成功");
                result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            }

        } catch (Exception e) {
            log.info("客户或者客户经理购买策略处理异常e={}", e);
        }
        log.info("客户或者客户经理购买策略处理结果result={}", JSON.toJSONString(result));

        return result;
    }
}
