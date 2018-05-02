package com.aq.facade.service.permission;


import com.aq.facade.vo.permission.MenuVO;
import com.aq.util.result.Result;

import java.util.List;

/**
 * 菜单接口类
 *
 * @author 郑朋
 * @create 2018/1/20
 */
public interface IMenuService {


    /**
     * 根据用户id查询菜单列表树型结构
     *
     * @param userId
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result getMenuTreeByUserId(Integer userId);


    /**
     * 获取所有的菜单
     *
     * @param
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/20
     */
    Result getMenuList();


    /**
     * 根据角色获取权限
     *
     * @param roleIds
     * @return java.util.List<com.aq.facade.vo.permission.MenuVO>
     * @author 郑朋
     * @create 2018/1/20
     */
    List<MenuVO> getMenuByRoleIds(List<Integer> roleIds);

}
