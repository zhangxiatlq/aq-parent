package com.aq.controller.pc.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.system.IDeclarationService;
import com.aq.util.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Declaration controller
 *
 * @author 郑朋
 * @create 2018/2/9 0009
 **/

@RestController
@RequestMapping("/api/pc/declaration")
@Api(value = "机构宣言接口", description = "机构宣言接口")
@Slf4j
public class DeclarationController {

    @Reference(version = "1.0.0", check = false)
    private IDeclarationService declarationService;

    @ApiOperation(value = "机构宣言接口", notes = "[郑朋]查询机构宣言")
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功")
    })
    @RequestMapping(method = RequestMethod.GET)
    public Result getDeclaration() {
        Result result = declarationService.getDeclaration();
        log.info("查询机构宣言返回值：result={}", JSON.toJSONString(result));
        return result;
    }
}
