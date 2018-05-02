package com.aq.controller.permission;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.base.BaseController;
import com.aq.facade.service.permission.IMenuService;
import com.aq.util.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * MenuController
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Slf4j
@Controller
@RequestMapping("web/permission/menu")
public class MenuController extends BaseController {

    @Reference(version = "1.0.0")
    IMenuService menuService;


    /**
     * 通过用户id获取菜单
     *
     * @param userId
     * @return java.lang.Object
     * @author 郑朋
     * @create 2018/1/20
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Object getMenuByUserId(@RequestParam(value = "userId") Integer userId) {
        Result result = menuService.getMenuTreeByUserId(userId);
        log.info("根据用户id查询菜单列表树型结构返回值，result={}", JSON.toJSONString(result));
        return result;
    }
}
