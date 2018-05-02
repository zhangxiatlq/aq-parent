package com.aq.controller.wechat;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aq.core.config.properties.WeChatCoreProperties;
import com.aq.core.wechat.WeChatConfig;
import com.aq.core.wechat.authorize.AuthorizeHandler;
import com.aq.core.wechat.constant.TagEnum;
import com.aq.core.wechat.menu.WeChatMenuComponent;
import com.aq.core.wechat.response.QrCodeResponse;
import com.aq.core.wechat.tags.WeChatTagsUtil;
import com.aq.core.wechat.util.QrCodeUtil;
import com.aq.core.wechat.util.WeChatSignatureUtil;
import com.aq.facade.dto.WeChatBindDTO;
import com.aq.facade.service.IWeChatAdviserPushService;
import com.aq.facade.service.IWechatStrategyService;
import com.aq.facade.service.customer.ICustomerService;
import com.aq.service.IWeChatService;
import com.aq.util.http.HttpClientUtils;
import com.aq.util.qrcode.QRCodeUtil;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @ClassName: WeChatComtroller
 * @Description: 微信入口
 * @author: lijie
 * @date: 2018年1月20日 下午2:18:50
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/wechat")
@Api(value = "微信相关接口", description = "微信相关接口")
public class WeChatController {

    @Reference(version = "1.0.0", retries = 0, check = false)
    private IWechatStrategyService iWechatStrategyService;
    /**
     * 客户信息
     */
    @Reference(version = "1.0.0", check = false)
    private ICustomerService customerService;
    /**
     * 微信服务
     */
    @Autowired
    private IWeChatService weChatService;
    /**
     * 微信授权
     */
    @Autowired
    private AuthorizeHandler authorize;

    @Autowired
    private WeChatCoreProperties weChatProperties;

    @Reference(version = "1.0.0")
    private IWeChatAdviserPushService weChatAdviserPushService;

    @Autowired
    private WeChatMenuComponent menuComponent;

