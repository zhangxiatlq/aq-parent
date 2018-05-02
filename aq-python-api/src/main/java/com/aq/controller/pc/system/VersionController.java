package com.aq.controller.pc.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aq.facade.service.system.IVersionService;
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
@RequestMapping("/api/pc/version")
@Api(value = "版本号接口", description = "版本号接口")
@Slf4j
public class VersionController {

    @Reference(version = "1.0.0", check = false)
    private IVersionService iVersionService;

    @ApiOperation(value = "获取最新版本号", notes = "[熊克文]获取最新版本号")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = List.class)
    })
    @RequestMapping(method = RequestMethod.GET)
    public Result getRecentVersionCode() {
        return this.iVersionService.getRecentVersionCode();
    }
}
