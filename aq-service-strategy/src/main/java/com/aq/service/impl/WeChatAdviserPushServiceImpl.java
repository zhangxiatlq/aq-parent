package com.aq.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.constant.ColorEnum;
import com.aq.core.wechat.constant.WechatTemplateEnum;
import com.aq.core.wechat.dto.WechatPushTemplateDTO;
import com.aq.core.wechat.template.WeChatPushTemplateEnum;
import com.aq.core.wechat.util.WeChatPushUtil;
import com.aq.core.wechat.util.WeChatSignatureUtil;
import com.aq.facade.contant.DirectionEnum;
import com.aq.facade.entity.AdviserPush;
import com.aq.facade.entity.AdviserPushUser;
import com.aq.facade.service.IWeChatAdviserPushService;
import com.aq.mapper.AdviserPushMapper;
import com.aq.mapper.AdviserPushUserMapper;
import com.aq.mapper.AdviserRecommendMapper;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.aq.util.transmoney.MoneyTransUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

/**
 * 微信推送-投顾
 *
 * @author 郑朋
 * @create 2018/3/12
 **/
@Slf4j
@Service(version = "1.0.0")
public class WeChatAdviserPushServiceImpl implements IWeChatAdviserPushService {

    @Autowired
    private AdviserPushMapper adviserPushMapper;

    @Autowired
    private AdviserRecommendMapper adviserRecommendMapper;

    @Autowired
    private AdviserPushUserMapper adviserPushUserMapper;

    @Autowired
    private AdviserPythonService adviserPythonService;


    @Override
    public Result pushWeChatTemplate(String pushId, String totalAssets) throws Exception {
        log.info("投顾推送消息 pushId={}，totalAssets={}", pushId, totalAssets);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        if (StringUtils.isBlank(pushId)) {
            result.setMessage("推送信息id不能为空");
            return result;
        }
        if (StringUtils.isBlank(totalAssets)) {
            result.setMessage("推送信息总资产不能为空");
            return result;
        }
        AdviserPush adviserPush = adviserPythonService.getAdviserPush(Integer.valueOf(pushId));
        if (null == adviserPush) {
            result.setMessage("不存在的推送信息");
            return result;
        }
        adviserPythonService.deleteAdviserPush(Integer.valueOf(pushId));
        List<AdviserPushUser> pushUsers = new ArrayList<>();
        String accessToken = WeChatSignatureUtil.getAccessToken();
        List<String> openIds = adviserPushMapper.selectPushOpenId(adviserPush.getAdviserId());
        // 推送自己
        List<String> managerOpenId = adviserRecommendMapper.selectManagerOpenId(adviserPush.getAdviserId());
        if (CollectionUtils.isNotEmpty(managerOpenId)) {
            if (CollectionUtils.isEmpty(openIds)) {
                openIds = new ArrayList<>();
            }
            openIds.addAll(managerOpenId);
        }
        if (CollectionUtils.isEmpty(openIds)) {
            result.setMessage("不存在推送客户");
            return result;
        }
        /**
         * {{first.DATA}}
         操作类型：{{keyword1.DATA}}
         股票代码：{{keyword2.DATA}}
         股票名称：{{keyword3.DATA}}
         操作价格：{{keyword4.DATA}}
         操作仓位：{{keyword5.DATA}}
         {{remark.DATA}}
         */
        WechatPushTemplateDTO wechatPushTemplateDTO = new WechatPushTemplateDTO();
        wechatPushTemplateDTO.setTemplate_id(WechatTemplateEnum.TWO.getTemplateId());
        WechatPushTemplateDTO.Data first = WechatPushTemplateDTO.Data.builder()
                .value("投顾模拟操作");
        WechatPushTemplateDTO.Data remark = WechatPushTemplateDTO.Data.builder()
                .value(WeChatPushTemplateEnum.PROMPT.getMessage());
        String direction = adviserPush.getDirection().equals(DirectionEnum.SELL.getCode())
                ? "卖出" : "买入";
        String color = adviserPush.getDirection().equals(DirectionEnum.SELL.getCode())
                ? ColorEnum.GREEN.getHex() : ColorEnum.RED.getHex();
        WechatPushTemplateDTO.Data keyword1 = WechatPushTemplateDTO.Data.builder().
                value(direction).color(color);
        WechatPushTemplateDTO.Data keyword2 = WechatPushTemplateDTO.Data.builder().
                value(adviserPush.getSharesCode());
        WechatPushTemplateDTO.Data keyword3 = WechatPushTemplateDTO.Data.builder().
                value(adviserPush.getSharesName());
        WechatPushTemplateDTO.Data keyword4 = WechatPushTemplateDTO.Data.builder().
                value(MoneyTransUtil.formatByTwo(adviserPush.getTradePrice()).toString());
        BigDecimal ratio = adviserPush.getTradeTotalPrice().divide(new BigDecimal(totalAssets), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100)).setScale(2);
        StringBuilder stringBuilder = new StringBuilder(ratio.toString()).append("%");
        WechatPushTemplateDTO.Data keyword5 = WechatPushTemplateDTO.Data.builder().
                value(stringBuilder.toString());
        Map<String, WechatPushTemplateDTO.Data> dataMap = new HashMap<>();
        dataMap.put("first", first);
        dataMap.put("remark", remark);
        dataMap.put("keyword1", keyword1);
        dataMap.put("keyword2", keyword2);
        dataMap.put("keyword3", keyword3);
        dataMap.put("keyword4", keyword4);
        dataMap.put("keyword5", keyword5);
        wechatPushTemplateDTO.setData(dataMap);
        for (String openId : openIds) {
            wechatPushTemplateDTO.setTouser(openId);
            if (WeChatPushUtil.pushWechat(accessToken, wechatPushTemplateDTO)) {
                AdviserPushUser adviserPushUser = new AdviserPushUser();
                adviserPushUser.setPushTime(new Date());
                adviserPushUser.setAdviserPushId(adviserPush.getId());
                adviserPushUser.setOpenId(openId);
                pushUsers.add(adviserPushUser);

            } else {
                log.error("推送失败,推送dto为:{}", JSON.toJSONString(wechatPushTemplateDTO));
            }
        }
        if (CollectionUtils.isNotEmpty(pushUsers)) {
            adviserPushUserMapper.insertList(pushUsers);
        }
        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }

}
