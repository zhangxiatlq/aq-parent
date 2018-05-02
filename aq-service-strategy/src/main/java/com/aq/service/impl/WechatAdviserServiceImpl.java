package com.aq.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.facade.dto.AdviserDetailDTO;
import com.aq.facade.dto.WechatAdviserQueryDTO;
import com.aq.facade.entity.AdviserPurchase;
import com.aq.facade.service.IWechatAdviserService;
import com.aq.facade.vo.AdviserWechatDetailVO;
import com.aq.facade.vo.WechatAdviserQueryVO;
import com.aq.mapper.AdviserPurchaseMapper;
import com.aq.mapper.WechatAdviserMapper;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：
 * @author： 张霞
 * @createTime： 2018/03/12
 * @Copyright @2017 by zhangxia
 */
@Slf4j
@Service(version = "1.0.0")
public class WechatAdviserServiceImpl implements IWechatAdviserService {


    @Autowired
    private WechatAdviserMapper wechatAdviserMapper;

    @Autowired
    private AdviserPurchaseMapper adviserPurchaseMapper;
    /**
     * @author: zhangxia
     * @desc: 微信 我的投顾和投顾列表接口
     * @params: [openId, pageBean]
     * @methodName:listWechatAdviser
     * @date: 2018/3/12 0012 下午 16:10
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result listWechatAdviser(WechatAdviserQueryDTO dto, PageBean pageBean) {
        if (dto.getOpenId() == null) {
            return ResultUtil.setResult(false, "openId不能为空");
        }
        log.info("微信 我的投顾 和 投顾列表 openId:{},page:{}", dto.getOpenId(), JSON.toJSONString(pageBean));
        PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
        List<WechatAdviserQueryVO> list = wechatAdviserMapper.listWechatAdviser(dto.getOpenId());
        log.info("微信 我的投顾 和 投顾列表 查询结果={}",JSON.toJSONString(list));
        PageInfo<WechatAdviserQueryVO> pageList = new PageInfo<>(list);
        log.info("微信 我的投顾 和 投顾列表 返回结果为 pageList:{}", JSON.toJSONString(pageList));
        return ResultUtil.getResult(RespCode.Code.SUCCESS, pageList.getList(), pageList.getTotal());
    }

    /**
     * @author: zhangxia
     * @desc: 微信端 获取投顾详情
     * @params: [adviserId]
     * @methodName:getWechantAdviserDetail
     * @date: 2018/3/28 0028 上午 10:38
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @Override
    public Result getWechantAdviserDetail(AdviserDetailDTO dto) {
        log.info("微信端 获取投顾详情 入参参数dtod={}",JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            AdviserPurchase  adviserPurchase = new AdviserPurchase();
            adviserPurchase.setAdviserId(dto.getAdviserId());
            adviserPurchase.setPurchaserId(dto.getPurchaserId());
            adviserPurchase.setPurchaserType(dto.getPurchaserType());
            List<AdviserPurchase> list =  adviserPurchaseMapper.select(adviserPurchase);
            if (!(list.size()>0)){
                log.info("微信端 获取投顾详情时，此投顾没有购买，无法查看详情");
                result.setMessage("请先购买投顾后查看");
                return result;
            }
            if (new Date().getTime()>list.get(0).getExpiryTime().getTime()){
                log.info("微信端 获取投顾详情时，此投顾购买后已经到期，无法查看详情");
                result.setMessage("请先续费投顾后查看");
                return result;
            }
            AdviserWechatDetailVO detailVO = wechatAdviserMapper.getAdviserWechatDetailVO(dto.getAdviserId());
            if (Objects.nonNull(detailVO)){
                result = ResultUtil.getResult(RespCode.Code.SUCCESS,detailVO);
            }else {
                result =ResultUtil.getResult(RespCode.Code.NOT_QUERY_DATA);
            }
        } catch (Exception e) {
            log.info("微信端 获取投顾详情 处理结果 异常e={}",e);
        }
        log.info("微信端 获取投顾详情 处理结果result={}",JSON.toJSONString(result));
        return result;
    }
}
