package com.aq.schedule;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.core.wechat.WeChatConfig;
import com.aq.core.wechat.util.WeChatSignatureUtil;
import com.aq.facade.dto.AddAttentionRecordDTO;
import com.aq.facade.service.customer.ICustomerService;
import com.aq.util.http.HttpClientUtils;
import com.aq.util.http.HttpResult;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：定时（每天0点）请求微信接口获取微信关注的openid
 * @author： 张霞
 * @createTime： 2018/03/05
 * @Copyright @2017 by zhangxia
 */
@JobHandler(value = "customerAttentionScheduled")
@Component
@Slf4j
public class CustomerAttentionWechatRecordScheduled extends IJobHandler {


    @Reference(version = "1.0.0", check = false)
    private ICustomerService customerService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {

        String accessToken = WeChatSignatureUtil.getAccessToken();

        Map<String,Object> params = new HashMap<>();
        params.put("access_token",accessToken);
        params.put("next_openid","");
        HttpResult httpResult = null;
        try {
            httpResult = HttpClientUtils.sendGet(WeChatConfig.GET_USER_OPENIDS,params,"utf-8");
            String result = httpResult.getResponseContent();
            log.info("获取用户微信关注列表请求回来结果result={}",result);
            AddAttentionRecordDTO addAttentionRecordDTO = JSON.parseObject(result, AddAttentionRecordDTO.class);
            customerService.recordAttention(addAttentionRecordDTO);
            if (addAttentionRecordDTO.getTotal().intValue()>addAttentionRecordDTO.getCount()&&addAttentionRecordDTO.getCount().intValue()==10000){
                for (int i = 1;i <= addAttentionRecordDTO.getTotal()/addAttentionRecordDTO.getCount()+1;i++){
                    params.put("next_openid",addAttentionRecordDTO.getData().getOpenid().get(addAttentionRecordDTO.getData().getOpenid().size()-1));
                    httpResult = HttpClientUtils.sendGet(WeChatConfig.GET_USER_OPENIDS,params,"utf-8");
                    result = httpResult.getResponseContent();
                    log.info("获取用户列表请求回来结果result={}",result);
                    addAttentionRecordDTO = JSON.parseObject(result, AddAttentionRecordDTO.class);
                    customerService.recordAttention(addAttentionRecordDTO);
                }
            }
        } catch (Exception e) {
            log.info("获取用户微信关注列表异常e={}",e);
        }

        return SUCCESS;
    }


}
