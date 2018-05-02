package com.aq.controller.wechat.adviser;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aq.controller.base.BaseController;
import com.aq.controller.wechat.dto.AdviserDTO;
import com.aq.controller.wechat.dto.RenewAdviserRequestDTO;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.rediscache.ICacheService;
import com.aq.facade.dto.*;
import com.aq.facade.entity.AdviserRecommend;
import com.aq.facade.service.IAdviserRecommendService;
import com.aq.facade.service.IAdviserService;
import com.aq.facade.service.IWechatAdviserService;
import com.aq.facade.vo.AdviserOrderVO;
import com.aq.facade.vo.AdviserWechatDetailVO;
import com.aq.facade.vo.WechatAdviserQueryVO;
import com.aq.facade.vo.customer.CustomerInfoVO;
import com.aq.facade.vo.order.OrderCacheInfoVO;
import com.aq.service.IOrderCacheService;
import com.aq.util.http.RequestUtil;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 微信 投顾
 *
 * @author 郑朋
 * @create 2018/3/7
 **/
@Slf4j
@RestController
@RequestMapping(value = "api/wechat/advisers")
@CrossOrigin(origins = "*")
@Api(value = "微信投顾接口", description = "微信投顾接口")
public class WeChatAdviserController extends BaseController {

    @Reference(version = "1.0.0")
    private IAdviserService adviserService;

    @Reference(version = "1.0.0")
    private IAdviserRecommendService adviserRecommendService;

    @Reference(version = "1.0.0")
    private IWechatAdviserService wechatAdviserService;

    @Autowired
    private ICacheService redisCache;

    @Autowired
    private IOrderCacheService orderCacheService;