    /**
     * @Title: getSignature
     * @author: lijie
     * @Description: 获取签名
     * @return: Result
     */
    @ApiOperation(value = "获取jsapi签名", notes = "[李杰]获取jsapi签名")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "sign", method = RequestMethod.GET)
    public Result getSignature() {
        return ResultUtil.getResult(RespCode.Code.SUCCESS, WeChatSignatureUtil.getSignature());
    }

    /**
     * @Title: 绑定菜单
     * @author: 熊克文
     * @Description: 绑定菜单
     * @return: Result
     */
    @ApiOperation(value = "绑定菜单", notes = "[熊克文]绑定菜单")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "openIds", value = "openIds “,”隔开", paramType = "path", required = true),
    })
    @RequestMapping(value = "batchTagging/{openIds}", method = RequestMethod.GET)
    public Result batchTagging(@PathVariable(name = "openIds") String openIds) throws Exception {
        log.info("微信绑定菜单开始:{}", openIds);
        return WeChatTagsUtil.batchTagging(TagEnum.FOLLOW_CUSTOMER_TAG.getTagId(), Arrays.asList(openIds.split(",")));
    }

    /**
     * @param @param  account
     * @param @param  openId
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: bindOpenId
     * @Description: 绑定openID
     */
    @ApiOperation(value = "绑定openID", notes = "[李杰]绑定openID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "bind", method = RequestMethod.POST)
    public Result bindOpenId(WeChatBindDTO dto) {

        return weChatService.bindOpenId(dto);
    }

    /**
     * @param @return 参数
     * @return Result    返回类型
     * @throws Exception
     * @throws
     * @Title: getAccessToken
     * @Description: 获取微信acctoken
     */
    @ApiOperation(value = "获取微信acctoken", notes = "[李杰]获取微信acctoken")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(value = "accesstoken", method = RequestMethod.GET)
    public Result getAccessToken() throws Exception {

        return ResultUtil.getResult(RespCode.Code.SUCCESS, WeChatSignatureUtil.getAccessToken());
    }

    /**
     * @param @param response
     * @param @param request    参数
     * @return void    返回类型
     * @throws IOException
     * @throws
     * @Title: wechatWebAuthorize
     * @Description:微信网页授权
     */
    @ApiOperation(value = "微信网页授权", notes = "[李杰]微信网页授权")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "route", value = "跳转路径", paramType = "query", required = true)
    })
    @RequestMapping(value = "authorize", method = RequestMethod.GET)
    public void wechatWebAuthorize(HttpServletResponse response, @RequestParam String route) throws IOException {
        JSONObject json = new JSONObject();
        json.put("route", route);
        authorize.webAuthorize(response, weChatProperties.getAuthorize(), json);
    }

    /**
     * @return void
     * @throws IOException
     * @throws
     * @Title: jumpWeChatBind
     * @Description: 跳转微信授权绑定（针对之前的接口、后续待定）
     * @param: @param request
     * @param: @param response
     * @author lijie
     */
    @RequestMapping(value = "jump/bind", method = RequestMethod.GET)
    public void jumpWeChatBind(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String account = request.getParameter("account");
        String roleCode = request.getParameter("roleCode");
        StringBuilder sbu = new StringBuilder();
        sbu.append(account).append(",").append(roleCode);
        response.sendRedirect("/api/wechat/authorize?route=" + sbu.toString());
    }

    /**
     * @param @return 参数
     * @return Result    返回类型
     * @throws Exception
     * @throws
     * @Title: synOpenIdBind
     * @Description: 同步微信绑定openID
     */
    @ApiOperation(value = "同步微信绑定openID", notes = "[李杰]同步微信绑定openID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功")
    })
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "synOpenIdBind", method = RequestMethod.GET)
    public Result synOpenIdBind() throws Exception {
        List<String> openIds = new ArrayList<>();
        Result result = customerService.getAllOpend();
        if (result.isSuccess()) {
            openIds.addAll((List<String>) result.getData());
        }
        Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
        int num = 1;
        List<String> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("access_token", WeChatSignatureUtil.getAccessToken());
        params.put("lang", "zh_CN");
        List<String> subscribe0 = new ArrayList<>();
        for (String str : openIds) {
            // 校验是否关注公众号
            params.put("openid", str);
            String content = HttpClientUtils.sendGet(WeChatConfig.GET_UEER_INGFO, params).getResponseContent();
            log.info("校验是否关注客户返回值={}", content);
            if (StringUtils.isNotBlank(content)) {
                JSONObject josn = JSONObject.parseObject(content);
                if ("0".equals(josn.getString("subscribe"))) {
                    //  加入为关注客户
                    subscribe0.add(str);
                    continue;
                }
            }
            if (num % 49 == 0) {
                list = new ArrayList<>();
                map.put(num, list);
            }
            list.add(str);
            num++;
        }
        log.info("未关注客户数据信息={}", JSON.toJSONString(subscribe0));
        map.put(num, list);
        for (Map.Entry<Integer, List<String>> m : map.entrySet()) {
            Result mresult = WeChatTagsUtil.batchTagging(TagEnum.FOLLOW_CUSTOMER_TAG.getTagId(), m.getValue());
            log.info("同步绑定菜单返回数据={}", JSON.toJSONString(mresult));
        }
        return ResultUtil.getResult(RespCode.Code.SUCCESS);
    }

    @ApiOperation(value = "策略微信推送消息", notes = "[熊克文]微信推送消息")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "templateId", value = "模板ids", paramType = "path", required = true),
            @ApiImplicitParam(dataType = "String", name = "pushIds", value = "推送信息ids", paramType = "path", required = true)
    })
    @RequestMapping(value = "wechantTemplete/{templateId}-{pushIds}", method = RequestMethod.POST)
    public Result pushWechantTemplete(
            @PathVariable(value = "templateId") String templateId,
            @PathVariable(value = "pushIds") String pushIds
    ) {
        log.info("策略微信推送消息templateId={},pushIds={} ", templateId, pushIds);
        Result result = null;
        try {
            result = this.iWechatStrategyService.pushWechantTemplete(pushIds, templateId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("推送微信模板异常");
        }
        log.info("策略微信推送消息 result={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "投顾微信推送", notes = "[郑朋]投顾微信推送")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "pushId", value = "委托记录id", paramType = "query", required = true),
            @ApiImplicitParam(dataType = "String", name = "totalAssets", value = "总资产", paramType = "query", required = true)
    })
    @RequestMapping(value = "adviser/push", method = RequestMethod.POST)
    public Result push(String pushId, String totalAssets) {
        log.info("投顾微信推送参数，pushId={}，totalAssets={}", pushId, totalAssets);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            result = weChatAdviserPushService.pushWeChatTemplate(pushId, totalAssets);
        } catch (Exception e) {
            log.error("投顾微信推送异常, e={}", e);
        }
        log.info("投顾微信推送, result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @return void
     * @throws
     * @Title: qrCode
     * @Description: 生成微信绑定客户/客户经理二维码
     * @param: @param telphone
     * @param: @param role
     * @param: @param request
     * @param: @param response
     * @param: @throws IOException
     * @author lijie
     */
    @ApiOperation(value = "生成微信绑定客户/客户经理二维码 ", notes = "[李杰]生成微信绑定客户/客户经理二维码 ")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "telphone", value = "手机号", paramType = "path", required = true),
            @ApiImplicitParam(dataType = "int", name = "role", value = "角色：2、客户 3、客户经理", paramType = "path", required = true)
    })
    @GetMapping("qrCode/{telphone}/{role}")
    public void qrCode(@PathVariable String telphone, @PathVariable Byte role, HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        QrCodeResponse result = QrCodeUtil.getQrCodeTicket(telphone, role);
        if (null != result && StringUtils.isNotBlank(result.getUrl())) {
            QRCodeUtil.zxingCodeCreate(request, response, result.getUrl(), 300, 300);
        }
    }

    /**
     * @return void
     * @throws Exception
     * @throws
     * @Title: createMenu
     * @Description: 创建菜单
     * @param:
     * @author lijie
     */
    @ApiOperation(value = "创建菜单 ", notes = "[李杰]创建菜单")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @PostMapping("menu")
    public Result createMenu() {
        Result result = menuComponent.createMenu();
        log.info("创建菜单返回数据={}", JSON.toJSONString(result));
        return result;
    }
    /**
     *
     * @Title: createConditionalMenu
     * @Description: 创建个性化菜单
     * @param: @return
     * @return Result
     * @author lijie
     * @throws
     */
	/*@ApiOperation(value = "创建个性化菜单", notes = "[李杰]创建个性化菜单")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
	@PostMapping("menu/conditional")
	public Result createConditionalMenu() {
		Result result = menuComponent.createConditionalMenu();
		log.info("创建个性化菜单返回数据={}", JSON.toJSONString(result));
		return result;
	}*/

    /**
     * @return Result
     * @throws
     * @Title: delMenu
     * @Description: 删除菜单
     * @param: @return
     * @author lijie
     */
    @ApiOperation(value = "删除菜单", notes = "[李杰]删除菜单")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @DeleteMapping("menu")
    public Result delMenu() {
        Result result = menuComponent.delMenu();
        log.info("删除菜单返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @return Result
     * @throws
     * @Title: delConditionalMenu
     * @Description: 删除个性化菜单
     * @param: @param menuId
     * @param: @return
     * @author lijie
     */
    @ApiOperation(value = "删除个性化菜单", notes = "[李杰]删除个性化菜单")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "menuId", value = "菜单ID", paramType = "path", required = true)
    })
    @DeleteMapping("menu/conditional/{menuId}")
    public Result delConditionalMenu(@PathVariable String menuId) {
        Result result = menuComponent.deleteConditionalMenu(menuId);
        log.info("删除个性化菜单返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @return Result
     * @throws
     * @Title: getMenus
     * @Description: 查询菜单数据
     * @param: @return
     * @author lijie
     */
    @ApiOperation(value = "查询菜单数据", notes = "[李杰]查询菜单数据")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @GetMapping("menus")
    public Result getMenus() {
        Result result = menuComponent.getMenus();
        log.info("查询创建的菜单返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @return Result
     * @throws
     * @Title: getTags
     * @Description: 查询公众号已创建的标签
     * @param: @return
     * @author lijie
     */
    @ApiOperation(value = "查询公众号已创建的标签 ", notes = "[李杰]查询公众号已创建的标签 ")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @GetMapping("tags")
    public Result getTags() {
        Result result = menuComponent.getTags();
        log.info("查询公众号已创建的标签返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @return Result
     * @throws Exception
     * @throws
     * @Title: createMaterial
     * @Description: 创建素材
     * @param: @return
     * @author lijie
     */
    @ApiOperation(value = "创建素材 ", notes = "[李杰]创建素材")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @PostMapping("createMaterial")
    public Result createMaterial() throws Exception {
        Result result = menuComponent.createMaterial();
        log.info("创建素材返回数据={}", JSON.toJSONString(result));
        return result;
    }

    @ApiOperation(value = "上传素材图片", notes = "[李杰]上传素材图片")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @PostMapping("uploadMaterials")
    public Result uploadMaterials() {
        Result result = menuComponent.uploadMaterials();
        log.info("上传素材图片返回数据={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * @return Result
     * @throws
     * @Title: createTag
     * @Description: 为公众号创建标签
     * @param: @param tagName
     * @param: @return
     * @author lijie
     */
    @ApiOperation(value = "为公众号创建标签", notes = "[李杰]为公众号创建标签")
    @ApiResponses(value = {
            @ApiResponse(code = 300, message = "操作失败"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "tagName", value = "标签名称", paramType = "form", required = true)
    })
    @PostMapping("tag")
    public Result createTag(@RequestParam String tagName) {

        return menuComponent.createTag(tagName);
    }

}
