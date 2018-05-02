package com.aq.controller.permission;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.base.BaseController;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.facade.dto.permission.GetMenuTreeDTO;
import com.aq.facade.dto.permission.RoleDTO;
import com.aq.facade.dto.permission.RoleMenuDTO;
import com.aq.facade.entity.permission.Role;
import com.aq.facade.enums.permission.PermissionEnum;
import com.aq.facade.service.permission.IRoleService;
import com.aq.facade.vo.permission.MenuTreeVO;
import com.aq.facade.vo.permission.SessionRoleVO;
import com.aq.facade.vo.permission.SessionUserVO;
import com.aq.facade.vo.permission.UserVO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 角色
 *
 * @author 郑朋
 * @create 2018/1/22
 */
@Slf4j
@Controller
@RequestMapping(value = "web/permission/role")
public class RoleController extends BaseController {


    @Reference(version = "1.0.0")
    IRoleService roleService;


    /**
     * 角色列表
     *
     * @param
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String toListRolePage() {
        return "permission/role";
    }


    /**
     * 跳转新增页面
     *
     * @param
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/addPage", method = RequestMethod.GET)
    public String toAddRolePage() {
        return "permission/role/add";
    }


    /**
     * 跳转修改页面
     *
     * @param id
     * @return org.springframework.web.servlet.ModelAndView
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/updatePage", method = RequestMethod.GET)
    public ModelAndView toUpdateRolePage(Integer id) {
        ModelAndView mv = new ModelAndView("permission/role/update");
        Result result = roleService.getRoleById(id);
        mv.addObject("role", result.getData());
        return mv;
    }


    /**
     * 跳转权限分配页面
     *
     * @param roleId
     * @param model
     * @return java.lang.String
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/permissionPage", method = RequestMethod.GET)
    public String toPermissionPage(Long roleId, Model model) {
        model.addAttribute("roleId", roleId);
        return "permission/role/roleMenu";
    }


    /**
     * 根据角色id查询分配权限页面的菜单树
     *
     * @param id
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/getRoleMenu", method = RequestMethod.POST)
    @ResponseBody
    public Result getRoleMenuByRoleId(Integer id) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(id);
        Result result = roleService.getRoleMenuAndMenuBtnByRoleId(roleDTO);
        return result;
    }


    /**
     * 修改分配权限页面角色菜单树
     *
     * @param getMenuTreeDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/updateRoleMenu", method = RequestMethod.POST)
    @ResponseBody
    public Result updateRoleMenu(@RequestBody GetMenuTreeDTO getMenuTreeDTO) {
        log.info("修改分配权限页面角色菜单树页面请求参数，getMenuTreeDTO={}", JSON.toJSONString(getMenuTreeDTO));
        //获取前端传来的json数据
        List<MenuTreeVO> menuTreeVOS = getMenuTreeDTO.getMenuTreeVOS();
        Integer roleId = getMenuTreeDTO.getRoleId();
        //组装数据
        List<Integer> listMenu = new ArrayList<>();
        Map<Integer, List<Integer>> menuButtons = new HashMap<>();
        if (menuTreeVOS != null && menuTreeVOS.size() != 0) {
            for (MenuTreeVO mtv : menuTreeVOS) {
                Integer menuId = mtv.getId();
                Integer btnId = mtv.getBtnId();
                Integer pId = mtv.getParentId();
                if (menuId != null && !"".equals(menuId)) {
                    listMenu.add(menuId);
                } else if (btnId != null && !"".equals(btnId)) {
                    if (menuButtons.containsKey(pId)) {
                        List<Integer> listBtn = menuButtons.get(pId);
                        listBtn.add(btnId);
                    } else {
                        List<Integer> listBtn = new ArrayList<>();
                        listBtn.add(btnId);
                        menuButtons.put(pId, listBtn);
                    }
                }
            }
        }
        //获取Session
        SessionUserVO sessionUserVO = getLoginUser();
        SessionRoleVO sessionRoleVO = sessionUserVO.getSessionRoleVo().get(0);

        //修改角色菜单
        RoleMenuDTO roleMenuDto = new RoleMenuDTO();
        roleMenuDto.setRoleId(roleId);
        roleMenuDto.setRoleCode(sessionRoleVO.getRoleCode());
        roleMenuDto.setMenuIds(listMenu);
        roleMenuDto.setCreaterId(sessionUserVO.getId());
        Result result = roleService.updateRoleMenuAndMenuBtn(roleMenuDto, menuButtons);
        if (result.isSuccess()) {
            //踢出主角色包含的用户
            forceLogoutUsersByRoleId(roleId);
        }
        return result;
    }


    /**
     * 分页获取角色列表
     *
     * @param page
     * @param role
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@ModelAttribute PageBean page, @ModelAttribute RoleDTO role) {
        return roleService.getRoleListByPage(page, role);
    }


    /**
     * 获取所有角色(管理员code)
     *
     * @param
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/list/all", method = RequestMethod.GET)
    @ResponseBody
    public Result listAll() {
        SessionUserVO sessionUserVO = getLoginUser();
        SessionRoleVO sessionRoleVO = sessionUserVO.getSessionRoleVo().get(0);
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setStatus(PermissionEnum.YES_USE.getCode());
        roleDTO.setRoleCode(sessionRoleVO.getRoleCode());
        return roleService.getRoleListByCondition(roleDTO);
    }

    /**
     * 新增角色
     *
     * @param role
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result addRole(@ModelAttribute Role role) {
        SessionUserVO sessionUserVO = getLoginUser();
        role.setCreaterId(sessionUserVO.getId());
        role.setUpdaterId(sessionUserVO.getId());
        role.setRoleCode(RoleCodeEnum.ADMIN.getCode());
        Result result = roleService.addRole(role);
        return result;
    }


    /**
     * 修改角色
     *
     * @param role
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/1/22
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Result updateRole(@ModelAttribute Role role) {
        SessionUserVO sessionUserVO = getLoginUser();
        role.setUpdaterId(sessionUserVO.getId());
        Result result = roleService.updateRole(role);
        return result;
    }


    private void forceLogoutUsersByRoleId(Integer roleId) {
        //处理session
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager) securityManager.getSessionManager();
        //获取当前已登录的用户session列表
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
        log.info("通过角色Id查询用户列表接口入参，roleId={}",roleId);
        List<UserVO> userVoList = roleService.getUsersByRoleId(roleId);
        for (UserVO u : userVoList) {
            for (Session session : sessions) {
                //清除该用户以前登录时保存的session
                if (u.getEmployeeID().equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                    sessionManager.getSessionDAO().delete(session);
                }
            }
        }

    }

}