    @ApiOperation(value = "微信 客户购买或者续费投顾", notes = "[郑朋]微信 客户购买或者续费投顾")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "list", value = "客户经理续费或者购买投顾", paramType = "query", required = true)
    })
    //@RequestMapping(value = "adviser/renewOrBuy", method = RequestMethod.POST)
    @Deprecated
    public Result renewAdviser(String list, HttpServletRequest request) {
        //list格式：{"purchaseType":"","adviserDTOS":[{"id":"","purchaseMonths":"","purchaseId":"","recommendId":""},{"id":"","purchaseMonths":"","purchaseId":"","recommendId":""}]}
        log.info("客户购买或者续费投顾参数， renewStrategyDTO={}", list);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            CustomerInfoVO customerInfoVO = getCustomerInfo(RequestUtil.getOpenId(request));
            RenewAdviserRequestDTO renewAdviserRequestDTO = JSON.parseObject(list, RenewAdviserRequestDTO.class);
            renewAdviserRequestDTO.setPurchaserId(customerInfoVO.getId());
            renewAdviserRequestDTO.setPurchaserType(RoleCodeEnum.CUSTOMER.getCode());
            List<AdviserDTO> adviserDTOS = renewAdviserRequestDTO.getAdviserDTOS();
            List<RenewAdviserDTO> renewAdviserDTOS = new ArrayList<>();
            RenewAdviserDTO renewAdviserDTO;
            for (AdviserDTO adviserDTO : adviserDTOS) {
                //获取投顾推荐记录信息
                AdviserQueryRecommendDTO adviserQueryRecommendDTO = new AdviserQueryRecommendDTO();
                adviserQueryRecommendDTO.setAdviserId(adviserDTO.getId());
                adviserQueryRecommendDTO.setRecommendedId(adviserDTO.getRecommendId());
                adviserQueryRecommendDTO.setBeRecommendedId(renewAdviserRequestDTO.getPurchaserId());
                adviserQueryRecommendDTO.setBeRecommendedRoleType(renewAdviserRequestDTO.getPurchaserType());
                log.info("获取投顾推荐记录数据入参，adviserQueryRecommendDTO={}", JSON.toJSONString(adviserQueryRecommendDTO));
                result = adviserRecommendService.getRecommendAdviser(adviserQueryRecommendDTO);
                log.info("获取投顾推荐记录数据返回值，result={}", JSON.toJSONString(result));
                if (!result.isSuccess() || null == result.getData()) {
                    result.setMessage("购买失败");
                    return result;
                }
                AdviserRecommend adviserRecommend = result.getData(AdviserRecommend.class);
                renewAdviserDTO = new RenewAdviserDTO();
                renewAdviserDTO.setId(adviserDTO.getPurchaseId());
                renewAdviserDTO.setManagerId(adviserRecommend.getRecommendedId());
                renewAdviserDTO.setRecommendId(adviserRecommend.getId());
                renewAdviserDTO.setPurchaserId(renewAdviserRequestDTO.getPurchaserId());
                renewAdviserDTO.setPurchaserType(renewAdviserRequestDTO.getPurchaserType());
                renewAdviserDTO.setPurchasePrice(adviserRecommend.getPushPrice());
                renewAdviserDTO.setPurchaseMoney(adviserRecommend.getPushPrice().multiply(new BigDecimal(adviserDTO.getPurchaseMonths())));
                renewAdviserDTO.setAdviserId(adviserDTO.getId());
                renewAdviserDTO.setPurchaseType(renewAdviserRequestDTO.getPurchaseType());
                renewAdviserDTOS.add(renewAdviserDTO);
            }
            result = adviserService.addAdviserOrder(renewAdviserDTOS);
            log.info("投顾订单返回值 result={}", JSON.toJSONString(result));
            if (result.isSuccess()) {
                //存入redis缓存
                AdviserOrderVO adviserOrderVO = result.getData(AdviserOrderVO.class);

                redisCache.set(adviserOrderVO.getMainOrderNo(), adviserOrderVO.getTotalMoney(), 8L, TimeUnit.HOURS);
                //redisCache.set(adviserOrderVO.getMainOrderNo() + OrderBizCode.INFO, adviserOrderVO.getAdviserOrders(), 8L, TimeUnit.HOURS);
                result.setData(adviserOrderVO.getMainOrderNo());
            }

        } catch (Exception e) {
            log.error("客户购买或者续费投顾异常, e={}", e);
        }
        log.info("客户购买或者续费投顾, result={}", JSON.toJSONString(result));
        return result;
    }


    @ApiOperation(value = "微信 客户购买或者续费投顾", notes = "[郑朋]微信 客户购买或者续费投顾")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "list", value = "客户经理续费或者购买投顾", paramType = "query", required = true)
    })
    @RequestMapping(value = "adviser/renewOrBuy", method = RequestMethod.POST)
    public Result addOrder(String list, HttpServletRequest request) {
        ////{adviserId:投顾id,recommendId:推荐记录id,purchaseMonths:购买月份（默认1）}
        log.info("客户购买或者续费投顾参数， list={}", list);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            CustomerInfoVO customerInfoVO = getCustomerInfo(RequestUtil.getOpenId(request));
            AdviserAddOrderDTO adviserAddOrderDTO = new AdviserAddOrderDTO();
            adviserAddOrderDTO.setPurchaserType(RoleCodeEnum.CUSTOMER.getCode());
            adviserAddOrderDTO.setPurchaserId(customerInfoVO.getId());
            List<AdviserRenewDTO> adviserRenewDTOS = JSONArray.parseArray(list, AdviserRenewDTO.class);
            adviserAddOrderDTO.setAdviserRenewDTOS(adviserRenewDTOS);
            log.info("投顾订单入参 adviserAddOrderDTO={}", JSON.toJSONString(adviserAddOrderDTO));
            result = adviserService.addAdviserOrder(adviserAddOrderDTO);
            log.info("投顾订单返回值 result={}", JSON.toJSONString(result));
            if (result.isSuccess()) {
                OrderCacheInfoVO orderCacheInfoVO = new OrderCacheInfoVO();
                AdviserOrderVO adviserOrderVO = result.getData(AdviserOrderVO.class);
                orderCacheInfoVO.setOrderNo(adviserOrderVO.getMainOrderNo());
                orderCacheInfoVO.setPrice(adviserOrderVO.getTotalMoney().toString());
                orderCacheService.putCacheOrder(orderCacheInfoVO);
                result.setData(orderCacheInfoVO.getOrderNo());
            }
        } catch (Exception e) {
            log.error("客户购买或者续费投顾异常, e={}", e);
        }
        log.info("客户购买或者续费投顾, result={}", JSON.toJSONString(result));
        return result;
    }


    /**
     * @author: zhangxia
     * @desc: 微信端获取 投顾列表（包含我购买的投顾和推荐的投顾）
     * @params: [openId, pageBean]
     * @methodName:adviserListPage
     * @date: 2018/3/12 0012 下午 16:38
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @ApiOperation(value = "微信 我的投顾和投顾列表的接口", notes = "[张霞]微信 微信端获取 投顾列表（包含我购买的投顾和推荐的投顾）接口")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功", response = WechatAdviserQueryVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "purchaseState", value = "策略购买状态 0 已购买 非0为所有策略", paramType = "query")
    })
    @RequestMapping(value = "/adviser/list", method = RequestMethod.POST)
    public Result adviserListPage(PageBean pageBean, HttpServletRequest request,WechatAdviserQueryDTO dto) {
        log.info("微信端获取 投顾列表（包含我购买的投顾和推荐的投顾） 入参参数dto={},pageBean={}",JSON.toJSONString(dto), JSON.toJSONString(pageBean));
        dto.setOpenId(RequestUtil.getOpenId(request));
        return wechatAdviserService.listWechatAdviser(dto, pageBean);
    }

    /**
     * @author: zhangxia
     * @desc: 微信端 投顾详情 接口
     * @params: [id]
     * @methodName:adviserDetail
     * @date: 2018/3/28 0028 上午 10:26
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @ApiOperation(value = "微信 投顾详情", notes = "[张霞]微信 投顾详情")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功", response = AdviserWechatDetailVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "Integer", name = "id", value = "投顾id", paramType = "path", required = true)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public Result adviserDetail(@PathVariable(value = "id") Integer id,HttpServletRequest request) {
        log.info("微信端 获取投顾详情 controller 入参参数id={}", id);
        CustomerInfoVO customerInfoVO = getCustomerInfo(RequestUtil.getOpenId(request));
        if (Objects.isNull(customerInfoVO)){
            return ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        }
        AdviserDetailDTO detailDTO = new AdviserDetailDTO();
        detailDTO.setPurchaserType(RoleCodeEnum.CUSTOMER.getCode());
        detailDTO.setPurchaserId(customerInfoVO.getId());
        detailDTO.setAdviserId(id);
        return wechatAdviserService.getWechantAdviserDetail(detailDTO);
    }

}
