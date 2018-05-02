package com.aq.controller.wechat.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aq.core.rediscache.ICacheService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: ManagerController
 * @Description: 客户经理
 * @author: lijie
 * @date: 2018年2月11日 下午5:47:08
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/wechat/managers")
@Api(value = "微信客户经理相关接口",description="微信客户经理相关接口")
public class WeChatManagerController {

	@Autowired
	private ICacheService iCacheService;
	
	@GetMapping("test")
	@SuppressWarnings("unchecked")
	public Object test(){
		iCacheService.hmSet("WG", "test", 1);
		iCacheService.hmSet("WG", "test", 2);
		iCacheService.hmSet("WG", "test1", 2);
		Object obj = iCacheService.keys("WG");
		return obj;
	}
}
