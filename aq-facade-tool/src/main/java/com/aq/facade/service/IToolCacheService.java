package com.aq.facade.service;

import com.aq.core.constant.ToolCategoryEnum;
import com.aq.util.result.Result;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: IToolCacheService
 * @Description: 工具缓存服务
 * @author: lijie
 * @date: 2018年2月27日 下午2:55:44
 */
public interface IToolCacheService {
    /**
     * @param @param  type
     * @param @param  toolIdentys
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getToolBindRealTimeDatas
     * @Description: 获取缓存绑定的工具实时计算数据
     */
    Result getToolBindRealTimeDatas(ToolCategoryEnum type, List<String> toolIdentys, Byte toolType);

    /**
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: operSharesHistoryData
     * @Description: 操作redis缓存的历史股票数据
     */
    Result operHistorySharesData();

    /**
     * @param @param  type
     * @param @param  tools
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: addToolBaseDatas
     * @Description:将工具写入缓存数据
     */
    Result addToolBaseDatas(ToolCategoryEnum type, Map<String, String> tools);

    /**
     * 将工具写入缓存数据
     *
     * @param type
     * @param tools --key {@link com.aq.facade.contant.ToolTypeEnum} 的name
     *              --value 需要同步的缓存数据
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/4/25
     */
    Result addToolBaseDataNew(ToolCategoryEnum type, Map<String, Map<String, String>> tools);

    /**
     * @param type
     * @param keys
     * @param @return  参数
     * @return Result    返回类型
     * @throws
     * @Title: deleteToolBaseDatas
     * @Description: 删除数据
     */
    Result deleteToolCacheDatas(ToolCategoryEnum type, List<String> keys);

    /**
     * 将工具写入缓存数据
     *
     * @param type
     * @param keys --key {@link com.aq.facade.contant.ToolTypeEnum} 的name
     *              --value 需要删除的缓存数据
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/4/25
     */
    Result deleteToolCacheDataNew(ToolCategoryEnum type, Map<String, List<String>> keys);

    /**
     * @param @param  stockCode
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getNewPriceByCode
     * @Description: 根据股票代码获取相关信息
     */
    Result getNewPriceByCode(List<String> codes);

    /**
     * @param @param  type
     * @param @param  tools
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getToolRealTimeDatas
     * @Description: 获取工具未绑定的实时数据
     */
    Result getToolRealTimeDatas(ToolCategoryEnum type, List<String> tools);

    /**
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: saveStockHistoryDatas
     * @Description: 保存股票历史数据
     */
    Result saveStockHistoryDatas();


    /**
     * 保存股票历史数据--专用趋势化
     *
     * @param
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/4/26
     */
    Result saveSpecialStockHistoryData();

    /**
     * @param @param  stockCode
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: getCacheStockInfo
     * @Description: 根据股票代码获取股票信息
     */
    Result getCacheStockInfo(String stockCode);

    /**
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: updateCacheToolBaseIsPush
     * @Description: 修改缓存工具数据是否推送状态
     */
    Result updateCacheToolBaseIsPush();

    /**
     * @return Result
     * @throws
     * @Title: getCacheStockInfoByCodes
     * @Description: 根据多支股票代码获取股票信息数据
     * @param: @param codes
     * @param: @return
     * @author lijie
     */
    Result getCacheStockInfoByCodes(List<Object> codes);
}
