package com.aq.controller.wechat.back;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aq.core.constant.OrderStateEnum;
import com.aq.core.constant.PayTypeEnum;
import com.aq.core.wechat.WeChatConfig;
import com.aq.core.wechat.pay.WeChatPay;
import com.aq.core.wechat.response.WeChatPayResponse;
import com.aq.core.wechat.response.WebAuthorizeResponse;
import com.aq.core.wechat.util.WXPayUtil;
import com.aq.core.wechat.util.WeChatCodeUtil;
import com.aq.facade.dto.order.AsyncOrderDTO;
import com.aq.facade.dto.order.UpdateVipOrderDTO;
import com.aq.facade.service.IWechatStrategyService;
import com.aq.facade.service.order.IOrderService;
import com.aq.facade.vo.order.OrderCacheInfoVO;
import com.aq.service.IOrderCacheService;
import com.aq.util.http.HttpClientUtils;
import com.aq.util.order.IdGenerator;
import com.aq.util.order.OrderBizCode;
import com.aq.util.result.Result;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: WeChatBackController
 * @Description: 回调处理
 * @author: lijie
 * @date: 2018年2月23日 上午10:24:36
 */
@Slf4j
@Controller
@RequestMapping("wechat")
public class WeChatBackController {

    @Autowired
    private WeChatPay weChatPay;

    @Reference(version = "1.0.0", check = false)
    private IOrderService orderService;

    @Reference(version = "1.0.0", check = false)
    private IWechatStrategyService wechatStrategyService;

    @Autowired
    private IOrderCacheService orderCacheService;

