package com.aq.controller.wechat.strategy;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.controller.base.BaseController;
import com.aq.controller.wechat.dto.RenewStrategyRequstDTO;
import com.aq.controller.wechat.dto.StrategyDTO;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.rediscache.ICacheService;
import com.aq.facade.dto.QueryStrategyRecommendDTO;
import com.aq.facade.dto.RenewStrategyDTO;
import com.aq.facade.dto.StrategyDetailDTO;
import com.aq.facade.dto.WechatStrategyQueryDTO;
import com.aq.facade.entity.StrategyRecommend;
import com.aq.facade.service.IStrategyRecommendService;
import com.aq.facade.service.IWechatStrategyService;
import com.aq.facade.vo.StrategyOrderVO;
import com.aq.facade.vo.StrategyWechatDetailVO;
import com.aq.facade.vo.WechatStrategyQueryVO;
import com.aq.facade.vo.customer.CustomerInfoVO;
import com.aq.facade.vo.order.OrderCacheInfoVO;
import com.aq.service.IOrderCacheService;
import com.aq.util.http.RequestUtil;
import com.aq.util.order.OrderBizCode;
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
 * @author:zhangxia
 * @createTime:14:21 2018-2-10
 * @version:1.0
 * @desc:微信端：我的量化-〉我的策略 模块controller
 */
@Slf4j
@RestController
@RequestMapping(value = "api/wechat/strategy")
@CrossOrigin(origins = "*")
@Api(value = "微信策略接口", description = "微信策略接口")
public class WechatStrategyController extends BaseController{

    @Reference(version = "1.0.0", check = false)
    IWechatStrategyService iWechatStrategyService;


    @Reference(version = "1.0.0", check = false)
    private IStrategyRecommendService strategyRecommendService;

    @Autowired
    private ICacheService redisCache;

    @Autowired
    private IOrderCacheService orderCacheService;

