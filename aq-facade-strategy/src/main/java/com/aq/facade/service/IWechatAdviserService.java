package com.aq.facade.service;

import com.aq.facade.dto.AdviserDetailDTO;
import com.aq.facade.dto.WechatAdviserQueryDTO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：投顾微信版 接口
 * @author： 张霞
 * @createTime： 2018/03/12
 * @Copyright @2017 by zhangxia
 */
public interface IWechatAdviserService {

    /**
     * @author: zhangxia
     * @desc: 微信 我的投顾和投顾列表接口
     * @params: [openId--微信openid, pageBean]
     * @methodName:listWechatAdviser
     * @date: 2018/3/12 0012 下午 16:08
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result listWechatAdviser(WechatAdviserQueryDTO dto, PageBean pageBean);

    /**
     * @author: zhangxia
     * @desc: 微信端 获取投顾详情
     * @params: [detailDTO]
     * @methodName:getWechantAdviserDetail
     * @date: 2018/3/28 0028 上午 10:34
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result getWechantAdviserDetail(AdviserDetailDTO detailDTO);
}
