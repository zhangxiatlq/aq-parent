package com.aq.controller.pc.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.system.IBannerService;
import com.aq.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Banner controller
 *
 * @author 郑朋
 * @create 2018/2/9 0009
 **/

@RestController
@RequestMapping("/api/pc/banners")
@Api(value = "Banner图接口", description = "Banner图接口")
@Slf4j
public class BannerController {

    @Reference(version = "1.0.0", check = false)
    private IBannerService bannerService;

    @ApiOperation(value = "Banner图列表", notes = "[郑朋]查询Banner图列表")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = List.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    public Result getBanner() {
        Result result = bannerService.getBanner();
        log.info("查询Banner图列表返回值：result={}", JSON.toJSONString(result));
        return result;
    }
}
