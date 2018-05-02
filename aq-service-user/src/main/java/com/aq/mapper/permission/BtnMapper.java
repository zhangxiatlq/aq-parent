package com.aq.mapper.permission;


import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.permission.Btn;
import com.aq.facade.vo.permission.BtnVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * BtnMapper
 *
 * @author 郑朋
 * @createTime 2018/1/20
 */
@Repository
public interface BtnMapper extends BaseMapper<Btn> {

    /**
     * 根据角色Id和菜单id获取已分配（已选择）按钮
     *
     * @param menuIds
     * @param roleId
     * @return java.util.List<com.aq.facade.vo.permission.MenuVO>
     * @author 郑朋
     * @create 2017/5/13
     */
    List<BtnVO> getBtnListByMenuIdsAndRoleId(@Param("menuIds") List<Integer> menuIds,
                                             @Param("roleId") Integer roleId);

    /**
     * 通过菜单id查询该菜单的基础按钮
     *
     * @param menuId
     * @return java.util.List<com.aq.facade.vo.permission.BtnVO>
     * @author 郑朋
     * @create 2017/5/12
     */
    List<BtnVO> getAllBaseBtnByMenuId(@Param("menuId") Integer menuId);

    /**
     * 查询所有按钮
     *
     * @param
     * @return java.util.List<com.aq.facade.vo.permission.BtnVO>
     * @author 郑朋
     * @create 2017/5/13
     */
    List<BtnVO> getAllBtn();

}
