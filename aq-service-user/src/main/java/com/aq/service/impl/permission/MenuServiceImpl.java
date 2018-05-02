package com.aq.service.impl.permission;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.permission.IMenuService;
import com.aq.facade.vo.permission.MenuVO;
import com.aq.facade.vo.permission.RoleVO;
import com.aq.mapper.permission.MenuMapper;
import com.aq.mapper.permission.RoleMapper;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * MenuServiceImpl
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Slf4j
@Service(version = "1.0.0")
public class MenuServiceImpl implements IMenuService {


    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Result getMenuTreeByUserId(Integer userId) {
        log.info("根据用户id查询菜单列表树型结构入参, userId={}", userId);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            List<RoleVO> list = roleMapper.getRoleListByUserId(userId);
            if (CollectionUtils.isEmpty(list)) {
                return result;
            }
            List<Integer> role = new ArrayList<>();
            for (RoleVO r : list) {
                role.add(r.getId());
            }
            List<MenuVO> menuVos = menuMapper.getMenuByRoleIds(role);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, getMenuTree(menuVos));
        } catch (Exception ex) {
            log.error("通过用户id查询菜单异常，ex={}", ex);
        }
        log.info("根据用户id查询菜单列表树型结构返回值, result={}", JSON.toJSONString(result));
        return result;
    }


    @Override
    public Result getMenuList() {
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, menuMapper.getAllMenu());
        } catch (Exception ex) {
            log.error("获取所有的菜单异常，ex={}", ex);
        }
        log.info("获取所有的菜单返回值, result={}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public List<MenuVO> getMenuByRoleIds(List<Integer> roleIds) {
        log.info("根据角色获取权限入参, roleIds={}", JSON.toJSONString(roleIds));
        List<MenuVO> list = new ArrayList<>();
        try {
            list = menuMapper.getMenuByRoleIds(roleIds);
        } catch (Exception ex) {
            log.error("根据角色获取权限异常，ex={}", ex);
        }
        log.info("根据角色获取权限返回值, list={}", JSON.toJSONString(list));
        return list;
    }


//===============================================私有通用方法================================================/

    /**
     * 获取菜单树
     *
     * @param menuVos
     * @return
     */
    public static List<MenuVO> getMenuTree(List<MenuVO> menuVos) {
        List<MenuVO> treeList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(menuVos)) {
            for (MenuVO vo : menuVos) {
                if (null != vo.getParentId() && Integer.valueOf(0).equals(vo.getParentId())) {
                    vo = getChildMenu(vo, menuVos);
                    treeList.add(vo);
                }
            }
        }
        return treeList;
    }

    private static MenuVO getChildMenu(MenuVO parent, List<MenuVO> menuVos) {
        List<MenuVO> child = null;
        for (MenuVO vo : menuVos) {
            if (parent.getId().equals(vo.getParentId())) {
                vo = getChildMenu(vo, menuVos);
                if (child == null) {
                    child = new ArrayList<>();
                }
                child.add(vo);
            }
        }
        parent.setChild(child);
        return parent;
    }


}
