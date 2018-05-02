package com.aq.facade.service;

import com.aq.facade.dto.*;
import com.aq.facade.dto.add.AddGridToolDTO;
import com.aq.facade.dto.add.AddSellingToolDTO;
import com.aq.facade.dto.add.AddTrendToolDTO;
import com.aq.facade.dto.update.UpdateGridToolDTO;
import com.aq.facade.dto.update.UpdateSellingToolDTO;
import com.aq.facade.dto.update.UpdateTrendToolDTO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

import java.text.ParseException;
import java.util.List;

/**
 * @author lijie
 * @ClassName: IToolService
 * @Description: 工具相关接口
 * @date 2018年1月19日 下午5:55:00
 */
public interface IToolService {
    /**
     * @param @param  request
     * @param @return 设定文件
     * @return Result    返回类型
     * @throws
     * @Title: applyTool
     * @Description: 申请工具
     * @author lijie
     */
    Result applyTool(ApplyDTO request);

    /**
     * @param userId
     * @return
     * @Title: getToolsByPage
     * @author: lijie
     * @Description: 分页获取工具数据
     * @return: Result
     */
    Result getToolsByPage(Integer userId, PageBean pageBean);

    /**
     * @param datas
     * @param toolCode
     * @param createrId
     * @Title: addGridTools
     * @author: lijie
     * @Description: 添加网格工具
     * @return: Result
     */
    Result addGridTools(AddGridToolDTO dto);

    /**
     * @param dto
     * @param updaterId
     * @Title: updateGridTool
     * @author: lijie
     * @Description: 修改网格信息
     * @return: Result
     */
    Result updateGridTool(UpdateGridToolDTO dto);

    /**
     * @param datas
     * @param toolCode
     * @param createrId
     * @Title: addSellingTools
     * @author: lijie
     * @Description: 添加卖点工具
     * @return: Result
     */
    Result addSellingTools(AddSellingToolDTO dto);

    /**
     * @param dto
     * @param updaterId
     * @Title: updateSellingTool
     * @author: lijie
     * @Description: 修改卖点工具
     * @return: Result
     */
    Result updateSellingTool(UpdateSellingToolDTO dto);

    /**
     * @param datas
     * @param toolCode
     * @param createrId
     * @Title: addTrendTool
     * @author: lijie
     * @Description: 添加趋势化工具
     * @return: Result
     */
    Result addTrendTool(AddTrendToolDTO dto);

    /**
     * @param dto
     * @param updaterId
     * @Title: updateTrendTool
     * @author: lijie
     * @Description: 修改趋势化
     * @return: Result
     */
    Result updateTrendTool(UpdateTrendToolDTO dto);

    /**
     * @param dto
     * @Title: bingTool
     * @author: lijie
     * @Description: 推送工具（绑定工具）
     * @return: Result
     */
    Result bingTool(BingToolDTO dto);

    /**
     * @param @param  toolCode
     * @param @param  toolIds
     * @param @param  toolBindIds
     * @param @param  operId
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: deleteTool
     * @Description: 删除工具/推荐绑定的工具
     */
    Result deleteTool(String toolCode, List<Integer> toolIds, List<Integer> toolBindIds, Integer operId);

    /**
     * @param userId
     * @param pageBean
     * @param toolCode
     * @Title: getPcToolsByPage
     * @author: lijie
     * @Description:查询工具信息
     * @return: Result
     */
    Result getPcToolsByPage(QueryToolDTO dto, PageBean pageBean);

    /**
     * @param @param  managerId
     * @param @param  pageBean
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getBindTools
     * @Description: 根据客户经理查询推荐的 工具列表数据
     */
    Result getBindTools(Integer managerId, Integer customerId, PageBean pageBean);

    /**
     * @param @param  bindId
     * @param @param  categoryCode
     * @param @param  pageBean
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getBindToolInfo
     * @Description:根据ID查询绑定详情
     */
    Result getBindToolInfo(Integer bindId, String categoryCode, PageBean pageBean);

    /**
     * @param @param  bindId
     * @param @param  pageBean
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getPushsByPage
     * @Description: 单个工具推送列表
     */
    Result getPushsByPage(Integer bindId, PageBean pageBean);

    /**
     * @param @param  dto
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: toolPush
     * @Description: 工具推送
     */
    Result toolPush(ToolPushDTO dto);

    /**
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: timingUpdateToolSynchro
     * @Description: 定时修改工具同步数据
     */
    Result timingUpdateToolSynchro();

    /**
     * @param @param  stockCode
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getStockInfo
     * @Description: 根据股票代码获取股票信息
     */
    Result getStockInfo(String stockCode);

    /**
     * @param @param  id
     * @param @param  toolCategoryCode
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getApplyToolSurplusNum
     * @Description: 获取剩余工具数量
     */
    Result getApplyToolSurplusNum(Integer id, String toolCategoryCode);

    /**
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: synchroTrendToolToRedis
     * @Description: 同步绑定的趋势量化工具到redis
     */
    Result synchroTrendToolToRedis();

    /**
     * @auth: zhangxia
     * @desc:定期计算工具收益率
     * @methodName: timingCountToolIncomeRate
     * @params: []
     * @return: com.aq.util.result.Result
     * @createTime: 2018/4/26 11:09
     * @version:2.1.6
     */
    Result timingCountToolIncomeRate(boolean isHistory) throws ParseException;

    /**
     * 专用趋势化工具详情
     *
     * @param toolSpecialDTO
     * @param pageBean
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/4/26
     */
    Result getSpecialToolInfo(ToolSpecialDTO toolSpecialDTO, PageBean pageBean);
}
