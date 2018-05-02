package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.ToolBindRecord;
import com.aq.facade.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName: ToolBindRecordMapper
 * @Description: 工具绑定
 * @author: lijie
 * @date: 2018年1月20日 下午3:24:53
 */
public interface ToolBindRecordMapper extends BaseMapper<ToolBindRecord> {
    /**
     * @param customerId
     * @return
     * @Title: selectToolBindRecord
     * @author: lijie
     * @Description: 查询工具记录
     * @return: List<WeChatToolVO>
     */
    List<WeChatToolVO> selectToolBindRecord(@Param("customerId") Integer customerId);

    /**
     * @param @param  bindlist
     * @param @return 参数
     * @return List<ToolBindRecord>    返回类型
     * @throws
     * @Title: selectRecordByIds
     * @Description: 根据多个绑定记录ID查询绑定记录
     */
    List<ToolBindRecord> selectRecordByIds(List<Integer> bindlist);

    /**
     * 根据多个绑定记录ID 和 编码类型 查询绑定记录
     *
     * @param bindlist
     * @param categoryCode
     * @return java.util.List<com.aq.facade.vo.ToolBindInfoVO>
     * @author 郑朋
     * @create 2018/4/25
     */
    List<ToolBindInfoVO> selectToolBindInfoByCode(@Param("bindIdS") List<Integer> bindlist
            , @Param("categoryCode") String categoryCode);

    /**
     * @param @param  valueOf
     * @param @return 参数
     * @return ToolSetUpVO    返回类型
     * @throws
     * @Title: selectToolSetUpById
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    ToolSetUpVO selectToolSetUpById(Integer valueOf);

    /**
     * @param @param  categoryCode
     * @param @param  customerId
     * @param @return 参数
     * @return ToolSetUpVO    返回类型
     * @throws
     * @Title: selectToolSetUp
     * @Description: 查询设置数据
     */
    ToolSetUpVO selectToolSetUp(@Param("categoryCode") String categoryCode, @Param("customerId") Integer customerId);

    /**
     * @param @param  managerId
     * @param @return 参数
     * @return List<ToolBindVO>    返回类型
     * @throws
     * @Title: selectToolBind
     * @Description:根据客户经理ID查询推荐记录
     */
    List<ToolBindVO> selectToolBinds(@Param("managerId") Integer managerId, @Param("customerId") Integer customerId);

    /**
     * @param @param  bindId
     * @param @param  categoryCode
     * @param @return 参数
     * @return ToolBindInfoVO    返回类型
     * @throws
     * @Title: selectToolBindInfo
     * @Description: 工具推荐详情
     */
    ToolBindInfoVO selectToolBindInfo(@Param("bindId") Integer bindId, @Param("categoryCode") String categoryCode);

    /**
     * @param @param bindIds    参数
     * @return void    返回类型
     * @throws
     * @Title: updateToolIsSynchro
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    void updateToolIsSynchro(@Param("list") List<Integer> bindIds, @Param("isSynchro") Byte isSynchro);

    /**
     * @param @param binds
     * @param @param status    参数
     * @return int    返回类型
     * @throws
     * @Title: updateByIds
     * @Description: 修改状态
     */
    int updateByIds(@Param("list") List<Integer> list, @Param("status") Byte status);

    /**
     * @param @param  bindId
     * @param @return 参数
     * @return String    返回类型
     * @throws
     * @Title: selectCustomerOpenId
     * @Description: 根据绑定ID查询客户openID
     */
    ToolBindUserInfoVO selectCustomerOpenId(@Param("bindId") Integer bindId);

    /**
     * @param @param  id
     * @param @param  toolCategoryCode
     * @param @return 参数
     * @return ToolNumberVO    返回类型
     * @throws
     * @Title: selectToolNumber
     * @Description: 获取工具数量
     */
    List<Integer> selectToolNumber(@Param("customerId") Integer customerId, @Param("toolCategoryCode") String toolCategoryCode);

    /**
     * @param @param  list
     * @param @param  status
     * @param @return 参数
     * @return int    返回类型
     * @throws
     * @Title: upateSynchroDelete
     * @Description: 修改同步删除
     */
    void upateSynchroDelete(@Param("list") List<Integer> list, @Param("status") Byte status);

}
