package com.aq.facade.service;

import com.aq.util.result.Result;

/**
 * 微信推送-投顾
 *
 * @author 郑朋
 * @create 2018/3/12
 **/
public interface IWeChatAdviserPushService {
    /**
     * 投顾微信推送
     *
     * @param pushId 推送记录id
     * @param totalAssets 总资产
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/12
     * @throws Exception
     */
    Result pushWeChatTemplate(String pushId,String totalAssets) throws Exception;
}
