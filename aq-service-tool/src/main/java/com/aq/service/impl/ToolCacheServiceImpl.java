package com.aq.service.impl;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aq.core.constant.CacheKey;
import com.aq.core.constant.ToolCategoryEnum;
import com.aq.facade.contant.ToolCacheKeyEnum;
import com.aq.facade.contant.ToolStatusEnum.Pushdirection;
import com.aq.facade.contant.ToolTypeEnum;
import com.aq.facade.entity.SpecialStockHistory;
import com.aq.facade.entity.StockHistory;
import com.aq.facade.entity.TrendTool;
import com.aq.facade.exception.ToolException;
import com.aq.facade.service.IToolCacheService;
import com.aq.facade.vo.StockInfoVO;
import com.aq.facade.vo.ToolCacheVO;
import com.aq.mapper.SpecialStockHistoryMapper;
import com.aq.mapper.StockHistoryMapper;
import com.aq.mapper.TrendToolMapper;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.aq.util.string.ToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName: ToolCacheServiceImpl
 * @Description: 工具缓存数据服务实现
 * @author: lijie
 * @date: 2018年2月27日 下午3:13:35
 */
@Slf4j
@Service(version = "1.0.0")
public class ToolCacheServiceImpl implements IToolCacheService {

    @Autowired
    private StringRedisTemplate pyRedisTemplate;

    @Autowired
    private StockHistoryMapper stockHistoryMapper;

    @Autowired
    private SpecialStockHistoryMapper specialStockHistoryMapper;


    /**
     * 趋势化工具
     */
    @Autowired
    private TrendToolMapper trendToolMapper;

