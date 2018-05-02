package com.aq.mapper.customer;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.customer.ImportFailureRecord;
import com.aq.facade.vo.customer.ImportFailureRecordVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 导入客户失败 记录mapper
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Repository
public interface ImportFailureRecordMapper extends BaseMapper<ImportFailureRecord> {

    /**
     * 导入失败的客户记录列表
     *
     * @param managerId
     * @return java.util.List<com.aq.facade.vo.customer.ImportFailureRecordVO>
     * @author 郑朋
     * @create 2018/2/9 0009
     */
    List<ImportFailureRecordVO> selectRecordByList(Integer managerId);
}