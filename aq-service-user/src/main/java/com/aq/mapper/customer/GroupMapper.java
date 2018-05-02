package com.aq.mapper.customer;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.customer.QueryGroupDTO;
import com.aq.facade.entity.customer.Group;
import com.aq.facade.vo.customer.QueryGroupVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 客户分组mapper
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Repository
public interface GroupMapper extends BaseMapper<Group> {
    /**
     * 根据条件查询分组
     *
     * @param queryGroupDTO
     * @return java.util.List<com.aq.facade.vo.customer.QueryCustomerVO>
     * @author 郑朋
     * @create 2018/2/8
     */
    List<QueryGroupVO> selectGroupList(QueryGroupDTO queryGroupDTO);

}