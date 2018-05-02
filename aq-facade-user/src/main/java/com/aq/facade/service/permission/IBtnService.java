package com.aq.facade.service.permission;


import com.aq.facade.vo.permission.BtnVO;

import java.util.List;

/**
 * 按钮接口类
 *
 * @author 郑朋
 * @create 2018/1/20
 */
public interface IBtnService {

    /**
     * 根据菜单id获取按钮
     * @param menuIds
     * @param roleId
     * @return java.util.List<com.aq.facade.vo.permission.BtnVO>
     * @author 郑朋
     * @create 2018/1/20
     */
    List<BtnVO> getBtnListByMenuId(List<Integer> menuIds, Integer roleId);


    /**
     * 获取所有的按钮
     * @param
     * @return java.util.List<com.aq.facade.vo.permission.BtnVO>
     * @author 郑朋
     * @create 2018/1/20
     */
    List<BtnVO> getBtnList();

}
