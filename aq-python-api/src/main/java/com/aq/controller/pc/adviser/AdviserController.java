package com.aq.controller.pc.adviser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aq.aop.AccessToken;
import com.aq.controller.base.BaseController;
import com.aq.controller.wechat.dto.AdviserDTO;
import com.aq.controller.wechat.dto.RenewAdviserRequestDTO;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.rediscache.ICacheService;
import com.aq.facade.dto.AdviseUpdateDTO;
import com.aq.facade.dto.AdviserAddDTO;
import com.aq.facade.dto.AdviserAddOrderDTO;
import com.aq.facade.dto.AdviserCollectionDTO;
import com.aq.facade.dto.AdviserCollectionUpdateDTO;
import com.aq.facade.dto.AdviserCustomListDTO;
import com.aq.facade.dto.AdviserDetailDTO;
import com.aq.facade.dto.AdviserListRefreshDTO;
import com.aq.facade.dto.AdviserQueryDTO;
import com.aq.facade.dto.AdviserQueryRecommendDTO;
import com.aq.facade.dto.AdviserRecommendCustomDTO;
import com.aq.facade.dto.AdviserRecommendDTO;
import com.aq.facade.dto.AdviserRecommendUpdateDTO;
import com.aq.facade.dto.AdviserRenewDTO;
import com.aq.facade.dto.RenewAdviserDTO;
import com.aq.facade.service.IAdviserPositionService;
import com.aq.facade.service.IAdviserRecommendService;
import com.aq.facade.service.IAdviserService;
import com.aq.facade.vo.AdviserCustomListVO;
import com.aq.facade.vo.AdviserDetailVO;
import com.aq.facade.vo.AdviserInfoVO;
import com.aq.facade.vo.AdviserListRefreshVO;
import com.aq.facade.vo.AdviserListVO;
import com.aq.facade.vo.AdviserOrderVO;
import com.aq.facade.vo.AdviserPositionAndTradeVO;
import com.aq.facade.vo.AdviserPushQueryVO;
import com.aq.facade.vo.AdviserRecommendVO;
import com.aq.facade.vo.RealTimeAdviserVO;
import com.aq.facade.vo.manage.ManageInfoVO;
import com.aq.facade.vo.order.OrderCacheInfoVO;
import com.aq.service.IOrderCacheService;
import com.aq.util.page.PageBean;
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
 * 投顾 controller
 *
 * @author 郑朋
 * @create 2018/3/7
 **/

@RestController
@RequestMapping("/api/pc/advisers")
@Api(value = "投顾接口", description = "投顾接口")
@Slf4j
public class AdviserController extends BaseController {

    @Reference(version = "1.0.0")
    private IAdviserService adviserService;

    @Reference(version = "1.0.0")
    private IAdviserRecommendService adviserRecommendService;

    @Reference(version = "1.0.0")
    private IAdviserPositionService adviserPositionService;

    @Autowired
    private ICacheService redisCache;

    @Autowired
    private IOrderCacheService orderCacheService;

