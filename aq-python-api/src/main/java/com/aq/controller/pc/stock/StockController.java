package com.aq.controller.pc.stock;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.IToolCacheService;
import com.aq.facade.vo.StockInfoVO;
import com.aq.util.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: StockController
 * @Description: 股票相关接口
 * @author: lijie
 * @date: 2018年3月8日 下午3:07:07
 */
@Slf4j
@RestController
@RequestMapping("api/stocks")
@Api(value = "股票相关接口", description = "股票相关接口")
public class StockController {

	@Reference(version = "1.0.0", check = false)
    private IToolCacheService toolCacheService;
	
	/**
	 * 
	* @Title: toolPush  
	* @Description: 根据股票代码获取股票数据  
	* @param @param code
	* @param @return    参数  
	* @return Result    返回类型  
	* @throws
	 */
	@ApiOperation(value = "根据股票代码获取股票数据 ", notes = "[李杰]根据股票代码获取股票数据 ")
    @ApiResponses(value = {
        @ApiResponse(code = 10100, message = "请求参数有误"),
        @ApiResponse(code = 200, message = "操作成功",response = StockInfoVO.class)
    })
	@ApiImplicitParams({
		@ApiImplicitParam(dataType = "String", name = "code", value = "股票代码", paramType = "path", required = true) })
	@GetMapping(value = "info/{code}")
	public Result toolPush(@PathVariable String code) {
		log.info("根据股票代码获取股票数据入参={}", JSON.toJSONString(code));
		Result result = toolCacheService.getCacheStockInfo(code);
		log.info("根据股票代码获取股票返回数据result={}", JSON.toJSONString(result));
		return result;
	}
	
}