    /**
     * @Creater: 熊克文
     * @description：微信端获取我的策略
     */
    @ApiOperation(value = "微信 我的策略和策略精的接口", notes = "[熊克文]微信 我的策略和策略精的接口")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功", response = WechatStrategyQueryVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "purchaseState", value = "策略购买状态 0 已购买 非0为所有策略", paramType = "query")
    })
    @RequestMapping(value = "/strategyTrade", method = RequestMethod.POST)
    public Result strategyTrade(PageBean pageBean, HttpServletRequest request, WechatStrategyQueryDTO dto) {
        dto.setOpenId(RequestUtil.getOpenId(request));
        return this.iWechatStrategyService.listWechatStrategyQueryVO(dto, pageBean);
    }

    /**
     * @Creater: 熊克文
     * @description：微信端 策略详情
     */
    @ApiOperation(value = "微信 策略详情", notes = "[熊克文]微信 策略详情")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功", response = StrategyWechatDetailVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "Integer", name = "id", value = "策略id", paramType = "path", required = true)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public Result getWechantStrategyDetail(@PathVariable(value = "id") Integer id,HttpServletRequest request) {

        CustomerInfoVO customerInfoVO = getCustomerInfo(RequestUtil.getOpenId(request));
        if (Objects.isNull(customerInfoVO)){
            return ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        }
        StrategyDetailDTO detailDTO = new StrategyDetailDTO();
        detailDTO.setPurchaserType(RoleCodeEnum.CUSTOMER.getCode());
        detailDTO.setPurchaserId(customerInfoVO.getId());
        detailDTO.setStrategyId(id);
        return this.iWechatStrategyService.getWechantStrategyDetail(detailDTO);
    }

    /**
     * @Creater: zhangxia
     * @description：客户购买或者续费策略
     * @methodName: renewStrategy
     * @params: [renewStrategyRequstDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 17:33 2018-2-10
     */
    @ApiOperation(value = "微信 客户购买或者续费策略", notes = "[张霞]微信 客户购买或者续费策略")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功", response = WechatStrategyQueryVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openId", value = "微信openId", paramType = "header", required = true)
    })
    @RequestMapping(value = "renewOrBuy", method = RequestMethod.POST)
    public Result renewStrategy(HttpServletRequest request,String list){
        //list格式：{"recommendId":"0","strategyIds":[{"id":"","purchaseMonths":""},{"id":"","purchaseMonths":""}]}
        log.info("客户微信端对策略进行续费或者购买入参参数renewStrategyDTO={}",JSON.toJSONString(list));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            CustomerInfoVO customerInfoVO = getCustomerInfo(RequestUtil.getOpenId(request));
            RenewStrategyRequstDTO renewStrategyRequstDTO = JSON.parseObject(list,RenewStrategyRequstDTO.class);
            renewStrategyRequstDTO.setPurchaserId(customerInfoVO.getId());
            renewStrategyRequstDTO.setPurchaserType(RoleCodeEnum.CUSTOMER.getCode());
            List<RenewStrategyDTO> renewStrategyDTOS = new ArrayList<>();
            List<StrategyDTO> strategyDTOS = renewStrategyRequstDTO.getStrategyIds();
            if (strategyDTOS.size()>0){
                for (StrategyDTO strategyDTO:strategyDTOS
                        ) {
                    //获取策略推荐记录信息
                    QueryStrategyRecommendDTO qdto = new QueryStrategyRecommendDTO();
                    qdto.setStrategyId(strategyDTO.getId());
                    qdto.setRecommendedId(renewStrategyRequstDTO.getRecommendId());
                    qdto.setBeRecommendedId(renewStrategyRequstDTO.getPurchaserId());
                    qdto.setBeRecommendedRoleType(renewStrategyRequstDTO.getPurchaserType());
                    result = strategyRecommendService.getRecommendStrategy(qdto);
                    if (result.isSuccess() && result.getData() instanceof StrategyRecommend){
                        StrategyRecommend strategyRecommend= (StrategyRecommend) result.getData();
                        RenewStrategyDTO renewStrategyDTO = new RenewStrategyDTO();
                        renewStrategyDTO.setId(strategyDTO.getPurchaseId());
                        renewStrategyDTO.setRecommendId(renewStrategyRequstDTO.getRecommendId());
                        renewStrategyDTO.setPurchaserId(renewStrategyRequstDTO.getPurchaserId());
                        renewStrategyDTO.setPurchaserType(renewStrategyRequstDTO.getPurchaserType());
                        renewStrategyDTO.setPurchasePrice(strategyRecommend.getPushPrice());
                        renewStrategyDTO.setPurchaseMoney(strategyRecommend.getPushPrice().multiply(new BigDecimal(strategyDTO.getPurchaseMonths())));
                        renewStrategyDTO.setStrategyId(strategyDTO.getId());
                        renewStrategyDTOS.add(renewStrategyDTO);
                    }
                }
                if (renewStrategyDTOS.size()>0){
                    result = iWechatStrategyService.renewStrategyByCustomer(renewStrategyDTOS);
                    log.info("购买策略返回值，result={}", JSON.toJSONString(result));
                    if (result.isSuccess()) {
                        StrategyOrderVO strategyOrderVO = result.getData(StrategyOrderVO.class);
                        // redisCache.set(strategyOrderVO.getMainOrderNo(), strategyOrderVO.getTotalMoney(),8L, TimeUnit.HOURS);
						OrderCacheInfoVO infoVO = new OrderCacheInfoVO();
						infoVO.setOrderNo(strategyOrderVO.getMainOrderNo());
						infoVO.setPrice(strategyOrderVO.getTotalMoney().toString());
						orderCacheService.putCacheOrder(infoVO);
						redisCache.set(strategyOrderVO.getMainOrderNo() + OrderBizCode.INFO, strategyOrderVO.getList(),
								8L, TimeUnit.HOURS);
						result.setData(strategyOrderVO.getMainOrderNo());
                    }
                }else {
                    log.info("购买或者续费的策略没有推送过，非法操作");
                    result.setMessage("操作的策略没有进行推送");
                }
            }else {
                result.setMessage("策略不能为空");
            }
        }catch (Exception e){
            log.info("客户微信端对策略进行续费或者购买处理异常e={}",e);
        }
        log.info("客户微信端对策略进行续费或者购买处理结果result={}",JSON.toJSONString(result));
        return result;
    }
}
