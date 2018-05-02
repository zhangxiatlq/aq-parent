package com.aq.service.impl.permission;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.facade.dto.permission.RoleDTO;
import com.aq.facade.dto.permission.RoleMenuDTO;
import com.aq.facade.entity.permission.MenuBtn;
import com.aq.facade.entity.permission.Role;
import com.aq.facade.enums.permission.PermissionEnum;
import com.aq.facade.exception.permission.PermissionBizException;
import com.aq.facade.exception.permission.PermissionExceptionEnum;
import com.aq.facade.service.permission.IRoleService;
import com.aq.facade.vo.permission.MenuTreeVO;
import com.aq.facade.vo.permission.MenuVO;
import com.aq.facade.vo.permission.RoleVO;
import com.aq.facade.vo.permission.UserVO;
import com.aq.mapper.permission.*;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * RoleServiceImpl
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Slf4j
@Service(version = "1.0.0")
public class RoleServiceImpl implements IRoleService {


    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private MenuBtnMapper menuBtnMapper;

    @Override
    public Result getRoleById(Integer roleId) {
        log.info("通过角色id查询角色信息入参, roleId={}", roleId);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            Role role = roleMapper.selectByPrimaryKey(roleId);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, role);
        } catch (Exception ex) {
            log.error("通过角色id查询角色信息异常，ex={}", ex);
        }
        log.info("通过角色id查询角色信息返回值, role={}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public List<RoleVO> getRoleByUserId(Integer userId) {
        log.info("根据用户Id查询角色集合入参, roleIds={}", userId);
        List<RoleVO> list = new ArrayList<>();
        try {
            list = roleMapper.getRoleListByUserId(userId);
        } catch (Exception ex) {
            log.error("根据用户Id查询角色集合异常，ex={}", ex);
        }
        log.info("根据用户Id查询角色集合返回值, list={}", JSON.toJSONString(list));
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addRole(Role role) {
        try {
            log.info("新增角色入参, role={}", JSON.toJSONString(role));
            role.setCreateTime(new Date());
            role.setUpdateTime(new Date());
            role.setStatus(PermissionEnum.YES_USE.getCode());
            role.setIsDefault(PermissionEnum.YES_DEFAULT.getCode());
            roleMapper.insert(role);
            return ResultUtil.getResult(RespCode.Code.SUCCESS);
        } catch (Exception ex) {
            log.error("新增角色异常，ex={}", ex);
            throw new PermissionBizException(PermissionExceptionEnum.ADD_ROLE_ERROR);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateRole(Role role) {
        try {
            log.info("修改角色入参, role={}", JSON.toJSONString(role));
            role.setUpdateTime(new Date());
            roleMapper.updateByPrimaryKeySelective(role);
            return ResultUtil.getResult(PermissionExceptionEnum.Code.SUCCESS);
        } catch (Exception ex) {
            log.error("修改角色异常，ex={}", ex);
            throw new PermissionBizException(PermissionExceptionEnum.UPDATE_ROLE_ERROR);
        }

    }

    @Override
    public Result getRoleListByPage(PageBean pageBean, RoleDTO roleDTO) {
        log.info("分页查询角色入参, pageBean={},roleDTO={}", JSON.toJSONString(pageBean), JSON.toJSONString(roleDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<RoleVO> list = roleMapper.getRoleByPage(roleDTO);
            PageInfo<RoleVO> pageInfo = new PageInfo<>(list);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
        } catch (Exception ex) {
            log.error("分页查询角色异常，ex={}", ex);
        }
        log.info("分页查询角色返回值, result={}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public Result getRoleListByCondition(RoleDTO roleDTO) {
        log.info("根据条件查询所有角色入参, roleDTO={}", JSON.toJSONString(roleDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            Role role = new Role();
            BeanUtils.copyProperties(roleDTO, role);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, roleMapper.select(role));
        } catch (Exception ex) {
            log.error("根据条件查询所有角色异常，ex={}", ex);
        }
        log.info("根据条件查询所有角色返回值, result={}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public Result getRoleMenuAndMenuBtnByRoleId(RoleDTO roleDTO) {
        log.info("通过角色id查询菜单入参, roleDTO={}", JSON.toJSONString(roleDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            //通过角色ID获取菜单和按钮
            List<MenuVO> list = menuMapper.getAllBaseMenuByRoleId(roleDTO.getId());
            List<MenuTreeVO> menuTreeVOList = paramsTrans(list, roleDTO.getId());
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, menuTreeVOList);
        } catch (Exception ex) {
            log.error("通过角色id查询菜单异常，ex={}", ex);
        }
        log.info("通过角色id查询菜单返回值, role={}", JSON.toJSONString(result));
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateRoleMenuAndMenuBtn(RoleMenuDTO roleMenuDTO, Map<Integer, List<Integer>> map) {
        try {
            //删除按钮
            List<Integer> menuIds = roleMenuMapper.selectRoleMenuByRoleId(roleMenuDTO.getRoleId());
            if (CollectionUtils.isNotEmpty(menuIds)) {
                menuBtnMapper.deleteBtnByMenuIdAndRoleId(menuIds, roleMenuDTO.getRoleId());
            }
            //删除菜单
            roleMenuMapper.deleteRoleMenuByRoleId(roleMenuDTO.getRoleId());
            //修改菜单
            if (CollectionUtils.isNotEmpty(roleMenuDTO.getMenuIds())) {
                roleMenuMapper.insertRoleMenu(roleMenuDTO.getRoleId(), roleMenuDTO.getMenuIds());
            }

            if (null != map) {
                //修改按钮
                Set<Integer> set = map.keySet();
                for (Integer key : set) {
                    if (CollectionUtils.isNotEmpty(map.get(key))) {
                        menuBtnMapper.insertMenuBtn(key, map.get(key), roleMenuDTO.getRoleId());
                    }
                }
            }

            return ResultUtil.getResult(PermissionExceptionEnum.Code.SUCCESS);
        } catch (Exception ex) {
            log.error("修改角色权限异常，ex={}", ex);
            throw new PermissionBizException(PermissionExceptionEnum.UPDATE_PERMISSION_ERROR);
        }
    }

    @Override
    public List<UserVO> getUsersByRoleId(Integer roleId) {

        log.info("通过角色Id查询用户列表入参, roleId={}", roleId);
        List<UserVO> list = new ArrayList<>();
        try {
            list = roleMapper.getUserListByRoleId(roleId);
        } catch (Exception ex) {
            log.error("通过角色Id查询用户列表异常，ex={}", ex);
        }
        log.info("通过角色Id查询用户列表返回值, list={}", JSON.toJSONString(list));
        return list;
    }


    /**
     * 判断角色拥有的菜单和按钮
     *
     * @param menuVos
     * @param roleId
     */
    private List<MenuTreeVO> paramsTrans(List<MenuVO> menuVos, Integer roleId) {
        List<MenuTreeVO> list = null;
        MenuTreeVO menuTreeVo;
        if (CollectionUtils.isNotEmpty(menuVos)) {
            //查询拥有的菜单权限
            List<Integer> listMenu = roleMenuMapper.selectRoleMenuByRoleId(roleId);
            Map<Integer, Integer> menuMap = new HashMap<>(16);
            for (Integer id : listMenu) {
                menuMap.put(id, id);
            }
            //查询拥有的按钮权限
            List<MenuBtn> listBtn = menuBtnMapper.selectMenuBtnByMenuIdAndRoleId(null, roleId);
            Map<String, String> btnMap = new HashMap<>(16);
            String key;
            for (MenuBtn btn : listBtn) {
                key = btn.getMenuId() + "-" + btn.getBtnId();
                btnMap.put(key, key);
            }
            list = new ArrayList<>();
            for (MenuVO menuVo : menuVos) {
                menuTreeVo = new MenuTreeVO();
                transVo(menuTreeVo, menuVo);
                //如果btnId为空 说明是菜单
                if (null == menuTreeVo.getBtnId()) {
                    menuTreeVo.setChecked(menuMap.containsKey(menuTreeVo.getId()));
                } else {
                    // 按钮
                    menuTreeVo.setChecked(btnMap.containsKey(menuTreeVo.getParentId() + "-" + menuTreeVo.getBtnId()));
                }
                list.add(menuTreeVo);
            }
        }
        return list;
    }

    private void transVo(MenuTreeVO menuTreeVo, MenuVO menuVo) {
        menuTreeVo.setId(menuVo.getId());
        menuTreeVo.setParentId(menuVo.getParentId());
        menuTreeVo.setName(menuVo.getMenuName());
        menuTreeVo.setBtnId(menuVo.getBtnId());
    }

}
