package com.aq.facade.service;

import com.aq.facade.dto.ConsultAddDTO;
import com.aq.facade.dto.ConsultQueryDTO;
import com.aq.facade.dto.ConsultRemoveDTO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * 今日汇
 *
 * @author 郑朋
 * @create 2018/2/28
 **/
public interface IConsultService {

    /**
     * 新增今日汇
     *
     * @param consultAddDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/28 0028
     */
    Result addConsult(ConsultAddDTO consultAddDTO);

    /**
     * 分页查询今日汇列表
     *
     * @param pageBean
     * @param consultQueryDTO
     * @return com.aq.util.result.Result {@link com.aq.facade.vo.ConsultQueryVO}
     * @author 郑朋
     * @create 2018/2/28 0028
     */
    Result getConsultPage(PageBean pageBean, ConsultQueryDTO consultQueryDTO);

    /**
     * 删除今日汇
     *
     * @param consultRemoveDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/28
     */
    Result removeConsult(ConsultRemoveDTO consultRemoveDTO);

    /**
     * 微信--今日汇列表（分页）
     *
     * @param pageBean
     * @param customerId
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/12
     */
    Result getWeChatConsult(PageBean pageBean, Integer customerId);

    /**
     * 微信--通过今日汇id 获取详情
     *
     * @param consultId
     * @param customerId
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/3/26
     */
    Result getWeChatConsultById(Integer consultId, Integer customerId);
}
