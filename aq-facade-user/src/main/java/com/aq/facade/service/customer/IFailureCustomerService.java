package com.aq.facade.service.customer;

import com.aq.facade.dto.customer.DeleteImportDTO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

import java.util.List;

/**
 * 导入失败的客户记录
 *
 * @author 郑朋
 * @create 2018/2/9 0009
 **/
public interface IFailureCustomerService {

    /**
     * 导入失败的客户记录列表（分页）
     *
     * @param pageBean
     * @param managerId
     * @return com.aq.util.result.Result {@link com.aq.facade.vo.customer.ImportFailureRecordVO}
     * @author 郑朋
     * @create 2018/2/8 0008
     */
    Result getFailureCustomerByPage(PageBean pageBean, Integer managerId);

    /**
     * 导入失败的客户记录列表
     *
     * @param managerId
     * @return com.aq.util.result.Result {@link com.aq.facade.vo.customer.ImportFailureRecordVO}
     * @author 郑朋
     * @create 2018/2/8 0008
     */
    Result getFailureCustomerByList(Integer managerId);

    /**
     * 批量删除导入失败的客户记录
     *
     * @param deleteImportDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/8 0008
     */
    Result deleteFailureCustomer(DeleteImportDTO deleteImportDTO);
}
