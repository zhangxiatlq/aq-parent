package com.aq.service.impl.permission;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.permission.IBtnService;
import com.aq.facade.vo.permission.BtnVO;
import com.aq.mapper.permission.BtnMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * BtnServiceImpl
 *
 * @author 郑朋
 * @create 2018/1/20
 */
@Service(version = "1.0.0")
@Slf4j
public class BtnServiceImpl implements IBtnService {


    @Autowired
    private BtnMapper btnMapper;


    @Override
    public List<BtnVO> getBtnListByMenuId(List<Integer> menuIds, Integer roleId) {
        List<BtnVO> list = new ArrayList<>();
        try {
            log.info("根据菜单id和角色id查询按钮入参, menuIds={}, roleId={}", JSON.toJSONString(menuIds), roleId);
            list = btnMapper.getBtnListByMenuIdsAndRoleId(menuIds, roleId);
        } catch (Exception e) {
            log.error("根据菜单id和角色id查询按钮异常, e={}", e);
        }
        log.info("根据菜单id和角色id查询按钮返回值, list={}", JSON.toJSONString(list));
        return list;
    }

    @Override
    public List<BtnVO> getBtnList() {
        List<BtnVO> list = new ArrayList<>();
        try {
            list = btnMapper.getAllBtn();
        } catch (Exception e) {
            log.error("获取所有的按钮异常, e={}", e);
        }
        log.info("获取所有的按钮返回值, list={}", JSON.toJSONString(list));
        return list;
    }
}