    @ApiOperation(value = "新增投顾会", notes = "[郑朋]新增投顾会")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "adviser", method = RequestMethod.POST)
    @AccessToken
    public Result addAdviser(AdviserAddDTO adviserAddDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(false);
        adviserAddDTO.setManagerId(manageInfoVO.getId());
        log.info("新增投顾会入参：adviserAddDTO={}", JSON.toJSONString(adviserAddDTO));
        Result result = adviserService.addAdviser(adviserAddDTO);
        log.info("新增投顾会返回值：result={}", JSON.toJSONString(result));
        return result;
    }


    @ApiOperation(value = "投顾会详情", notes = "[郑朋]投顾会详情")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = AdviserInfoVO.class)
    })
    @RequestMapping(value = "adviser", method = RequestMethod.GET)
    @AccessToken
    public Result getAdviser() {
        ManageInfoVO manageInfoVO = getManageInfo(false);
        log.info("投顾会详情入参：managerId={}", manageInfoVO.getId());
        Result result = adviserService.getAdviser(manageInfoVO.getId());
        log.info("投顾会详情返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "修改投顾会", notes = "[郑朋]修改投顾会")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "adviser", method = RequestMethod.PUT)
    @AccessToken
    public Result modifyAdviser(AdviseUpdateDTO adviseUpdateDTO) {
        ManageInfoVO manageInfoVO = getManageInfo(false);
        adviseUpdateDTO.setManagerId(manageInfoVO.getId());
        log.info("修改投顾会入参：adviseUpdateDTO={}", JSON.toJSONString(adviseUpdateDTO));
        Result result = adviserService.modifyAdviser(adviseUpdateDTO);
        log.info("修改投顾会返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "删除投顾会", notes = "[郑朋]删除投顾会")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "adviser", method = RequestMethod.DELETE)
    @AccessToken
    public Result deleteAdviser() {
        ManageInfoVO manageInfoVO = getManageInfo(false);
        log.info("修改投顾会入参：managerId={}", manageInfoVO.getId());
        Result result = adviserService.deleteAdviser(manageInfoVO.getId());
        log.info("修改投顾会返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "委托记录", notes = "[郑朋]委托记录")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = AdviserPushQueryVO.class)
    })
    @RequestMapping(value = "adviser/push", method = RequestMethod.GET)
    @AccessToken
    public Result getAdviserPush(PageBean pageBean) {
        ManageInfoVO manageInfoVO = getManageInfo(false);
        log.info("获取客户经理的委托记录入参：managerId={},pageBean={}", manageInfoVO.getId(), JSON.toJSONString(pageBean));
        Result result = adviserService.getAdviserPush(manageInfoVO.getId(), pageBean);
        log.info("获取客户经理的委托记录返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "委托记录-撤单", notes = "[郑朋]委托记录-撤单")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "adviser/push/{pushId}", method = RequestMethod.PUT)
    @AccessToken
    public Result modifyAdviserPush(@PathVariable(value = "pushId") Integer pushId) {
        log.info("客户经理的委托记录 撤单入参：pushId={}", pushId);
        Result result = adviserService.modifyAdviserPush(pushId);
        log.info("客户经理的委托记录 撤单返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "客户经理续费或者购买投顾", notes = "[郑朋]客户经理续费或者购买投顾")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "list", value = "客户经理续费或者购买投顾", paramType = "query", required = true)
    })
    //@RequestMapping(value = "adviser/renewOrBuy", method = RequestMethod.POST)
    @ResponseBody
    @Deprecated
    public Result renewStrategy(String list) {
        //list格式：{"purchaseType":"","adviserDTOS":[{"id":"","purchaseMonths":"","purchaseId":"","recommendId":""},{"id":"","purchaseMonths":"","purchaseId":"","recommendId":""}]}
        log.info("客户经理续费或者购买投顾入参，renewStrategyDTO={}", list);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            ManageInfoVO manageInfoVO = getManageInfo(true);
            RenewAdviserRequestDTO renewAdviserRequestDTO = JSON.parseObject(list, RenewAdviserRequestDTO.class);
            renewAdviserRequestDTO.setPurchaserId(manageInfoVO.getId());
            renewAdviserRequestDTO.setPurchaserType(RoleCodeEnum.MANAGER.getCode());
            List<AdviserDTO> adviserDTOS = renewAdviserRequestDTO.getAdviserDTOS();
            List<RenewAdviserDTO> renewAdviserDTOS = new ArrayList<>();
            RenewAdviserDTO renewAdviserDTO;
            for (AdviserDTO adviserDTO : adviserDTOS) {
                if (manageInfoVO.getId().equals(adviserDTO.getRecommendId())) {
                    result.setMessage("自己不能购买自己的投顾会");
                    return result;
                }
                //查询投顾价格
                Result adviserResult = adviserService.getAdviser(adviserDTO.getRecommendId());
                if (!adviserResult.isSuccess() || adviserResult.getData(AdviserInfoVO.class) == null) {
                    result.setMessage("购买失败");
                    return result;
                }

                AdviserInfoVO adviserInfoVO = adviserResult.getData(AdviserInfoVO.class);
                renewAdviserDTO = new RenewAdviserDTO();
                renewAdviserDTO.setId(adviserDTO.getPurchaseId());
                //客户经理购买不存在推荐 -这里存的是创建投顾客户经理的id
                renewAdviserDTO.setRecommendId(adviserInfoVO.getManagerId());
                renewAdviserDTO.setManagerId(adviserInfoVO.getManagerId());
                renewAdviserDTO.setPurchaserId(renewAdviserRequestDTO.getPurchaserId());
                renewAdviserDTO.setPurchaserType(renewAdviserRequestDTO.getPurchaserType());
                renewAdviserDTO.setPurchasePrice(new BigDecimal(adviserInfoVO.getPrice()));
                renewAdviserDTO.setPurchaseMoney(new BigDecimal(adviserInfoVO.getPrice()).multiply(new BigDecimal(adviserDTO.getPurchaseMonths())));
                renewAdviserDTO.setAdviserId(adviserInfoVO.getId());
                renewAdviserDTO.setPurchaseType(renewAdviserRequestDTO.getPurchaseType());
                renewAdviserDTOS.add(renewAdviserDTO);
            }
            result = adviserService.addAdviserOrder(renewAdviserDTOS);
            log.info("投顾订单返回值 result={}", JSON.toJSONString(result));
            if (result.isSuccess()) {
                AdviserOrderVO adviserOrderVO = result.getData(AdviserOrderVO.class);
                redisCache.set(adviserOrderVO.getMainOrderNo(), adviserOrderVO.getTotalMoney(), 8L, TimeUnit.HOURS);
                //redisCache.set(adviserOrderVO.getMainOrderNo() + OrderBizCode.INFO, adviserOrderVO.getAdviserOrders(), 8L, TimeUnit.HOURS);
                result.setData(adviserOrderVO.getMainOrderNo());
            }
        } catch (Exception e) {
            log.error("户经理续费或者购买投顾异常, e={}", e);
        }
        log.info("户经理续费或者购买投顾, result={}", JSON.toJSONString(result));
        return result;
    }


    @ApiOperation(value = "投顾下单", notes = "[郑朋]投顾下单")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "list", value = "投顾下单{adviserId:投顾id,recommendId:推荐记录id,purchaseMonths:购买月份（默认1）}", paramType = "query", required = true)
    })
    @RequestMapping(value = "adviser/renewOrBuy", method = RequestMethod.POST)
    @ResponseBody
    public Result addOrder(String list) {
        //{adviserId:投顾id,recommendId:推荐记录id,purchaseMonths:购买月份（默认1）}
        log.info("客户经理续费或者购买投顾入参，list={}", list);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            ManageInfoVO manageInfoVO = getManageInfo(true);
            AdviserAddOrderDTO adviserAddOrderDTO = new AdviserAddOrderDTO();
            adviserAddOrderDTO.setPurchaserType(RoleCodeEnum.MANAGER.getCode());
            adviserAddOrderDTO.setPurchaserId(manageInfoVO.getId());
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
            log.error("户经理续费或者购买投顾异常, e={}", e);
        }
        log.info("户经理续费或者购买投顾, result={}", JSON.toJSONString(result));
        return result;
    }


    /**
     * @author: zhangxia
     * @desc: 客户经理 分页查询 投顾汇 列表接口
     * @params: [dto, pageBean]
     * @methodName:listAdviserPage
     * @date: 2018/3/7 0007 下午 16:25
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @ApiOperation(value = "PC客户经理分页查询投顾汇列表", notes = "PC[张霞]投顾汇列表查询（投顾组合-我购买的）")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = AdviserListVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "customerId", value = "客户id", paramType = "query"),
            @ApiImplicitParam(dataType = "int", name = "managerId", value = "经理id", paramType = "query"),
            @ApiImplicitParam(dataType = "int", name = "purchaseState", value = "投顾购买状态 0 已购买  1未购买  2 自己创建的", paramType = "query"),
            @ApiImplicitParam(dataType = "int", name = "collectionState", value = "投顾收藏状态 0 已收藏 1 未收藏", paramType = "query"),
            @ApiImplicitParam(dataType = "int", name = "recommendState", value = "投顾推荐状态  0 已推荐 1 未推荐", paramType = "query"),
            @ApiImplicitParam(dataType = "int", name = "orderType", value = "排序类型 asc-正序，desc-倒序", paramType = "query"),
            @ApiImplicitParam(dataType = "int", name = "orderByContent", value = "排序字段:1=年化收益(默认 desc),2=开始时间,3=今日收益,4=累计收益", paramType = "query")
    })
    @RequestMapping(value = "/listAdviserPage", method = RequestMethod.GET)
    public Result listAdviserPage(AdviserQueryDTO dto, PageBean pageBean) {
        log.info("PC客户经理分页查询投顾汇列表controller入参参数dto={}，pageBean={}", JSON.toJSONString(dto), JSON.toJSONString(pageBean));
        ManageInfoVO manageInfoVO = getManageInfo(true);
        dto.setManagerId(manageInfoVO.getId());
        dto.setCollectionerType(RoleCodeEnum.MANAGER.getCode());
        dto.setOrderType("".equals(dto.getOrderType())||null==dto.getOrderType()?null: dto.getOrderType().toLowerCase());
        return adviserRecommendService.getListAdviserPage(dto, pageBean);
    }

    /**
     * @author: zhangxia
     * @desc:投顾汇详情查询
     * @params: [id]
     * @methodName:getAdviserDetail
     * @date: 2018/3/7 0007 下午 16:37
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @ApiOperation(value = "PC投顾汇详情查询", notes = "PC[张霞]投顾汇详情查询")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = AdviserDetailVO.class)
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "int", name = "id", value = "投顾id", paramType = "path", required = true),
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true)
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result getAdviserDetail(@PathVariable Integer id) {
        log.info("PC投顾汇详情查询controller入参参数 投顾id={}", id);
        ManageInfoVO manageInfoVO = getManageInfo(true);
        AdviserDetailDTO adviserDetailDTO = new AdviserDetailDTO();
        adviserDetailDTO.setAdviserId(id);
        adviserDetailDTO.setPurchaserId(manageInfoVO.getId());
        adviserDetailDTO.setPurchaserType(RoleCodeEnum.MANAGER.getCode());
        return adviserRecommendService.getAdviserDetailVO(adviserDetailDTO);
    }

    /**
     * @author: zhangxia
     * @desc: 收藏者收藏投顾汇
     * @params: [dto]
     * @methodName:collectionStrategy
     * @date: 2018/3/8 0008 下午 15:08
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @ApiOperation(value = "PC客户经理收藏投顾", notes = "PC[张霞]客户经理收藏投顾")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "adviserId", value = "投顾id", paramType = "query", required = true)
    })
    @RequestMapping(value = "/collectionAdviser", method = RequestMethod.GET)
    @ResponseBody
    public Result collectionAdviser(AdviserCollectionDTO dto) {
        log.info("客户经理收藏投顾controller层入参参数dto={}", JSON.toJSONString(dto));
        ManageInfoVO manageInfoVO = getManageInfo(true);
        dto.setCollectionerId(manageInfoVO.getId());
        dto.setCollectionerType(RoleCodeEnum.MANAGER.getCode());
        return adviserRecommendService.collectionAdviser(dto);
    }

    @ApiOperation(value = "PC客户经理取消收藏投顾", notes = "PC[张霞]客户经理取消收藏投顾")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "id", value = "收藏投顾表id", paramType = "query", required = true)
    })
    @RequestMapping(value = "/updateCollectionAdviser", method = RequestMethod.GET)
    @ResponseBody
    public Result updateCollectionAdviser(AdviserCollectionUpdateDTO dto) {
        log.info("PC客户经理取消收藏投顾controller入参参数dto={}", JSON.toJSONString(dto));
        ManageInfoVO manageInfoVO = getManageInfo(true);
        dto.setCollectionerId(manageInfoVO.getId());
        dto.setCollectionerType(RoleCodeEnum.MANAGER.getCode());
        return adviserRecommendService.updateCollectionAdviser(dto);
    }

    /**
     * @author: zhangxia
     * @desc: 客户经理推荐投顾时获取客户信息列表
     * @params: [dto, pageBean]
     * @methodName:getStrategyCustomers
     * @date: 2018/3/8 0008 下午 15:16
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @ApiOperation(value = "PC客户经理推荐投顾时获取客户信息列表", notes = "PC[张霞]客户经理推荐投顾时获取客户信息列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = AdviserCustomListVO.class),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "adviserId", value = "投顾id", paramType = "query", required = true),
            @ApiImplicitParam(dataType = "String", name = "customMessage", value = "客户信息【姓名或者手机号】", paramType = "query", required = false),
            @ApiImplicitParam(dataType = "int", name = "groupId", value = "客户经理分组的id", paramType = "query", required = false)

    })
    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public Result getAdviserCustomers(AdviserCustomListDTO dto, PageBean pageBean) {
        ManageInfoVO manageInfoVO = getManageInfo(true);
        dto.setManagerId(manageInfoVO.getId());
        log.info("客户经理推荐投顾时获取客户信息列表入参参数dto={}，pageBean={}", JSON.toJSONString(dto), JSON.toJSONString(pageBean));
        return adviserRecommendService.recommendAdviserGetCustomers(dto, pageBean);
    }


    /**
     * @author: zhangxia
     * @desc: PC客户经理推荐投顾给客户
     * @params: [request, list]
     * @methodName:recommendStrategyToCustom
     * @date: 2018/3/8 0008 下午 18:32
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @ApiOperation(value = "PC客户经理推荐投顾给客户", notes = "PC[张霞]客户经理推荐投顾给客户")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "list", value = "推荐投顾数组数据", paramType = "query", required = true)
    })
    @RequestMapping(value = "/recommendAdviserToCustom", method = RequestMethod.GET)
    public Result recommendAdviserToCustom(HttpServletRequest request, String list) {
        //list的格式：{"adviserId":"","adviserRecommendCustomDTOS":[{"isPush":"","customerId":"","pushPrice":"","beRecommendedRoleType":""},{"isPush":"","customerId":"","pushPrice":"","beRecommendedRoleType":""}]}
        log.info("客户经理推荐投顾给客户controller入参参数list={}", JSON.toJSONString(list));
        ManageInfoVO manageInfoVO = getManageInfo(request, false);
        AdviserRecommendDTO adviserRecommendDTO = JSON.parseObject(list, AdviserRecommendDTO.class);
        adviserRecommendDTO.setManagerId(manageInfoVO.getId());
        log.info("转换后的dto={}", JSON.toJSONString(adviserRecommendDTO));
        List<AdviserRecommendCustomDTO> recommendCustomDTOList = adviserRecommendDTO.getAdviserRecommendCustomDTOS();
        for (AdviserRecommendCustomDTO dto :
                recommendCustomDTOList) {
            if (Objects.nonNull(dto.getPushPrice()) && dto.getPushPrice().doubleValue() <= 0 && RoleCodeEnum.CUSTOMER.getCode().equals(dto.getBeRecommendedRoleType())) {
                log.info("投顾推荐价格有误,入参参数list={}", JSON.toJSONString(list));
                return ResultUtil.setResult(false, "投顾推荐价格有误(必须大于0)");
            }
        }
        return adviserRecommendService.recommendAdviser(adviserRecommendDTO);
    }


    /**
     * @author: zhangxia
     * @desc:客户经理更新投顾是否微信推送
     * @params: [dto]
     * @methodName:updateAdviserRecommendPushstate
     * @date: 2018/3/8 0008 下午 18:29
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @ApiOperation(value = "PC客户经理更新投顾是否微信推送", notes = "PC[张霞]客户经理更新投顾是否微信推送")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
    })
    @RequestMapping(value = "/updateAdviserRecommendPushstate", method = RequestMethod.GET)
    public Result updateAdviserRecommendPushstate(AdviserRecommendUpdateDTO dto) {
        log.info("客户经理更新投顾是否微信推送controller入参参数dto={}", JSON.toJSONString(dto));
        return adviserRecommendService.updateRecommendAdviserPushstate(dto);
    }

    /**
     * @author: zhangxia
     * @desc: 客户经理获取推荐投顾的客户列表
     * @params: []
     * @methodName:recommendAdviserList
     * @date: 2018/3/9 0009 下午 15:06
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @ApiOperation(value = "PC客户经理获取推荐投顾的客户列表", notes = "PC[张霞]客户经理获取推荐投顾的客户列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功", response = AdviserRecommendVO.class),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
    })
    @RequestMapping(value = "/recommendAdviserList", method = RequestMethod.GET)
    public Result recommendAdviserList() {
        log.info("进入 客户经理获取推荐投顾的客户列表");
        ManageInfoVO manageInfoVO = getManageInfo(true);
        AdviserQueryRecommendDTO dto = new AdviserQueryRecommendDTO();
        dto.setRecommendedId(manageInfoVO.getId());
        return adviserRecommendService.recommendAdviserList(dto);
    }

    /**
     * @author: zhangxia
     * @desc:
     * @params: [updateDTO]
     * @methodName:updateRecommendAdviserDelete
     * @date: 2018/3/9 0009 下午 16:26
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    @ApiOperation(value = "PC删除客户经理推荐给客户的投顾", notes = "PC[张霞]删除客户经理推荐给客户的投顾")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
    })
    @RequestMapping(value = "/deleteRecommendAdviser", method = RequestMethod.GET)
    public Result updateRecommendAdviserDelete(AdviserRecommendUpdateDTO updateDTO) {
        log.info("删除客户经理推荐给客户的投顾 controller 入参参数updateDTO={}", JSON.toJSONString(updateDTO));
        ManageInfoVO manageInfoVO = getManageInfo(true);
        if (Objects.isNull(updateDTO.getId()) || Objects.isNull(manageInfoVO)) {
            log.info("没有登录或者传入推荐id有误");
            Result result = ResultUtil.getResult(RespCode.Code.FAIL);
            return result;
        }
        return adviserRecommendService.updateRecommendAdviserDelete(updateDTO);
    }

    /**
     * @param @param  adviserId
     * @param @param  pageBean
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getPosition
     * @Description: 查询投顾持仓列表
     */
    @ApiOperation(value = "投顾持仓列表", notes = "[李杰]投顾持仓列表")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "adviserId", value = "投顾ID", paramType = "query", required = true),
            @ApiImplicitParam(dataType = "int", name = "isQueryTrade", value = "是否查询资产：0、否 1、是", paramType = "query", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = AdviserPositionAndTradeVO.class)
    })
    @RequestMapping(value = "positions", method = RequestMethod.GET)
    @AccessToken
    public Result getPosition(@RequestParam Integer adviserId, @RequestParam Byte isQueryTrade, PageBean pageBean) {
        Result result = adviserPositionService.getAdviserPositionByPage(isQueryTrade, adviserId, pageBean);
        log.info("查询投顾持仓列表返回数据={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "PC客户经理实时刷新查询投顾今日收益数据", notes = "PC[张霞]客户经理实时刷新查询投顾今日收益数据")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功",response = AdviserListRefreshVO.class),
            @ApiResponse(code = 300, message = "操作失败")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "String", name = "list", value = "投顾id数组", paramType = "query", required = true)
    })
    @RequestMapping(value = "/adviserListRefresh", method = RequestMethod.GET)
    @AccessToken
    public Result getAdviserListRefresh(String list){
        //list的格式：{"adviserIds":["",""]}
        log.info("客户经理实时刷新查询投顾今日收益数据controller入参参数list={}", JSON.toJSONString(list));
        AdviserListRefreshDTO dto = JSON.parseObject(list, AdviserListRefreshDTO.class);
        log.info("转换后的dto={}", JSON.toJSONString(dto));
        return adviserRecommendService.listAdviserRefresh(dto);
    }

    /**
     * @param @param  adviserId
     * @param @param  pageBean
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getPosition
     * @Description: 查询投顾持仓列表
     */
    @ApiOperation(value = "投顾持仓实时数据", notes = "[李杰]投顾持仓实时数据")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "accessToken", value = "登录Token", paramType = "header", required = true),
            @ApiImplicitParam(dataType = "int", name = "adviserId", value = "投顾ID", paramType = "query", required = true),
            @ApiImplicitParam(dataType = "String", name = "codes", value = "股票代码，多个用,号给开", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = RealTimeAdviserVO.class)
    })
	@RequestMapping(value = "realtimeData", method = RequestMethod.GET)
	@AccessToken
	public Result getRealTimeAdviserPosition(@RequestParam Integer adviserId, String codes) {
		List<String> ids = new ArrayList<>();
		if (StringUtils.isNotBlank(codes)) {
			ids.addAll(Arrays.asList(codes.split(",")));
		}
		Result result = adviserPositionService.getRealTimeAdviserPosition(adviserId, ids);
		log.info("查询投顾持仓实时返回数据={}", JSON.toJSONString(result));
		return result;
	}

}
