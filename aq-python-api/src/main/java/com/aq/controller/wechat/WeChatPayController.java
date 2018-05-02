package com.aq.controller.wechat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.controller.base.BaseController;
import com.aq.core.annotation.RepeatToken;
import com.aq.core.wechat.pay.WeChatPay;
import com.aq.core.wechat.response.WeChatPayResponse;
import com.aq.core.wechat.util.WXPayUtil;
import com.aq.facade.service.IWechatStrategyService;
import com.aq.facade.service.order.IOrderService;
import com.aq.facade.vo.customer.CustomerInfoVO;
import com.aq.facade.vo.order.OrderCacheInfoVO;
import com.aq.service.IOrderCacheService;
import com.aq.util.http.IPUtil;
import com.aq.util.http.RequestUtil;
import com.aq.util.qrcode.QRCodeUtil;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: WeChatPayController
 * @Description: 微信支付
 * @author: lijie
 * @date: 2018年1月29日 上午11:03:34
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/wechat/pay")
@Api(value = "微信支付相关接口",description="微信支付相关接口")
public class WeChatPayController extends BaseController {
	
	@Autowired
	private WeChatPay weChatPay;
	
	@Reference(version = "1.0.0", check = false)
	private IOrderService orderService;
	
	@Autowired
	private IOrderCacheService orderCacheService;
	
	@Reference(version = "1.0.0",check = false)
	private IWechatStrategyService wechatStrategyService;
	
	/**
	 * 
	 * @Title: orderSign
	 * @author: lijie 
	 * @Description: 获取微信支付签名
	 * @param
	 * @return
	 * @return: Result
	 */
	@ApiOperation(value = "获取微信支付签名", notes = "[李杰]获取微信支付签名")
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "orderNo", value = "订单编号", paramType = "path" , required = true),
		@ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header" , required = true)
	})
    @ApiResponses(value = {
			@ApiResponse(code = 200, message = "操作成功")
	})
	@RepeatToken(key = "orderNo")
	@GetMapping("sign/{orderNo}")
	public Result orderSign(HttpServletRequest request, @PathVariable String orderNo) {
		log.info("根据订单ID获取微信支付签名入参={}", orderNo);
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		CustomerInfoVO vo = getCustomerInfo(RequestUtil.getOpenId(request));
		if (null == vo) {
			result.setMessage("未绑定微信");
			return result;
		}
		OrderCacheInfoVO info = orderCacheService.getCacheOrder(orderNo);
		if (null == info) {
			result.setMessage("订单不存在");
			return result;
		}
		BigDecimal totalMoney = new BigDecimal(info.getPrice());
		if (totalMoney.compareTo(BigDecimal.ZERO) <= 0) {
			result.setMessage("支付金额必须大于0");
			return result;
		}
		try {
			Map<String, String> signMap = weChatPay.unifiedOrder(
					queryOrderPayInfo(totalMoney, orderNo, IPUtil.getIpAddress(request), vo.getOpenId(), "JSAPI"));
			log.info("调用微信下单返回数据={}", JSON.toJSONString(signMap));
			WeChatPayResponse payResponse = WXPayUtil.getWeChatPayResponse(signMap);
			if (payResponse.isSuccess()) {
				result.setData(payResponse.getJsApiResult(weChatPay.getSignType()));
				ResultUtil.setResult(result, RespCode.Code.SUCCESS);
			}
		} catch (Exception e) {
			log.info("调用微信下单异常", e);
		}
		return result;
	}
	/**
	 * 
	* @Title: orderCodeUrl  
	* @Description: 获取微信支付二维码支付url 
	* @param @param request
	* @param @param orderNo
	* @param @param openId
	* @param @return    参数  
	* @return Result    返回类型  
	* @throws
	 */
	@ApiOperation(value = "获取微信支付二维码支付url", notes = "[李杰]获取微信支付二维码支付url")
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "orderNo", value = "订单编号", paramType = "path" , required = true)
	})
    @ApiResponses(value = {
			@ApiResponse(code = 200, message = "操作成功")
	})
	@RepeatToken(key = "orderNo")
	@GetMapping("codeurl/{orderNo}")
	public Result orderCodeUrl(HttpServletRequest request, @PathVariable String orderNo) {
		log.info("获取微信支付二维码url入参={}", orderNo);
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		OrderCacheInfoVO info = orderCacheService.getCacheOrder(orderNo);
		if (null == info) {
			result.setMessage("订单不存在");
			return result;
		}
		BigDecimal totalMoney = new BigDecimal(info.getPrice());
		if (totalMoney.compareTo(BigDecimal.ZERO) <= 0) {
			result.setMessage("支付金额必须大于0");
			return result;
		}
		try {
			Map<String, String> signMap = weChatPay
					.unifiedOrder(queryOrderPayInfo(totalMoney, orderNo, IPUtil.getIpAddress(request), null, "NATIVE"));
			log.info("调用微信下单返回二维码数据={}", JSON.toJSONString(signMap));
			WeChatPayResponse payResponse = WXPayUtil.getWeChatPayResponse(signMap);
			if (payResponse.isSuccess()) {
				result.setData(payResponse.getCodeUrl());
				ResultUtil.setResult(result, RespCode.Code.SUCCESS);
			}
		} catch (Exception e) {
			log.info("调用微信下单获取微信支付二维码异常", e);
		}
		return result;
	}
	/**
	 * 
	* @Title: gen  
	* @Description: 根据微信支付url生成二维码
	* @param @param codeUrl
	* @param @param request
	* @param @param response    参数  
	* @return void    返回类型  
	* @throws
	 */
	@ApiOperation(value = "根据微信支付url生成二维码", notes = "[李杰]根据微信支付url生成二维码")
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "codeUrl", value = "订单编号", paramType = "query" , required = true),
	})
    @ApiResponses(value = {
			@ApiResponse(code = 200, message = "操作成功")
	})
	@GetMapping("genqrcode")
	public void gen(@RequestParam String codeUrl, HttpServletRequest request, HttpServletResponse response) {
		log.info("根据微信支付url生成二维码入参={}", codeUrl);
		QRCodeUtil.zxingCodeCreate(request, response, codeUrl, 300, 300);
	}
	/**
	 * 
	* @Title: queryOrderPayInfo  
	* @Description: 得到支付信息
	* @param totalMoney 金额
	* @param orderNo 订单号 
	* @param ip 请求IP
	* @param openId openId
	* @param trade_type 请求类型
	* @return
	* @throws Exception    参数  
	* @return Map<String,String>    返回类型  
	* @throws
	 */
	private Map<String, String> queryOrderPayInfo(final BigDecimal totalMoney, final String orderNo, final String ip,
			final String openId, final String trade_type) throws Exception {
		Map<String, String> result = new HashMap<>();
		result.put("body", "购买商品");
		result.put("out_trade_no", orderNo);
		if (StringUtils.isNotBlank(openId)) {
			result.put("openid", openId);
		}
		// 订单总金额(单位为分，不能带小数点)
		int money = totalMoney.multiply(new BigDecimal("100")).intValue();
		result.put("total_fee", String.valueOf(money));
		result.put("spbill_create_ip", ip);
		result.put("trade_type", trade_type);
		return result;
	}
}
