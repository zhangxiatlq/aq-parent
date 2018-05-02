package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.ConsultQueryDTO;
import com.aq.facade.entity.Consult;
import com.aq.facade.vo.ConsultManagerVO;
import com.aq.facade.vo.ConsultQueryVO;
import com.aq.facade.vo.ConsultWeChatVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 今日汇 mapper
 *
 * @author 郑朋
 * @create 2018/2/28
 */
@Repository
public interface ConsultMapper extends BaseMapper<Consult> {

    /**
     * 根据条件查询今日汇
     *
     * @param consultQueryDTO
     * @return java.util.List<com.aq.facade.vo.ConsultQueryVO>
     * @author 郑朋
     * @create 2018/2/28 0028
     */
    List<ConsultQueryVO> selectConsultList(ConsultQueryDTO consultQueryDTO);

    /**
     * 微信--今日汇列表
     *
     * @param customerId
     * @return java.util.List<com.aq.facade.vo.ConsultManagerVO>
     * @author 郑朋
     * @create 2018/3/12
     */
    ConsultManagerVO selectAdviserWeChatList(Integer customerId);

    /**
     * 根据条件查询今日汇
     *
     * @param managerId
     * @return java.util.List<com.aq.facade.vo.ConsultQueryVO>
     * @author 郑朋
     * @create 2018/2/28 0028
     */
    List<ConsultWeChatVO> selectConsultWeChatList(Integer managerId);
}