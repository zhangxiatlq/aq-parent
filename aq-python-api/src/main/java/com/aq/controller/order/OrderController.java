package com.aq.controller.order;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.controller.base.BaseController;
import com.aq.facade.dto.customer.VipPurchaseDTO;
import com.aq.facade.exception.customer.CustomerExceptionEnum;
import com.aq.facade.service.order.IOrderService;
import com.aq.facade.vo.customer.CustomerInfoVO;
import com.aq.facade.vo.order.OrderCacheInfoVO;
import com.aq.service.IOrderCacheService;
import com.aq.util.http.RequestUtil;
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
 * @ClassName: OrderController
 * @Description: 订单相关接口
 * @author: lijie
 * @date: 2018年2月23日 下午2:44:31
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/orders")
@Api(value = "订单相关接口", description = "订单相关接口")
public class OrderController extends BaseController {

	@Reference(version = "1.0.0", check = false)
	private IOrderService orderService;
	
	@Autowired
	private IOrderCacheService orderCacheService;
	
	/**
	 * 
	* @Title: vipPurchaseOrder  
	* @Description:vip购买下单
	* @param @param dto
	* @param @return    参数  
	* @return Result    返回类型  
	* @throws
	 */
	@ApiOperation(value = "vip购买下单",notes = "[李杰]vip购买下单")
    @ApiResponses(value = {
            @ApiResponse(code = 10100,message = "请求参数有误"),
            @ApiResponse(code = 200,message = "操作成功")
    })
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header" , required = true)
	})
	@PostMapping("vip")
	public Result vipPurchaseOrder(VipPurchaseDTO dto, HttpServletRequest request) {
		log.info("vip购买下单入参={}", JSON.toJSONString(dto));
		CustomerInfoVO vo = getCustomerInfo(RequestUtil.getOpenId(request));
		if (null == vo) {
			return ResultUtil.getResult(CustomerExceptionEnum.NOT_EXISTS);
		}
		dto.setCustomerId(vo.getId());
		Result result = orderService.addVipOrder(dto);
		log.info("购买vip下单返回数据={}", JSON.toJSONString(result));
		if (result.isSuccess()) {
			OrderCacheInfoVO info = result.getData(OrderCacheInfoVO.class);
			orderCacheService.putCacheOrder(info);
			result.setData(info.getOrderNo());
		}
		return result;
	}
	
}