    /**
     * @param @param request
     * @param @param response    参数
     * @return void    返回类型
     * @throws IOException
     * @throws
     * @Title: authorizecode
     * @Description: 微信网页授权回调
     */
    @RequestMapping(value = "/authorize/code", method = {RequestMethod.GET, RequestMethod.POST})
    public void authorizecode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        log.info("微信授权回调参数code={},state={}", code, state);
        final Map<String, Object> params = new HashMap<>();
        params.put("appid", WeChatConfig.APPID);
        params.put("secret", WeChatConfig.SECRET);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        try {
            String result = HttpClientUtils.sendGet(WeChatConfig.AUTHORIZE_ACCESS_TOKEN_URL, params)
                    .getResponseContent();
            log.info("微信授权回调请求access_token 返回数据={}", result);
            if (StringUtils.isNotBlank(result)) {
                WebAuthorizeResponse rs = JSON.parseObject(result, WebAuthorizeResponse.class);
                if (rs.isSuccess()) {
                    // 获取成功处理业务
                    String rstate = WeChatCodeUtil.decode(state);
                    log.info("微信网页授权回调解析后数据={}", rstate);
                    if (StringUtils.isNotBlank(rstate)) {
                        JSONObject json = JSON.parseObject(rstate);
                        String route = json.getString("route");
                        // 如果是url 则重定向到微信页面（返回对应的openId）
                        String redirectUrl = route + "?openId=" + rs.getOpenid();
                        log.info("微信授权重定向地址={}", redirectUrl);
                        response.sendRedirect(redirectUrl);
                    }
                }
            }
        } catch (Exception e) {
            log.error("网页授权回调异常", e);
        }
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     * @Title: notify
     * @author: lijie
     * @Description: 微信异步回调
     * @return: void
     */
    @PostMapping("pay/async")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("微信异步回调start");
        boolean result = false;
        try {
            // 得到回调xml数据
            String backXml = WXPayUtil.parseResponseXml(request, response);
            log.info("异步回调xml数据={}", backXml);
            // 验签并得到数据
            WeChatPayResponse pr = WXPayUtil.getWeChatPayResponse(weChatPay.processResponseXml(backXml));
            log.info("异步回调响应数据={}", JSON.toJSONString(pr));
            if (pr.isSuccess()) {
                // 获取订单号处理业务
                OrderCacheInfoVO info = orderCacheService.getCacheOrder(pr.getOutTradeNo());
                if (null == info) {
                    log.warn("根据订单号:" + pr.getOutTradeNo() + ",未获取到订单相关信息");
                } else {
                    BigDecimal money = new BigDecimal(info.getPrice()).multiply(new BigDecimal("100"));
                    if (money.compareTo(new BigDecimal(pr.getTotalFee().toString())) == 0) {
                        result = handlerOrder(pr.getOutTradeNo(), pr.getTransactionId(), money);
                        // 成功后清楚缓存数据
                        if (result) {
                            orderCacheService.evictCacheOrder(pr.getOutTradeNo());
                        }
                    } else {
                        log.warn("回调金额与订单金额不匹配");
                    }
                }
            }
        } catch (Exception e) {
            log.error("微信异步回调异常e={}", e);
        } finally {
            if (result) {
                sendResponse(response, WXPayUtil.setXML("SUCCESS", "OK"));
            } else {
                sendResponse(response, WXPayUtil.setXML("FAIL", "error"));
            }
        }
    }

    /**
     * @return void
     * @throws
     * @Title: sendResponse
     * @Description: 发送响应数据
     * @param: @param response
     * @param: @param content
     * @param: @throws IOException
     * @author lijie
     */
    private void sendResponse(HttpServletResponse response, final String content) throws IOException {
        PrintWriter pw = response.getWriter();
        pw.write(content);
        pw.flush();
        pw.close();
    }

    /**
     * @param orderNo
     * @param thirdOrderNo
     * @param money
     * @return boolean    返回类型
     * @throws
     * @Title: handlerOrderService
     * @Description: 订单业务处理
     */
    private boolean handlerOrder(String orderNo, String thirdOrderNo, BigDecimal money) {
        log.info("第三方回调后根据订单号进行业务处理入参数orderNo={}", orderNo);
        boolean result = false;
        try {
            String orderPrefix = IdGenerator.getBizeCode(orderNo);
            // 策略购买业务处理
            if (OrderBizCode.CUSTOMR_RENEW_MD.equals(orderPrefix)
                    || OrderBizCode.MANAGER_RENEW_MD.equals(orderPrefix)
                    || OrderBizCode.MANAGER_BUY_MD.equals(orderPrefix)
                    || OrderBizCode.CUSTOMR_BUY_MD.equals(orderPrefix)) {
                // 客户或者客户经理续费策略、更新订单号状态
                AsyncOrderDTO asyncOrderDTO = new AsyncOrderDTO();
                asyncOrderDTO.setMainOrderNo(orderNo);
                asyncOrderDTO.setOrderState(OrderStateEnum.SUCCESS.getCode());
                asyncOrderDTO.setPayType(PayTypeEnum.PAY_TYPE_WECHATPAY.getPayType());
                asyncOrderDTO.setPrice(money);
                asyncOrderDTO.setThirdOrderNo(thirdOrderNo);
                log.info("订单异步回调接口入参, asyncOrderDTO={}", JSON.toJSONString(asyncOrderDTO));
                Result oresult = orderService.updateAsyncOrder(asyncOrderDTO);
                log.info("订单异步回调接口返回值，result={}", JSON.toJSONString(oresult));
                result = oresult.isSuccess();
            } else if (OrderBizCode.CUSTOMR_VIP.equals(orderPrefix)) {
                // vip业务处理
                UpdateVipOrderDTO dto = new UpdateVipOrderDTO();
                dto.setOrderNo(orderNo);
                dto.setPayType(PayTypeEnum.PAY_TYPE_WECHATPAY.getPayType());
                dto.setStatus(OrderStateEnum.SUCCESS.getCode());
                dto.setThirdOrderNo(thirdOrderNo);
                dto.setUpdaterId(0);
                Result oresult = orderService.updateVipOrder(dto);
                log.info("修改vip订单 返回数据={}", JSON.toJSONString(oresult));
                result = oresult.isSuccess();
            } else if (OrderBizCode.CUSTOMER_ADVISER_MD.equals(orderPrefix)
                    || OrderBizCode.MANAGER_ADVISER_MD.equals(orderPrefix)) {
                // 客户或者客户经理购买或者续费投顾、更新订单号状态
                AsyncOrderDTO asyncOrderDTO = new AsyncOrderDTO();
                asyncOrderDTO.setMainOrderNo(orderNo);
                asyncOrderDTO.setOrderState(OrderStateEnum.SUCCESS.getCode());
                asyncOrderDTO.setPayType(PayTypeEnum.PAY_TYPE_WECHATPAY.getPayType());
                asyncOrderDTO.setPrice(money);
                asyncOrderDTO.setThirdOrderNo(thirdOrderNo);
                log.info("(投顾)订单异步回调接口入参, asyncOrderDTO={}", JSON.toJSONString(asyncOrderDTO));
                Result oresult = orderService.updateAsyncAdviserOrder(asyncOrderDTO);
                log.info("(投顾)订单异步回调接口返回值，result={}", JSON.toJSONString(oresult));
                result = oresult.isSuccess();


            } else {
                log.warn("回调处理订单编号不匹配");
            }
        } catch (Exception e) {
            log.info("第三方回调后根据订单号进行业务处理异常", e);
        }
        return result;
    }
}
