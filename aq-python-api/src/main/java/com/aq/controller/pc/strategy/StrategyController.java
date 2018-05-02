package com.aq.controller.pc.strategy;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.controller.base.BaseController;
import com.aq.controller.wechat.dto.RenewStrategyRequstDTO;
import com.aq.controller.wechat.dto.StrategyDTO;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.rediscache.ICacheService;
import com.aq.facade.dto.*;
import com.aq.facade.dto.customer.QueryGroupDTO;
import com.aq.facade.entity.StrategyRecommend;
import com.aq.facade.service.IStrategyRecommendService;
import com.aq.facade.service.IStrategyService;
import com.aq.facade.service.IWechatStrategyService;
import com.aq.facade.service.customer.IGroupService;
import com.aq.facade.vo.*;
import com.aq.facade.vo.customer.QueryGroupVO;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.facade.vo.order.OrderCacheInfoVO;
import com.aq.service.IOrderCacheService;
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
 * 策略接入
 *
 * @author 熊克文
 * @createDate 2018\2\9
 **/
@RestController
@RequestMapping("/api/pc/strategy")
@Api(value = "策略管理接口", description = "策略管理接口")
@Slf4j
public class StrategyController extends BaseController {

    @Reference(version = "1.0.0", check = false)
    private IStrategyService iBoutiqueStrategyService;

    @Reference(version = "1.0.0", check = false)
    private IStrategyRecommendService strategyRecommendService;

    @Reference(version = "1.0.0", check = false)
    private IGroupService groupService;

    @Reference(version = "1.0.0", check = false)
    IWechatStrategyService iWechatStrategyService;

    @Autowired
    private ICacheService redisCache;

    @Autowired
    private IOrderCacheService orderCacheService;


