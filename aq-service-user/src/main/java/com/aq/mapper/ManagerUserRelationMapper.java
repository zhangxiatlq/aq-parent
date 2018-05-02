package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.UpdateClientDTO;
import com.aq.facade.entity.ManagerUserRelation;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：客户和客户经理关联关系表的实体mapper
 * @作者： 张霞
 * @创建时间： 18:25 2018/1/21
 * @Copyright @2017 by zhangxia
 */
public interface ManagerUserRelationMapper extends BaseMapper<ManagerUserRelation> {

    /**
     * @author: 张霞
     * @description：更新客户和客户之间的关联关系
     * @createDate: 11:28 2018/1/23
     * @param updateClientDTO
     * @return: int
     * @version: 2.1
     */
    int updateManagerUserRelation(UpdateClientDTO updateClientDTO);
}
