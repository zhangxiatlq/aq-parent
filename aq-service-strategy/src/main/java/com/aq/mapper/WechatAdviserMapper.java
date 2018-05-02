package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.Adviser;
import com.aq.facade.vo.AdviserWechatDetailVO;
import com.aq.facade.vo.WechatAdviserQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：微信版 投顾 mapper
 * @author： 张霞
 * @createTime： 2018/03/12
 * @Copyright @2017 by zhangxia
 */
public interface WechatAdviserMapper extends BaseMapper<Adviser> {

    /**
     * @author: zhangxia
     * @desc:微信 我的投顾和投顾列表接口
     * @params: [openId]
     * @methodName:listWechatAdviser
     * @date: 2018/3/12 0012 下午 16:14
     * @return: java.util.List<com.aq.facade.vo.WechatAdviserQueryVO>
     * @version:2.1.2
     */
    List<WechatAdviserQueryVO> listWechatAdviser(@Param(value = "openId") String openId);

    /**
     * @author: zhangxia
     * @desc: 微信端 获取投顾详情
     * @params: [adviserId]
     * @methodName:getAdviserWechatDetailVO
     * @date: 2018/3/28 0028 上午 10:41
     * @return: com.aq.facade.vo.AdviserWechatDetailVO
     * @version:2.1.2
     */
    AdviserWechatDetailVO getAdviserWechatDetailVO(Integer adviserId);
}
