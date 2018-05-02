package com.aq.config.shiro;


import com.aq.facade.vo.permission.BtnVO;
import com.aq.facade.vo.permission.MenuVO;
import com.aq.facade.vo.permission.RoleVO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 转换工具
 *
 * @author 郑朋
 * @create 2018/1/19
 **/
public class ConvertRoleListUtil {

    /**
     * 将角色List转换成Set<String>
     *
     * @param roles
     * @return
     */
    public static Set<String> ConvertRoleListToSet(List<RoleVO> roles) {
        Set<String> set = new HashSet<>();
        for (RoleVO role : roles) {
            set.add(role.getRoleCode().toString());
        }
        return set;
    }

    /**
     * 将角色权限List转换成List<String>
     *
     * @param permissions
     */
    public static List<String> ConvertPermissionListToStringList(List<MenuVO> permissions) {
        List<String> list = new ArrayList<>();
        //---这里用的菜单表，后面要改成菜单权限表和按钮权限表
        for (MenuVO permission : permissions) {

            String permissionName = permission.getMenuUrl();

            if (permissionName != null && !"".equals(permissionName.trim())) {
                list.add(permissionName);
            }
        }
        return list;
    }


    /**
     * 将按钮权限List转换成List<String>
     *
     * @param permissions
     * @return java.util.List<java.lang.String>
     * @author 郑朋
     * @create 2018/1/20
     */
    public static List<String> ConvertBtnPermissionListToStringList(List<BtnVO> permissions) {
        List<String> list = new ArrayList<String>();
        //---这里用的按钮表
        for (BtnVO permission : permissions) {
            String permissionName = permission.getBtnCode();
            if (permissionName != null && !"".equals(permissionName.trim())) {
                list.add(permissionName);
            }
        }
        return list;
    }
}
