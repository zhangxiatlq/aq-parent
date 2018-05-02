package com.aq.facade.service;

import com.aq.facade.dto.AddPayTradeDTO;
import com.aq.facade.dto.RenewStrategyDTO;
import com.aq.facade.dto.StrategyDetailDTO;
import com.aq.facade.dto.WechatStrategyQueryDTO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

import java.util.List;

/**
 * 微信策略接口
 *
 * @author 熊克文
 * @createDate 2018\2\12
 **/
public interface IWechatStrategyService {

    /**
     * 微信推送接口
     *
     * @param pushIds 推送信息的ids
     * @param template_id 模板id
     * @return Result
     * @throws Exception 异常
     */
    Result pushWechantTemplete(String pushIds, String templateId) throws Exception;

    /**
     * 微信 我的策略
     *
     * @param pageBean 分页参数
     * @param dto
     * @return {@link com.aq.facade.vo.WechatStrategyQueryVO}
     * @author 熊克文
     */
    Result listWechatStrategyQueryVO(WechatStrategyQueryDTO dto, PageBean pageBean);

    /**
     * 微信 策略详情
     *
     * @param dto 策略详情dto
     * @return {@link com.aq.facade.vo.WechatStrategyQueryVO}
     * @author 熊克文
     */
    Result getWechantStrategyDetail(StrategyDetailDTO dto);

    /**
     * @Creater: zhangxia
     * @description：客户或者客户经理(微信端或者pc端)对策略进行续费或者购买
     * @methodName: renewStrategyByCustomer
     * @params: [renewStrategyDTOS]
     * @return: com.aq.util.result.Result
     * @createTime: 9:24 2018-2-13
     */
    Result renewStrategyByCustomer(List<RenewStrategyDTO> renewStrategyDTOS);

    /**
     * @Creater: zhangxia
     * @description：第三方支付回调后进行支付记录
     * @methodName: addStrategyPayTrade
     * @params: [addPayTradeDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 12:21 2018-2-13
     */
    Result addStrategyPayTrade(AddPayTradeDTO addPayTradeDTO);

    /**
     * @Creater: zhangxia
     * @description：客户或者客户经理续费已经购买的策略
     * @methodName: updateRenewStrategy
     * @params: [renewStrategyDTOS]
     * @return: com.aq.util.result.Result
     * @createTime: 10:07 2018-2-22
     */
    Result updateRenewStrategy(List<RenewStrategyDTO> renewStrategyDTOS);
    /**
     * @Creater: zhangxia
     * @description：客户或者客户经理购买策略
     * @methodName: insertBuyStrategy
     * @params: [renewStrategyDTOS]
     * @return: com.aq.util.result.Result
     * @createTime: 17:33 2018-2-24
     */
    Result insertBuyStrategy(List<RenewStrategyDTO> renewStrategyDTOS);
}