    @Override
    public Result getToolBindRealTimeDatas(ToolCategoryEnum type, List<String> toolIdentys, Byte toolType) {
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        Map<String, ToolCacheVO> map = new HashMap<>();
        Collection<Object> keys = new HashSet<>(toolIdentys);
        try {
            List<Object> datas = pyRedisTemplate.opsForHash().multiGet(ToolCacheKeyEnum.GET.getKeyByToolType(type, toolType.toString()), keys);
            log.info("根据缓存获取工具计算后数据返回值={}", JSON.toJSONString(datas));
            if (CollectionUtils.isNotEmpty(datas)) {
                ToolCacheVO tc;
                JSONArray array;
                switch (type) {
                    case GRID:
                        // CacheKey.GRID_TOOL_CALCULATE
                        break;
                    case SELLING:
                        // CacheKey.SELLING_TOOL_CALCULATE
                        break;
                    case TREND:
                        for (Object obj : datas) {
                            if (null != obj) {
                                array = JSONArray.parseArray(obj.toString());
                                tc = new ToolCacheVO();
                                tc.setLatestPrice(array.getString(3));
                                map.put(array.getString(1), tc);
                                if (ToolTypeEnum.SPECIAL_PURPOSE.getType().equals(toolType)) {
                                    if (Pushdirection.CONTINUED_PURCHASE.getDirection().equals(array.getString(4))) {
                                        tc.setDirection("看多");
                                        continue;
                                    } else if (!Pushdirection.PURCHASE.getDirection().equals(array.getString(4))
                                            && !Pushdirection.SELL_OUT.getDirection().equals(array.getString(4))) {
                                        tc.setDirection("-");
                                        continue;
                                    }
                                }
                                tc.setDirection(castDirection(array.getString(4)));
                            }
                        }
                        break;
                    default:
                        log.warn("缓存里面取工具数据类别不存在");
                        break;
                }
            }
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS, map);
        } catch (Exception e) {
            log.error("根据绑定标识在缓存里获取工具计算数据异常", e);
        }
        return result;
    }

    @Override
    @Deprecated
    public Result operHistorySharesData() {
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            Long maxSize = pyRedisTemplate.opsForList().size(CacheKey.STOCK_GROUP.getKey());
            log.info("获取股票分组条数={}", maxSize);
            if (null != maxSize && maxSize > 0) {
                maxSize -= 1;
                while (maxSize-- > 0) {
                    String group = pyRedisTemplate.opsForList().rightPop(CacheKey.STOCK_GROUP.getKey());
                    Set<Object> keys = pyRedisTemplate.opsForHash().keys(group);
                    Object[] objs = new Object[keys.size()];
                    int i = 0;
                    for (Object obj : keys) {
                        objs[i++] = obj;
                    }
                    pyRedisTemplate.opsForHash().delete(group, objs);
                }
            }
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("删除历史股票数据异常", e);
        }
        return result;
    }

    @Override
    public Result addToolBaseDatas(ToolCategoryEnum type, Map<String, String> tools) {
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        if (null == type || null == tools || tools.isEmpty()) {
            result.setMessage("需要添加的工具数据不存在");
            return result;
        }
        try {
            // 处理存在的数据
            final String addKey = ToolCacheKeyEnum.ADD.getKey(type);
            Map<String, String> addTools = new HashMap<>();
            for (Map.Entry<String, String> map : tools.entrySet()) {
                if (!pyRedisTemplate.opsForHash().hasKey(addKey, map.getKey())) {
                    addTools.put(map.getKey(), map.getValue());
                }
            }
            if (addTools.isEmpty()) {
                log.info("没有需要同步缓存的数据,或则数据已存在redis");
                return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
            }
            // 批量执行新增
            pyRedisTemplate.opsForHash().putAll(addKey, addTools);
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("将工具数据写入缓存异常", e);
        }
        return result;
    }

    @Override
    public Result addToolBaseDataNew(ToolCategoryEnum type, Map<String, Map<String, String>> tools) {
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        if (null == type || null == tools || tools.isEmpty()) {
            result.setMessage("需要添加的工具数据不存在");
            return result;
        }
        try {
            if (tools.containsKey(ToolTypeEnum.ROUTINE.name()) && !tools.get(ToolTypeEnum.ROUTINE.name()).isEmpty()) {
                //常规工具处理
                handleAdd(type, tools.get(ToolTypeEnum.ROUTINE.name()), ToolTypeEnum.ROUTINE);
            }
            if (tools.containsKey(ToolTypeEnum.SPECIAL_PURPOSE.name()) && !tools.get(ToolTypeEnum.SPECIAL_PURPOSE.name()).isEmpty()) {
                //专用工具处理
                handleAdd(type, tools.get(ToolTypeEnum.SPECIAL_PURPOSE.name()), ToolTypeEnum.SPECIAL_PURPOSE);
            }
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("将工具数据写入缓存异常", e);
        }
        return result;
    }

    private void handleAdd(ToolCategoryEnum type, Map<String, String> tools, ToolTypeEnum toolTypeEnum) {
        final String addKey = ToolCacheKeyEnum.ADD.getKeyByToolType(type, toolTypeEnum.getType().toString());
        Map<String, String> addTools = new HashMap<>();
        // 处理存在的数据
        for (Map.Entry<String, String> map : tools.entrySet()) {
            if (!pyRedisTemplate.opsForHash().hasKey(addKey, map.getKey())) {
                addTools.put(map.getKey(), map.getValue());
            }
        }
        if (addTools.isEmpty()) {
            log.info("没有需要同步缓存的数据,或则数据已存在redis");
            return;
        }
        // 批量执行新增
        pyRedisTemplate.opsForHash().putAll(addKey, addTools);
    }

    @Override
    public Result deleteToolCacheDatas(ToolCategoryEnum type, List<String> keys) {
        final Result result = ResultUtil.getResult(RespCode.Code.SUCCESS);
        Object[] objs = new Object[keys.size()];
        int i = 0;
        for (Object obj : keys) {
            objs[i++] = obj;
        }
        try {
            Long base_num = 0L;
            // 删除基础数据、趋势量化数据目前值保存在一个key
            if (type != ToolCategoryEnum.TREND) {
                base_num = pyRedisTemplate.opsForHash().delete(ToolCacheKeyEnum.ADD.getKey(type), objs);
            }
            Long calculate_num = pyRedisTemplate.opsForHash().delete(ToolCacheKeyEnum.DELETE.getKey(type), objs);
            result.setData("删除工具基础数据条数：" + base_num + ",计算后的工具条数：" + calculate_num);
        } catch (Exception e) {
            log.error("删除缓存工具数据异常", e);
            ResultUtil.setResult(result, RespCode.Code.FAIL);
        }
        return result;
    }

    @Override
    public Result deleteToolCacheDataNew(ToolCategoryEnum type, Map<String, List<String>> keys) {
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        if (null == type || null == keys || keys.isEmpty()) {
            result.setMessage("需要删除的工具数据不存在");
            return result;
        }
        try {
            if (keys.containsKey(ToolTypeEnum.ROUTINE.name()) && !keys.get(ToolTypeEnum.ROUTINE.name()).isEmpty()) {
                //常规工具处理
                handleDelete(type, keys.get(ToolTypeEnum.ROUTINE.name()), ToolTypeEnum.ROUTINE);
            }
            if (keys.containsKey(ToolTypeEnum.SPECIAL_PURPOSE.name()) && !keys.get(ToolTypeEnum.SPECIAL_PURPOSE.name()).isEmpty()) {
                //专用工具处理
                handleDelete(type, keys.get(ToolTypeEnum.SPECIAL_PURPOSE.name()), ToolTypeEnum.SPECIAL_PURPOSE);
            }
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("将工具数据删除缓存异常", e);
        }
        return result;
    }

    private void handleDelete(ToolCategoryEnum type, List<String> keys, ToolTypeEnum toolTypeEnum) {
        Object[] objs = new Object[keys.size()];
        int i = 0;
        for (Object obj : keys) {
            objs[i++] = obj;
        }
        Long base_num = 0L;
        // 删除基础数据、趋势量化数据目前值保存在一个key
        if (type != ToolCategoryEnum.TREND) {
            base_num = pyRedisTemplate.opsForHash().delete(ToolCacheKeyEnum.ADD.getKeyByToolType(type, toolTypeEnum.getType().toString()), objs);
        }
        Long calculate_num = pyRedisTemplate.opsForHash().delete(ToolCacheKeyEnum.DELETE.getKeyByToolType(type, toolTypeEnum.getType().toString()), objs);
        log.info("删除工具基础数据条数：" + base_num + ",计算后的工具条数：" + calculate_num);
    }

    @Override
    public Result getNewPriceByCode(List<String> codes) {
        try {
            return ResultUtil.getResult(RespCode.Code.SUCCESS, queryCodeNewPrice(codes));
        } catch (Exception e) {
            log.error("根据股票代码获取最新价异常", e);
            return ResultUtil.getResult(RespCode.Code.FAIL);
        }
    }

    private Map<String, String> queryCodeNewPrice(List<String> codes) {
        Map<String, String> rmap = new HashMap<>();
        Object obj;
        JSONObject json;
        for (String code : codes) {
            obj = pyRedisTemplate.opsForHash().get(CacheKey.STOCK_REALTIME_DATA.getKey(), code);
            if (null != obj) {
                json = JSONObject.parseObject(obj.toString());
                if (null != json) {
                    rmap.put(code, json.getString("price"));
                }
            }
        }
        return rmap;
    }

    @Override
    public Result getToolRealTimeDatas(ToolCategoryEnum type, List<String> tools) {
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        Map<String, ToolCacheVO> map = new HashMap<>();
        ToolCacheVO tc;
        Integer toolId;
        try {
            switch (type) {
                case GRID:
                    // CacheKey.GRID_TOOL_CALCULATE
                    break;
                case SELLING:
                    // CacheKey.SELLING_TOOL_CALCULATE
                    break;
                case TREND:
                    List<Integer> ids = new ArrayList<>();
                    for (String str : tools) {
                        toolId = ToolUtils.getToolIdByTool(str);
                        if (null == toolId) {
                            continue;
                        }
                        ids.add(toolId);
                    }
                    if (!ids.isEmpty()) {
                        List<TrendTool> toolList = trendToolMapper.selectToolByIds(ids);
                        if (null != toolList && !toolList.isEmpty()) {
                            Set<String> codes = new HashSet<>();
                            for (TrendTool t : toolList) {
                                codes.add(t.getStockCode());
                            }
                            Map<String, String> codemap = queryCodeNewPrice(new ArrayList<>(codes));
                            for (TrendTool t : toolList) {
                                tc = new ToolCacheVO();
                                tc.setLatestPrice(codemap.get(t.getStockCode()));
                                tc.setDirection("-");
                                map.put(ToolUtils.getToolKey(t.getId()), tc);
                            }
                        }
                    }
                    break;
                default:
                    log.warn("处理未绑定的工具数据类别不存在");
                    break;
            }
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS, map);
        } catch (Exception e) {
            log.error("处理未绑定的工具数据异常", e);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveStockHistoryDatas() {
        log.info("保存股票历史数据 -- start --");
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            List<Object> list = pyRedisTemplate.opsForHash().values(CacheKey.TREND_TOOL_CALCULATE_BASE.getKey());
            if (CollectionUtils.isNotEmpty(list)) {
                List<StockHistory> inserts = new ArrayList<>();
                StockHistory insert;
                Date createTime = new Date();
                JSONObject value;
                for (Object obj : list) {
                    if (null != obj) {
                        value = JSONObject.parseObject(obj.toString());
                        insert = new StockHistory();
                        insert.setCode(value.getString("code"));
                        insert.setValue(value.toString());
                        insert.setCreateTime(createTime);
                        inserts.add(insert);
                    }
                }
                if (!inserts.isEmpty()) {
                    int num = stockHistoryMapper.insertList(inserts);
                    log.info("保存股票历史数据成功条数={}", num);
                    return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
                }
            }
        } catch (Exception e) {
            log.error("保存股票历史数据异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result saveSpecialStockHistoryData() {
        log.info("保存股票历史数据--专用趋势化 -- start --");
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            List<Object> list = pyRedisTemplate.opsForHash().values(CacheKey.SPECIAL_TREND_TOOL_CALCULATE_BASE.getKey());
            if (CollectionUtils.isNotEmpty(list)) {
                List<SpecialStockHistory> inserts = new ArrayList<>();
                SpecialStockHistory insert;
                Date createTime = new Date();
                JSONObject value;
                for (Object obj : list) {
                    if (null != obj) {
                        value = JSONObject.parseObject(obj.toString());
                        insert = new SpecialStockHistory();
                        insert.setCode(value.getString("code"));
                        insert.setValue(value.toString());
                        insert.setCreateTime(createTime);
                        inserts.add(insert);
                    }
                }
                if (!inserts.isEmpty()) {
                    int num = specialStockHistoryMapper.insertList(inserts);
                    log.info("保存股票历史数据--专用趋势化 成功条数={}", num);
                    return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
                }
            }
        } catch (Exception e) {
            log.error("保存股票历史数据--专用趋势化 异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    @Override
    public Result getCacheStockInfo(String stockCode) {
        log.info("根据股票代码获取股票信息入参={}", stockCode);
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        if (StringUtils.isBlank(stockCode)) {
            result.setMessage("无效的股票代码");
            return result;
        }
        try {
            Object value = pyRedisTemplate.opsForHash().get(CacheKey.STOCK_REALTIME_DATA.getKey(), stockCode);
            if (null == value) {
                result.setMessage("无效的股票代码");
                return result;
            }
            StockInfoVO vo = new StockInfoVO();
            JSONObject json = JSONObject.parseObject(value.toString());
            vo.setCode(json.getString("code"));
            vo.setName(json.getString("name"));
            vo.setPrice(json.getString("price"));
            // 获取方向[目前方向针对趋势量化]
            /**
             * modify 没有任何地方用，暂时注释
             *
             * @author 郑朋
             * @create 2018/4/25
             */
            //vo.setDirection(queryDirectionByStatus(stockCode));
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS, vo);
        } catch (Exception e) {
            log.error("趋势量化/根据股票代码获取股票信息异常", e);
        }
        return result;
    }

    /**
     * @return String
     * @throws
     * @Title: queryDirectionByStatus
     * @Description: 获取方向
     * @param: @param stockCode
     * @param: @return
     * @author lijie
     */
    private String queryDirectionByStatus(final String stockCode) {
        Object value = pyRedisTemplate.opsForHash().get(CacheKey.TREND_TOOL_CALCULATE_BASE.getKey(), stockCode);
        if (null != value) {
            JSONObject json = JSONObject.parseObject(value.toString());
            if (null != json) {
                return castDirection(json.getString("aq_status"));
            }
        }
        return "-";
    }

    /**
     * @return String
     * @throws
     * @Title: castDirection
     * @Description: 转换方向
     * @param: @param status
     * @param: @return
     * @author lijie
     */
    private String castDirection(final String status) {
        String result = "观望";
        if (StringUtils.isNotBlank(status)) {
            switch (status) {
                case "1":
                    result = "看多";
                    break;
                case "2":
                    result = "持续看多";
                    break;
                case "3":
                    result = "看空";
                    break;
                case "5":
                    result = "停牌";
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    @Override
    public Result updateCacheToolBaseIsPush() {
        log.info("定时修改缓存的工具数据的是否推送状态 -- start -- ");
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            Set<Object> keys = pyRedisTemplate.opsForHash().keys(CacheKey.TREND_TOOL_BASE.getKey());
            if (null != keys && !keys.isEmpty()) {
                List<Object> list = pyRedisTemplate.opsForHash().multiGet(CacheKey.TREND_TOOL_BASE.getKey(), keys);
                if (null != list && !list.isEmpty()) {
                    Map<String, String> tools = new HashMap<>();
                    JSONArray array;
                    for (Object obj : list) {
                        array = JSONArray.parseArray(obj.toString());
                        array.set(array.size() - 1, "0");
                        tools.put(array.getString(1), array.toJSONString());
                    }
                    pyRedisTemplate.opsForHash().putAll(CacheKey.TREND_TOOL_BASE.getKey(), tools);
                    log.info("批量修改缓存工具是否推送成功");
                }
            }

            Set<Object> keysSpecial = pyRedisTemplate.opsForHash().keys(CacheKey.SPECIAL_TREND_TOOL_BASE.getKey());
            if (null != keysSpecial && !keysSpecial.isEmpty()) {
                List<Object> list = pyRedisTemplate.opsForHash().multiGet(CacheKey.SPECIAL_TREND_TOOL_BASE.getKey(), keys);
                if (null != list && !list.isEmpty()) {
                    Map<String, String> tools = new HashMap<>();
                    JSONArray array;
                    for (Object obj : list) {
                        array = JSONArray.parseArray(obj.toString());
                        array.set(array.size() - 1, "0");
                        tools.put(array.getString(1), array.toJSONString());
                    }
                    pyRedisTemplate.opsForHash().putAll(CacheKey.SPECIAL_TREND_TOOL_BASE.getKey(), tools);
                    log.info("批量修改缓存工具是否推送成功");
                }
            }
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("定时修改缓存的工具数据的是否推送状态异常", e);
        }
        return result;
    }

    @Override
    public Result getCacheStockInfoByCodes(List<Object> codes) {
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        if (CollectionUtils.isNotEmpty(codes)) {
            List<Object> list = pyRedisTemplate.opsForHash().multiGet(CacheKey.STOCK_REALTIME_DATA.getKey(), codes);
            JSONObject json;
            Map<String, String> retMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(list)) {
                for (Object obj : list) {
                    if (null != obj) {
                        json = JSONObject.parseObject(obj.toString());
                        if (null != json) {
                            retMap.put(json.getString("code"), json.getString("name"));
                        }
                    }
                }
            }
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS, retMap);
        }
        return result;
    }
}
