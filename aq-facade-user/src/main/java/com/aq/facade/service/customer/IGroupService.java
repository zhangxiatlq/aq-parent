package com.aq.facade.service.customer;

import com.aq.facade.dto.customer.AddOrUpdateGroupDTO;
import com.aq.facade.dto.customer.DeleteGroupDTO;
import com.aq.facade.dto.customer.QueryGroupDTO;
import com.aq.util.result.Result;

/**
 * 客户分组 service
 *
 * @author 郑朋
 * @create 2018/2/8
 **/
public interface IGroupService {

    /**
     * 根据条件查询分组
     *
     * @param queryGroupDTO
     * @return com.aq.util.result.Result{@link com.aq.facade.vo.customer.QueryGroupVO}
     * @author 郑朋
     * @create 2018/2/8
     */
    Result getGroupList(QueryGroupDTO queryGroupDTO);

    /**
     * 新增分组
     *
     * @param addOrUpdateGroupDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/8
     */
    Result addGroup(AddOrUpdateGroupDTO addOrUpdateGroupDTO);

    /**
     * 修改分组
     *
     * @param addOrUpdateGroupDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/8
     */
    Result updateGroup(AddOrUpdateGroupDTO addOrUpdateGroupDTO);


    /**
     * 删除分组
     *
     * @param deleteGroupDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/8
     */
    Result deleteGroup(DeleteGroupDTO deleteGroupDTO);
}