    /**
     * @author: 熊克文
     * @Description: 策略详情查询
     * @return: Result
     */
    @ApiOperation(value = "PC策略详情查询", notes = "PC[熊克文]策略详情查询")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = StrategyDetailVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "策略id", paramType = "path", required = true),
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public Result detail(@PathVariable Integer id) {
        ManageInfoVO manageInfoVO = getManageInfo(true);
        StrategyDetailDTO detailDTO = new StrategyDetailDTO();
        detailDTO.setStrategyId(id);
        detailDTO.setPurchaserId(manageInfoVO.getId());
        /*detailDTO.setPurchaserId(1);*/
        detailDTO.setPurchaserType(RoleCodeEnum.MANAGER.getCode());
        return this.iBoutiqueStrategyService.getStrategyDetailVO(detailDTO);
    }

    /**
     * @author: 熊克文
     * @Description: 精品策略列表查询
     * @return: Result
     */
    @ApiOperation(value = "PC策略列表查询", notes = "PC[熊克文]策略列表查询")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = BoutiqueStrategyQueryVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "customerId", value = "客户id", paramType = "query"),
            @ApiImplicitParam(dataType = "int", name = "userId", value = "经理id", paramType = "query"),
            @ApiImplicitParam(dataType = "int", name = "purchaseState", value = "策略购买状态 0 已购买 1 未购买", paramType = "query"),
            @ApiImplicitParam(dataType = "int", name = "collectionState", value = "策略购买状态 0 已收藏 1 未收藏", paramType = "query"),
            @ApiImplicitParam(dataType = "int", name = "recommendState", value = "策略推荐状态  0 已推荐 1 未推荐", paramType = "query"),
            @ApiImplicitParam(dataType = "int", name = "orderType", value = "排序类型 asc-正序，desc-倒序", paramType = "query"),
            @ApiImplicitParam(dataType = "int", name = "orderByContent", value = "排序字段:1=年化收益(默认 desc),2=开始时间,3=今日收益,4=累计收益", paramType = "query")
    })
    @RequestMapping(value = "/boutiqueStrategy", method = RequestMethod.GET)
    public Result boutiqueStrategy(BoutiqueStrategyQueryDTO dto, PageBean pageBean) {
        ManageInfoVO manageInfoVO = getManageInfo(true);
        dto.setUserId(manageInfoVO.getId());
        dto.setUserRoleCode(RoleCodeEnum.MANAGER.getCode());
        return this.iBoutiqueStrategyService.listStrategyQueryVO(dto, pageBean);
    }

    /**
     * @author: 熊克文
     * @Description: 策略推送信息列表查询
     * @return: Result
     */
    @ApiOperation(value = "PC策略推送信息分页查询", notes = "PC[熊克文]策略推送信息分页查询")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = StrategyPushQueryVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "id", value = "策略id", paramType = "path", required = true)
    })
    @RequestMapping(value = "/strategyPush/{id}", method = RequestMethod.GET)
    public Result strategyPush(@PathVariable Integer id, PageBean pageBean) {
        return this.iBoutiqueStrategyService.listStrategyPushQueryVO(id, pageBean);
    }

    /**
     * @author: 熊克文
     * @Description: 策略推荐列表查询
     * @return: Result
     */
    @ApiOperation(value = "PC策略推荐分页查询", notes = "PC[熊克文]策略推荐分页查询")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = StrategyRecommendVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "strategyId", value = "策略id", paramType = "path", required = true)
    })
    @RequestMapping(value = "/StrategyRecommend/{strategyId}", method = RequestMethod.GET)
    public Result strategyRecommend(PageBean pageBean, @PathVariable Integer strategyId) {
        ManageInfoVO manageInfoVO = getManageInfo(true);
        return this.iBoutiqueStrategyService.listStrategyRecommendVO(strategyId, manageInfoVO.getId(), pageBean);
    }

    /**
     * @author: 熊克文
     * @Description: 策略购买列表查询
     * @return: Result
     */
    @ApiOperation(value = "PC策略购买分页查询", notes = "PC[熊克文]策略购买分页查询")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = StrategyPurchaseVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "strategyId", value = "策略id", paramType = "path", required = true)
    })
    @RequestMapping(value = "/strategyPurchase/{strategyId}", method = RequestMethod.GET)
    public Result strategyPurchase(PageBean pageBean, @PathVariable Integer strategyId) {
        ManageInfoVO manageInfoVO = getManageInfo(true);
        return this.iBoutiqueStrategyService.listStrategyPurchaseVO(strategyId, manageInfoVO.getId(), pageBean);
    }

    /**
     * @author: 熊克文
     * @Description: 策略持仓列表查询
     * @return: Result
     */
    @ApiOperation(value = "PC策略持仓分页查询", notes = "PC[熊克文]策略持仓分页查询")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = StrategyPositionVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "strategyId", value = "策略id", paramType = "path", required = true)
    })
    @RequestMapping(value = "/strategyPosition/{strategyId}", method = RequestMethod.GET)
    public Result strategyPosition(PageBean pageBean, @PathVariable Integer strategyId) {
        return this.iBoutiqueStrategyService.listStrategyPositionVO(strategyId, pageBean);
    }

    /**
     * @Creater: zhangxia
     * @description：客户经理收藏策略
     * @methodName: collectionStrategy
     * @params: [collectionStrategyDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 17:39 2018-2-12
     */
    @ApiOperation(value = "PC客户经理收藏策略", notes = "PC[张霞]客户经理收藏策略")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "strategyId", value = "策略id", paramType = "query", required = true)
    })
    @RequestMapping(value = "/collectionStrategy", method = RequestMethod.GET)
    @ResponseBody
    public Result collectionStrategy(CollectionStrategyDTO collectionStrategyDTO) {
        log.info("客户经理收藏策略controller层入参参数collectionStrategyDTO={}", JSON.toJSONString(collectionStrategyDTO));
        ManageInfoVO manageInfoVO = getManageInfo(true);
        collectionStrategyDTO.setManagerId(manageInfoVO.getId());
        return strategyRecommendService.collectionStrategy(collectionStrategyDTO);
    }

    /**
     * @Creater: zhangxia
     * @description：更新客户经理收藏策略
     * @methodName: updateCollectionStrategy
     * @params: [collectionStrategyUpdateDTO]
     * @return: com.aq.util.result.Result
     * @createTime: 18:02 2018-2-12
     */
    @ApiOperation(value = "PC客户经理取消收藏策略", notes = "PC[张霞]客户经理取消收藏策略")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "id", value = "收藏策略表id", paramType = "query", required = true)
    })
    @RequestMapping(value = "/updateCollectionStrategy", method = RequestMethod.GET)
    @ResponseBody
    public Result updateCollectionStrategy(CollectionStrategyUpdateDTO collectionStrategyUpdateDTO) {
        log.info("PC客户经理取消收藏策略controller入参参数collectionStrategyUpdateDTO={}", JSON.toJSONString(collectionStrategyUpdateDTO));
        ManageInfoVO manageInfoVO = getManageInfo(true);
        collectionStrategyUpdateDTO.setManagerId(manageInfoVO.getId());
        return strategyRecommendService.updateCollectionStrategy(collectionStrategyUpdateDTO);
    }

    /**
     * @Creater: zhangxia
     * @description：客户经理推荐策略时获取客户信息列表
     * @methodName: getStrategyCustomers
     * @params: [dto, pageBean]
     * @return: com.aq.util.result.Result
     * @createTime: 18:48 2018-2-12
     */
    @ApiOperation(value = "PC客户经理推荐策略时获取客户信息列表", notes = "PC[张霞]客户经理推荐策略时获取客户信息列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = StrategyCustomListVO.class),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "strategyId", value = "策略id", paramType = "query", required = true),
            @ApiImplicitParam(dataType = "String", name = "customMessage", value = "客户信息【姓名或者手机号】", paramType = "query", required = false),
            @ApiImplicitParam(dataType = "int", name = "groupId", value = "客户经理分组的id", paramType = "query", required = false)

    })
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public Result getStrategyCustomers(StrategyCustomListDTO dto, PageBean pageBean) {
        ManageInfoVO manageInfoVO = getManageInfo(true);
        dto.setManagerId(manageInfoVO.getId());
        log.info("客户经理推荐策略时获取客户信息列表入参参数dto={}，pageBean={}", JSON.toJSONString(dto), JSON.toJSONString(pageBean));
        return strategyRecommendService.getStrategyCustomers(dto, pageBean);
    }


    @ApiOperation(value = "PC客户经理获取自己的分组列表", notes = "PC[张霞]客户经理获取自己的分组列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = QueryGroupVO.class),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "managerId", value = "客户经理id", paramType = "query", required = true)
    })
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public Result getGroupList(QueryGroupDTO queryGroupDTO) {
        log.info("客户经理获取自己的分组列表入参参数queryGroupDTO={}", JSON.toJSONString(queryGroupDTO));
        return groupService.getGroupList(queryGroupDTO);
    }

    @ApiOperation(value = "PC客户经理推荐策略给客户", notes = "PC[张霞]客户经理推荐策略给客户")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = QueryGroupVO.class),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "list", value = "推荐策略数组数据", paramType = "query", required = true)
    })
    @RequestMapping(value = "/recommendStrategyToCustom", method = RequestMethod.GET)
    public Result recommendStrategyToCustom(HttpServletRequest request, String list) {
        //list的格式：{"strategyId":"","strategyCustomDTOS":[{"isPush":"","customerId":"","pushPrice":""},{"isPush":"","customerId":"","pushPrice":""}]}
        log.info("客户经理推荐策略给客户controller入参参数list={}", JSON.toJSONString(list));
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        StrategyRecommendDTO strategyRecommendDTO = JSON.parseObject(list, StrategyRecommendDTO.class);
        strategyRecommendDTO.setManagerId(manageInfoVO.getId());
        strategyRecommendDTO.setBeRecommendedRoleType(RoleCodeEnum.CUSTOMER.getCode());
        log.info("转换后的dto={}", JSON.toJSONString(strategyRecommendDTO));
        List<StrategyCustomDTO>  strategyCustomDTOS = strategyRecommendDTO.getStrategyCustomDTOS();
        for (StrategyCustomDTO strategyCustomDTO:
            strategyCustomDTOS) {
            if (Objects.nonNull(strategyCustomDTO.getPushPrice())&&strategyCustomDTO.getPushPrice().doubleValue()<=0){
                log.info("策略推荐价格有误,入参参数list={}",JSON.toJSONString(list));
                return ResultUtil.setResult(false,"策略推荐价格有误(必须大于0)");
            }
        }

        return strategyRecommendService.recommendStrategy(strategyRecommendDTO);
    }


    @ApiOperation(value = "PC客户经理续费或者购买策略", notes = "PC[张霞]客户经理续费或者购买策略")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = QueryGroupVO.class),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "list", value = "客户经理续费或者购买策略数组数据", paramType = "query", required = true)
    })
    @RequestMapping(value = "renewOrBuy", method = RequestMethod.POST)
    @ResponseBody
    public Result renewStrategy(HttpServletRequest request, String list) {
        //list格式：{"recommendId":"0","strategyIds":[{"id":"","purchaseMonths":""},{"id":"","purchaseMonths":""}]}
        log.info("客户经理PC端对策略进行续费或者购买入参参数renewStrategyDTO={}", list);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            ManageInfoVO manageInfoVO = getManageInfo(request, false);
            RenewStrategyRequstDTO renewStrategyRequstDTO = JSON.parseObject(list, RenewStrategyRequstDTO.class);
            renewStrategyRequstDTO.setPurchaserId(manageInfoVO.getId());
            renewStrategyRequstDTO.setPurchaserType(RoleCodeEnum.MANAGER.getCode());
            List<RenewStrategyDTO> renewStrategyDTOS = new ArrayList<>();
            List<StrategyDTO> strategyDTOS = renewStrategyRequstDTO.getStrategyIds();
            if (strategyDTOS.size() > 0) {
                for (StrategyDTO strategyDTO : strategyDTOS) {
                    //获取策略推荐记录信息
                    QueryStrategyRecommendDTO qdto = new QueryStrategyRecommendDTO();
                    qdto.setStrategyId(strategyDTO.getId());
                    qdto.setRecommendedId(renewStrategyRequstDTO.getRecommendId());
                    qdto.setBeRecommendedId(renewStrategyRequstDTO.getPurchaserId());
                    qdto.setBeRecommendedRoleType(renewStrategyRequstDTO.getPurchaserType());
                    result = strategyRecommendService.getRecommendStrategy(qdto);
                    if (result.isSuccess() && result.getData() instanceof StrategyRecommend) {
                        StrategyRecommend strategyRecommend = (StrategyRecommend) result.getData();
                        RenewStrategyDTO renewStrategyDTO = new RenewStrategyDTO();
                        renewStrategyDTO.setId(strategyDTO.getPurchaseId());
                        renewStrategyDTO.setRecommendId(strategyRecommend.getId());
                        renewStrategyDTO.setPurchaserId(renewStrategyRequstDTO.getPurchaserId());
                        renewStrategyDTO.setPurchaserType(renewStrategyRequstDTO.getPurchaserType());
                        renewStrategyDTO.setPurchasePrice(strategyRecommend.getPushPrice());
                        renewStrategyDTO.setPurchaseMoney(strategyRecommend.getPushPrice().multiply(new BigDecimal(strategyDTO.getPurchaseMonths())));
                        renewStrategyDTO.setStrategyId(strategyDTO.getId());
                        renewStrategyDTOS.add(renewStrategyDTO);
                    }
                }
                if (renewStrategyDTOS.size() > 0) {
                    result = iWechatStrategyService.renewStrategyByCustomer(renewStrategyDTOS);
                    log.info("购买策略返回值，result={}", JSON.toJSONString(result));
                    if (result.isSuccess()) {
                        StrategyOrderVO strategyOrderVO = result.getData(StrategyOrderVO.class);
//                        redisCache.set(strategyOrderVO.getMainOrderNo(), strategyOrderVO.getTotalMoney(), 8L, TimeUnit.HOURS);
                        OrderCacheInfoVO infoVO = new OrderCacheInfoVO();
                        infoVO.setOrderNo(strategyOrderVO.getMainOrderNo());
                        infoVO.setPrice(strategyOrderVO.getTotalMoney().toString());
                        orderCacheService.putCacheOrder(infoVO);
                        redisCache.set(strategyOrderVO.getMainOrderNo() + OrderBizCode.INFO, strategyOrderVO.getList(), 8L, TimeUnit.HOURS);
                        result.setData(strategyOrderVO.getMainOrderNo());
                    }
                } else {
                    log.info("购买或者续费的策略没有推送过，非法操作");
                    result.setMessage("操作的策略没有进行推送");
                }
            } else {
                result.setMessage("策略不能为空");
            }
        } catch (Exception e) {
            log.info("客户经理PC端对策略进行续费或者购买处理异常e={}", e);
        }
        log.info("客户经理PC端对策略进行续费或者购买处理结果result={}", JSON.toJSONString(result));
        return result;
    }
}
