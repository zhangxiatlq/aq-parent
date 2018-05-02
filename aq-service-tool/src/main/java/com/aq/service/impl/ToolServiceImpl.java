package com.aq.service.impl;


import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aq.config.PythonConfig;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.constant.ToolCategoryEnum;
import com.aq.core.wechat.template.WechatPushTemplateFactory;
import com.aq.extend.IndependentAffairService;
import com.aq.extend.push.ToolPushTacticsContext;
import com.aq.extend.push.data.ToolPushData;
import com.aq.extend.template.data.ToolTemplateData;
import com.aq.facade.contant.*;
import com.aq.facade.contant.ToolStatusEnum.Pushdirection;
import com.aq.facade.dto.*;
import com.aq.facade.dto.add.AddGridToolDTO;
import com.aq.facade.dto.add.AddSellingToolDTO;
import com.aq.facade.dto.add.AddTrendToolDTO;
import com.aq.facade.dto.synchro.*;
import com.aq.facade.dto.update.UpdateGridToolDTO;
import com.aq.facade.dto.update.UpdateSellingToolDTO;
import com.aq.facade.dto.update.UpdateTrendToolDTO;
import com.aq.facade.entity.*;
import com.aq.facade.exception.ToolException;
import com.aq.facade.service.IToolCacheService;
import com.aq.facade.service.IToolService;
import com.aq.facade.vo.*;
import com.aq.mapper.*;
import com.aq.util.bean.SpringUtil;
import com.aq.util.container.ClassUtil;
import com.aq.util.date.DateUtil;
import com.aq.util.http.HttpClientUtils;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.aq.util.string.StringTools;
import com.aq.util.string.ToolUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * @author lijie
 * @ClassName: ToolServiceImpl
 * @Description: 工具服务实现
 * @date 2018年1月19日 下午5:55:55
 */
@Slf4j
@Service(version = "1.0.0")
public class ToolServiceImpl implements IToolService {
    /**
     * 网格
     */
    @Autowired
    private GridToolMapper gridToolMapper;
    /**
     * 卖点
     */
    @Autowired
    private SellingToolMapper sellingToolMapper;
    /**
     * 趋势化工具
     */
    @Autowired
    private TrendToolMapper trendToolMapper;
    /**
     * 绑定记录
     */
    @Autowired
    private ToolBindRecordMapper toolBindRecordMapper;
    /**
     * 工具推送记录
     */
    @Autowired
    private ToolPushMapper toolPushMapper;

    private static PythonConfig PYTHON_CONFIG = SpringUtil.getBeanByClass(PythonConfig.class);

    @Autowired
    private Executor asyncTaskPool;

    @Autowired
    private IToolCacheService toolCacheService;

    @Autowired
    private WechatPushTemplateFactory pushTemplateFactory;

    @Autowired
    private IndependentAffairService affairService;

    /**
     * 工具收益率
     */
    @Autowired
    private ToolIncomeRateMapper toolIncomeRateMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result applyTool(ApplyDTO request) {
        log.info("申请工具入参={}", JSON.toJSONString(request));
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String errorStr = checkTool(request);
            if (StringUtils.isNotBlank(errorStr)) {
                result.setMessage(errorStr);
                return result;
            }
            final ToolCategoryEnum type = ToolCategoryEnum.getToolCategory(request.getToolCode());
            // 根据不同的工具类型进行申请处理
            boolean flag = false;
            switch (type) {
                case GRID:
                    flag = applyGridTool(request);
                    break;
                case SELLING:
                    flag = applySellingTool(request);
                    break;
                case TREND:
                    flag = applyTrendTool(request);
                    break;
                default:
                    log.warn("申请工具类型不存在，请检查数据");
                    break;
            }
            if (flag) {
                return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
            }
            return result;
        } catch (Exception e) {
            log.error("申请工具异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param @param  dto
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: applyTrendTool
     * @Description: 申请趋势量化工具
     */
    private boolean applyTrendTool(ApplyDTO dto) {
        TrendTool trendTool = queryTrendTool(dto, dto.getApplyUserType());
        int r = trendToolMapper.insertUseGeneratedKeys(trendTool);
        if (r == 1) {
            ToolBind bind = initToolBindByApply(ToolCategoryEnum.TREND, dto);
            bind.setToolId(trendTool.getId());
            return insertToolBindRecordByApply(bind, dto.getApplyUserType());
        } else {
            throw new ToolException("申请趋势量化工具失败");
        }
    }

    /**
     * @param @param  dto
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: applyTrendTool
     * @Description: 申请卖点工具
     */
    private boolean applySellingTool(ApplyDTO dto) {
        SellingTool sellingTool = querySellingTool(dto, dto.getApplyUserType());
        int r = sellingToolMapper.insertUseGeneratedKeys(sellingTool);
        if (r == 1) {
            ToolBind bind = initToolBindByApply(ToolCategoryEnum.SELLING, dto);
            bind.setToolId(sellingTool.getId());
            return insertToolBindRecordByApply(bind, dto.getApplyUserType());
        } else {
            throw new ToolException("申请卖点工具失败");
        }
    }

    /**
     * @param @param  request
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: applyGridTool
     * @Description: 申请网格工具
     */
    private boolean applyGridTool(ApplyDTO dto) {
        GridTool gridTool = queryGridTool(dto, dto.getApplyUserType());
        int r = gridToolMapper.insertUseGeneratedKeys(gridTool);
        if (r == 1) {
            ToolBind bind = initToolBindByApply(ToolCategoryEnum.GRID, dto);
            bind.setToolId(gridTool.getId());
            return insertToolBindRecordByApply(bind, dto.getApplyUserType());
        } else {
            throw new ToolException("申请网格工具失败");
        }
    }

    /**
     * @param @param  bind
     * @param @param  applyUserType
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: insertToolBindRecordByApply
     * @Description: 新增申请的工具绑定记录
     */
    private boolean insertToolBindRecordByApply(ToolBind bind, final Byte applyUserType) {
        ToolBindRecord record = queryToolBindRecord(bind, applyUserType);
        if (toolBindRecordMapper.insertUseGeneratedKeys(record) == 1) {
            if (ToolStatusEnum.PUSH.getStatus().equals(record.getIsPush())) {
                List<Integer> list = new ArrayList<>();
                list.add(record.getId());
                // 异步执行同步数据操作
                synchroPython(list, bind.getType(), ToolOperEnum.ADD, bind.getUserId());
            }
            return true;
        } else {
            throw new ToolException("新增工具绑定记录失败");
        }
    }

    /**
     * @param @param  type
     * @param @param  managerId
     * @param @param  customerId
     * @param @return 参数
     * @return ToolBind    返回类型
     * @throws
     * @Title: initToolBindByApply
     * @Description: 初始化申请绑定信息
     */
    private ToolBind initToolBindByApply(ToolCategoryEnum type, ApplyDTO dto) {
        ToolBind bind = new ToolBind();
        bind.setType(type);
        bind.setCreaterId(dto.getManagerId());
        bind.setUserId(dto.getCustomerId());
        // 用户类型
        bind.setUserType(RoleCodeEnum.CUSTOMER.getCode());
        return bind;
    }

    /**
     * @param @param  bindIds
     * @param @param  type
     * @param @return 参数
     * @return boolean    返回类型
     * @throws Exception
     * @throws
     * @Title: synchroPython
     * @Description: 同步工具数据到python
     */
    private void synchroPython(List<Integer> bindIds, ToolCategoryEnum type, ToolOperEnum oper, Integer operId) {
        if (null == bindIds || bindIds.isEmpty()) {
            return;
        }
        asyncTaskPool.execute(new Runnable() {

            public void run() {
                if (ToolOperEnum.ADD == oper) {
                    if (ToolCategoryEnum.TREND == type) {
                        //TODO 专用趋势化工具存在修改
                        synchroCacheAdd(bindIds, type);
                    } else {
                        synchroAdd(bindIds, type);
                    }
                } else if (ToolOperEnum.DELETE == oper) {
                    if (ToolCategoryEnum.TREND == type) {
                        synchroCacheDelete(bindIds, type, 0);
                    } else {
                        synchroDelete(bindIds, type, operId);
                    }
                }
            }

        });
    }

    /**
     * @param @param bindIds
     * @param @param type    参数
     * @return void    返回类型
     * @throws
     * @Title: synchroCacheAdd
     * @Description: 趋势量化工具--向缓存写入数据
     */
    private void synchroCacheAdd(List<Integer> bindIds, ToolCategoryEnum type) {
        Map<String, Map<String, String>> tools = new HashMap<>(16);
        Map<String, JSONArray> pythonData = new HashMap<>(16);
        ToolBindInfoVO info;
        List<Object> values;
        final JSONArray routineArray = new JSONArray();
        final JSONArray specialPurposeArray = new JSONArray();
        final List<Integer> successs = new ArrayList<>();
        String toolIdenty;
        Byte toolType;
        Map<String, String> routineMap = new HashMap<>(16);
        Map<String, String> specialPurposeMap = new HashMap<>(16);
        for (Integer id : bindIds) {
            info = toolBindRecordMapper.selectToolBindInfo(id, type.getCode());
            if (null == info) {
                continue;
            }
            toolType = info.getToolType();
            values = initValues(info, type);
            toolIdenty = ToolUtils.getToolBindKey(id);
            if (ToolCategoryEnum.TREND == type && null != toolType) {
                /**
                 * modify 趋势量化--专用和普通 分别存入不同的map
                 *
                 * @author 郑朋
                 * @create 2018/4/25
                 */
                ToolTypeEnum toolTypeEnum = ToolTypeEnum.typeEnum(toolType);
                switch (toolTypeEnum) {
                    case ROUTINE:
                        routineMap.put(toolIdenty, JSON.toJSONString(values));
                        routineArray.add(new SynNotifyTrendDTO(info.getStockCode(), toolIdenty, info.getStockName()));
                        break;
                    case SPECIAL_PURPOSE:
                        specialPurposeMap.put(toolIdenty, JSON.toJSONString(values));
                        specialPurposeArray.add(new SynNotifyTrendDTO(info.getStockCode(), toolIdenty, info.getStockName()));
                        break;
                    default:
                        break;
                }

            }
            successs.add(id);
        }
        tools.put(ToolTypeEnum.ROUTINE.name(), routineMap);
        tools.put(ToolTypeEnum.SPECIAL_PURPOSE.name(), specialPurposeMap);
        log.info("调用同步新增缓存数据tools={}", tools);
        Result result = toolCacheService.addToolBaseDataNew(type, tools);
        log.info("同步缓存数据返回信息={}", JSON.toJSONString(result));
        pythonData.put(ToolTypeEnum.ROUTINE.name(), routineArray);
        pythonData.put(ToolTypeEnum.SPECIAL_PURPOSE.name(), specialPurposeArray);
        if (result.isSuccess()) {
            // 修改同步状态
            affairService.updateToolIsSynchro(successs);
            // 触发计算
            if (ToolCategoryEnum.TREND == type) {
                //普通
                if (!pythonData.get(ToolTypeEnum.ROUTINE.name()).isEmpty()) {
                    JSONObject tadd = new JSONObject();
                    tadd.put("aqTools", pythonData.get(ToolTypeEnum.ROUTINE.name()));
                    send(PYTHON_CONFIG.getAddTrendQOneItem(), tadd.toJSONString());
                }
                //专用
                if (!pythonData.get(ToolTypeEnum.SPECIAL_PURPOSE.name()).isEmpty()) {
                    JSONObject tadd = new JSONObject();
                    tadd.put("aqTools", pythonData.get(ToolTypeEnum.SPECIAL_PURPOSE.name()));
                    send(PYTHON_CONFIG.getAddTrendQAtoolSpecific(), tadd.toJSONString());
                }

            }
        }
    }

    /**
     * @param @param  info
     * @param @param  type
     * @param @return 参数
     * @return List<Object>    返回类型
     * @throws
     * @Title: initValues
     * @Description: 初始化工具信息缓存数据
     */
    private List<Object> initValues(ToolBindInfoVO info, ToolCategoryEnum type) {
        List<Object> values = new ArrayList<>();
        String toolIdenty = ToolUtils.getToolBindKey(info.getBindId());
        values.add(info.getStockCode());
        // 封装网格数据
        if (ToolCategoryEnum.GRID == type) {
            if (null == info.getEntrustNum()) {
                values.add(100);
            } else {
                values.add(info.getEntrustNum());
            }
            values.add(info.getBasePrice());
            values.add(info.getDifferencePrice());
            values.add(info.getUpperPrice());
            values.add(info.getLowerPrice());
            values.add(toolIdenty);
            values.add(info.getUserId());
            // 封装卖点数据
        } else if (ToolCategoryEnum.SELLING == type) {
            if (null == info.getEntrustNum()) {
                values.add(100);
            } else {
                values.add(info.getEntrustNum());
            }
            values.add(info.getShortDay());
            values.add(info.getLongDay());
            values.add(info.getLowerDeviate());
            values.add(info.getTopDeviate());
            values.add(toolIdenty);
            values.add(info.getUserId());
            // 封装趋势量化数据
        } else if (ToolCategoryEnum.TREND == type) {
            values.add(toolIdenty);
            values.add(info.getStockName());
            values.add("0");
            values.add("_");
            values.add("0");
        }
        return values;
    }

    /**
     * @param @param bindIds
     * @param @param type    参数
     * @return void    返回类型
     * @throws
     * @Title: synchroAdd
     * @Description: 同步新增
     */
    private void synchroAdd(List<Integer> bindIds, ToolCategoryEnum type) {
        final List<Integer> successs = new ArrayList<>();
        ToolBindInfoVO info;
        String json;
        String url = PyUrl.getSynPythonUrl(type, ToolOperEnum.ADD);
        for (Integer id : bindIds) {
            info = toolBindRecordMapper.selectToolBindInfo(id, type.getCode());
            if (null == info) {
                continue;
            }
            json = initSynPythonData(info, type);
            log.info("调用同步新增数据url={},data json={}", url, json);
            if (StringUtils.isNotBlank(url)) {
                if (send(url, json)) {
                    successs.add(id);
                }
            }
        }
        // 执行成功则修改记录状态
        if (!successs.isEmpty()) {
            affairService.updateToolIsSynchro(successs);
        }
    }

    protected static class PyUrl {

        private PyUrl() {
        }

        /**
         * py同步url
         */
        private static Map<String, String> PY_URL_MAP;

        static {
            PY_URL_MAP = new HashMap<>(6);
            PY_URL_MAP.put(getUrlKey(ToolCategoryEnum.GRID, ToolOperEnum.ADD), PYTHON_CONFIG.getAddGridUrl());
            PY_URL_MAP.put(getUrlKey(ToolCategoryEnum.SELLING, ToolOperEnum.ADD), PYTHON_CONFIG.getAddSellingUrl());
            PY_URL_MAP.put(getUrlKey(ToolCategoryEnum.TREND, ToolOperEnum.ADD), PYTHON_CONFIG.getAddTrendUrl());

            PY_URL_MAP.put(getUrlKey(ToolCategoryEnum.GRID, ToolOperEnum.DELETE), PYTHON_CONFIG.getDeleteGridUrl());
            PY_URL_MAP.put(getUrlKey(ToolCategoryEnum.SELLING, ToolOperEnum.DELETE),
                    PYTHON_CONFIG.getDeleteSellingUrl());
            PY_URL_MAP.put(getUrlKey(ToolCategoryEnum.TREND, ToolOperEnum.DELETE), PYTHON_CONFIG.getDeleteTrendUrl());
        }

        /**
         * @return String
         * @throws
         * @Title: getSynPythonUrl
         * @Description: 获取同步地址
         * @param: @param type
         * @param: @param oper
         * @param: @return
         * @author lijie
         */
        public static String getSynPythonUrl(ToolCategoryEnum type, ToolOperEnum oper) {

            return PY_URL_MAP.get(getUrlKey(type, oper));
        }

        /**
         * @return String
         * @throws
         * @Title: getUrlKey
         * @Description: 同步url key
         * @param: @param type
         * @param: @param oper
         * @param: @return
         * @author lijie
         */
        private static String getUrlKey(ToolCategoryEnum type, ToolOperEnum oper) {

            return type.getCode() + oper.name();
        }
    }

    /**
     * @return String
     * @throws
     * @Title: initSynPythonData
     * @Description: 封装同步python数据
     * @param: @param info
     * @param: @param type
     * @param: @return
     * @author lijie
     */
    private String initSynPythonData(ToolBindInfoVO info, ToolCategoryEnum type) {
        String result = null;
        final SynPythonBaseDTO base = new SynPythonBaseDTO();
        base.setToolIdenty(ToolUtils.getToolBindKey(info.getBindId()));
        base.setManagerId(StringTools.objToString(info.getManagerId()));
        base.setClientId(StringTools.objToString(info.getUserId()));
        base.setCode(info.getStockCode());
        if (ToolCategoryEnum.GRID == type) {
            SynPythonGridDTO grid = new SynPythonGridDTO();
            BeanUtils.copyProperties(base, grid);
            if (null == info.getEntrustNum()) {
                grid.setEntrustNum("100");
            } else {
                grid.setEntrustNum(StringTools.objToString(info.getEntrustNum()));
            }
            grid.setBasePrice(info.getBasePrice());
            grid.setDifferencePrice(info.getDifferencePrice());
            grid.setUpperPrice(info.getUpperPrice());
            grid.setLowerPrice(info.getLowerPrice());
            result = JSON.toJSONString(grid);
        } else if (ToolCategoryEnum.SELLING == type) {
            SynPythonSellingDTO selling = new SynPythonSellingDTO();
            BeanUtils.copyProperties(base, selling);
            if (null == info.getEntrustNum()) {
                selling.setEntrustNum("100");
            } else {
                selling.setEntrustNum(StringTools.objToString(info.getEntrustNum()));
            }
            selling.setShortDay(StringTools.objToString(info.getShortDay()));
            selling.setLongDay(StringTools.objToString(info.getLongDay()));
            selling.setTopDeviate(info.getTopDeviate());
            selling.setLowerDeviate(info.getLowerDeviate());
            result = JSON.toJSONString(selling);
        } else {
            result = JSON.toJSONString(base);
        }
        return result;
    }

    /**
     * @param @param  bindIds
     * @param @param  type
     * @param @param  operId
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: synchroCacheDelete
     * @Description: 删除redis缓存的tool工具数据
     */
    private boolean synchroCacheDelete(List<Integer> bindIds, ToolCategoryEnum type, Integer operId) {
        boolean result = false;
        List<ToolBindInfoVO> list = toolBindRecordMapper.selectToolBindInfoByCode(bindIds, type.getCode());
        final List<String> routineKeys = new ArrayList<>();
        final List<String> specialPurposeKeys = new ArrayList<>();
        final List<Integer> successs = new ArrayList<>();
        Byte toolType;
        if (CollectionUtils.isNotEmpty(list)) {
            for (ToolBindInfoVO record : list) {
                toolType = record.getToolType();
                if (ToolCategoryEnum.TREND == type && null != toolType) {
                    /**
                     * modify 趋势量化--专用和普通 分别存入不同的map
                     *
                     * @author 郑朋
                     * @create 2018/4/25
                     */
                    ToolTypeEnum toolTypeEnum = ToolTypeEnum.typeEnum(toolType);
                    switch (toolTypeEnum) {
                        case ROUTINE:
                            routineKeys.add(ToolUtils.getToolBindKey(record.getBindId()));
                            break;
                        case SPECIAL_PURPOSE:
                            specialPurposeKeys.add(ToolUtils.getToolBindKey(record.getBindId()));
                            break;
                        default:
                            break;
                    }

                }
                successs.add(record.getBindId());
            }
        }
        Map<String, List<String>> keys = new HashMap<>(2);
        keys.put(ToolTypeEnum.ROUTINE.name(), routineKeys);
        keys.put(ToolTypeEnum.SPECIAL_PURPOSE.name(), specialPurposeKeys);
        Result rresult = toolCacheService.deleteToolCacheDataNew(type, keys);
        log.info("同步删除缓存数据返回信息={}", JSON.toJSONString(rresult));
        result = rresult.isSuccess();
        if (result) {
            // 修改数据库同步删除字段
            toolBindRecordMapper.upateSynchroDelete(successs, ToolStatusEnum.UNDELETED.getStatus());
        }
        return result;
    }

    /**
     * @param @param bindIds
     * @param @param type    参数
     * @return void    返回类型
     * @throws
     * @Title: synchroDelete
     * @Description: 同步删除数据
     */
    private boolean synchroDelete(List<Integer> bindIds, ToolCategoryEnum type, Integer operId) {
        boolean result = false;
        List<ToolBindRecord> list = toolBindRecordMapper.selectRecordByIds(bindIds);
        if (CollectionUtils.isNotEmpty(list)) {
            final StringBuilder sbud = new StringBuilder();
            final List<Integer> successs = new ArrayList<>();
            for (ToolBindRecord record : list) {
                sbud.append(ToolUtils.getToolBindKey(record.getId())).append(",");
                successs.add(record.getId());
            }
            sbud.deleteCharAt(sbud.length() - 1);
            final SynPythonDelDTO del = new SynPythonDelDTO();
            del.setManagerId(null == operId ? "" : operId.toString());
            del.setToolIdentys(sbud.toString());
            String url = PyUrl.getSynPythonUrl(type, ToolOperEnum.DELETE);
            String json = JSON.toJSONString(del);
            log.info("调用同步删除python数据url={},data json={}", url, json);
            if (StringUtils.isNotBlank(url)) {
                result = send(url, json);
                if (result) {
                    // 修改是否同步删除状态
                    toolBindRecordMapper.upateSynchroDelete(successs, ToolStatusEnum.UNDELETED.getStatus());
                }
            }
        }
        return result;
    }

    /**
     * @param @param  url
     * @param @param  data
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: send
     * @Description: 发送数据
     */
    private boolean send(String url, String data) {
        log.info("异步调用python同步工具操作入参={},data={}", url, data);
        try {
            String result = HttpClientUtils.sendPostJSON(url, data);
            log.info("异步调用同步工具操作返回数据={}", result);
            if (StringUtils.isNotBlank(result)) {
                JSONObject rjson = JSON.parseObject(result, JSONObject.class);
                String code = rjson.getString("code");
                // TODO:200:成功 1：数据已存在
                return null != code && ("200".equals(code) || "1".equals(code));
            }
        } catch (Exception e) {
            log.error("异步调用同步工具接口异常", e);
        }
        return false;
    }

    /**
     * @param @param  dto
     * @param @return 参数
     * @return String    返回类型
     * @throws
     * @Title: checkTool
     * @Description: 校验申请工具数据
     */
    private String checkTool(ApplyDTO dto) {
        String result = dto.validateForm();
        if (StringUtils.isNotBlank(result)) {
            return result;
        }
        Byte utype = dto.getApplyUserType();
        ToolCategoryEnum type = ToolCategoryEnum.getToolCategory(dto.getToolCode());
        result = checkToolNum(type.getCode(), dto.getCustomerId().toString(), null);
        if (StringUtils.isNotBlank(result)) {
            return result;
        }
        if (RoleCodeEnum.CUSTOMER.getCode().equals(utype)) {
            switch (type) {
                case GRID:
                    if (StringUtils.isBlank(dto.getBasePrice())) {
                        result = "基础价不能为空";
                    } else if (StringUtils.isBlank(dto.getDifferencePrice())) {
                        result = "价差不能为空";
                    } else if (StringUtils.isBlank(dto.getUpperPrice())) {
                        result = "上限价不能为空";
                    } else if (StringUtils.isBlank(dto.getLowerPrice())) {
                        result = "下限价不能为空";
                    }
                    break;
                case SELLING:
                    if (null == dto.getShortDay()) {
                        result = "短期均线天数不能为空";
                    } else if (null == dto.getLongDay()) {
                        result = "长期均线天数不能为空";
                    } else if (dto.getShortDay() > dto.getLongDay()) {
                        result = "短期均线天数必须小于长期均线天数";
                    } else if (StringUtils.isBlank(dto.getTopDeviate())) {
                        result = "向上偏离值不能为空";
                    } else if (StringUtils.isBlank(dto.getLowerDeviate())) {
                        result = "向下偏离不能为空";
                    }
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    @Data
    private class ToolBind {
        /**
         * 工具ID
         */
        private Integer toolId;
        /**
         * 工具类型
         */
        private ToolCategoryEnum type;
        /**
         * 用户ID
         */
        private Integer userId;
        /**
         * 用户类型
         */
        private Byte userType;
        /**
         * 创建人ID
         */
        private Integer createrId;
    }

    /**
     * @param @param  toolId
     * @param @param  type
     * @param @param  userId
     * @param @param  createrId
     * @param @return 参数
     * @return ToolBindRecord    返回类型
     * @throws
     * @Title: queryToolBindRecord
     * @Description: 封装绑定数据
     */
    private ToolBindRecord queryToolBindRecord(ToolBind bind, Byte type) {
        final ToolBindRecord record = new ToolBindRecord();
        record.setCreaterId(bind.getCreaterId());
        record.setCreateTime(new Date());
        record.setUserId(bind.getUserId());
        record.setUserType(bind.getUserType());
        record.setToolId(bind.getToolId());
        record.setToolCategoryCode(bind.getType().getCode());
        record.setIsDelete(ToolStatusEnum.NOT_DELETED.getStatus());
        record.setIsSynchroDelete(ToolStatusEnum.NOT_DELETED.getStatus());
        if (ToolCategoryEnum.TREND == bind.getType()) {
            record.setIsPush(ToolStatusEnum.PUSH.getStatus());
        } else {
            if (RoleCodeEnum.CUSTOMER.getCode().equals(type)) {
                record.setIsPush(ToolStatusEnum.PUSH.getStatus());
            } else {
                record.setIsPush(ToolStatusEnum.NO_PUSH.getStatus());
            }
        }
        record.setIsSynchro(ToolStatusEnum.NOT_SYNCHRO.getStatus());
        return record;
    }

    /**
     * @param request
     * @return
     * @Title: queryGridTool
     * @author: lijie
     * @Description: 封装网格数据
     * @return: GridTool
     */
    private GridTool queryGridTool(final ApplyDTO request, final Byte userType) {
        GridTool gridTool = new GridTool();
        gridTool.setIsDelete(ToolStatusEnum.NOT_DELETED.getStatus());
        gridTool.setCreaterType(ToolStatusEnum.CUSTOMER.getStatus());
        gridTool.setManagerId(request.getManagerId());
        gridTool.setCreaterId(request.getCustomerId());
        gridTool.setToolCode(request.getToolCode());
        gridTool.setStockCode(request.getStockCode());
        gridTool.setStockName(request.getStockName());
        gridTool.setEntrustNum(request.getEntrustNum());
        gridTool.setCreateTime(new Date());
        if (RoleCodeEnum.CUSTOMER.getCode().equals(userType)) {
            if (StringUtils.isNotBlank(request.getBasePrice())) {
                gridTool.setBasePrice(new BigDecimal(request.getBasePrice()));
            }
            if (StringUtils.isNotBlank(request.getDifferencePrice())) {
                gridTool.setDifferencePrice(new BigDecimal(request.getDifferencePrice()));
            }
            if (StringUtils.isNotBlank(request.getLowerPrice())) {
                gridTool.setLowerPrice(new BigDecimal(request.getLowerPrice()));
            }
            if (StringUtils.isNotBlank(request.getUpperPrice())) {
                gridTool.setUpperPrice(new BigDecimal(request.getUpperPrice()));
            }
            gridTool.setStatus(ToolStatusEnum.NORMAL.getStatus());
        } else {
            gridTool.setStatus(ToolStatusEnum.TO_BE_CONFIRMED.getStatus());
        }
        return gridTool;
    }

    /**
     * @param datas
     * @param toolCode
     * @param createrId
     * @return
     * @Title: queryGridTools
     * @author: lijie
     * @Description: 批量网格工具数据
     * @return: List<GridTool>
     */
    private List<GridTool> queryGridTools(List<GridToolDTO> datas, String toolCode, Integer createrId) {
        List<GridTool> result = new ArrayList<GridTool>(datas.size());
        GridTool tool;
        for (GridToolDTO dto : datas) {
            tool = new GridTool();
            BeanUtils.copyProperties(dto, tool);
            tool.setStatus(ToolStatusEnum.NORMAL.getStatus());
            tool.setIsDelete(ToolStatusEnum.NOT_DELETED.getStatus());
            tool.setCreaterType(ToolStatusEnum.CUSTOMER_MANAGER.getStatus());
            tool.setManagerId(createrId);
            tool.setCreaterId(createrId);
            tool.setToolCode(toolCode);
            tool.setCreateTime(new Date());
            if (StringUtils.isNotBlank(dto.getBasePrice())) {
                tool.setBasePrice(new BigDecimal(dto.getBasePrice()));
            }
            if (StringUtils.isNotBlank(dto.getDifferencePrice())) {
                tool.setDifferencePrice(new BigDecimal(dto.getDifferencePrice()));
            }
            if (StringUtils.isNotBlank(dto.getUpperPrice())) {
                tool.setLowerPrice(new BigDecimal(dto.getLowerPrice()));
            }
            if (StringUtils.isNotBlank(dto.getUpperPrice())) {
                tool.setUpperPrice(new BigDecimal(dto.getUpperPrice()));
            }
            result.add(tool);
        }
        return result;
    }

    /**
     * @param request
     * @return
     * @Title: querySellingTool
     * @author: lijie
     * @Description: 封装卖点工具
     * @return: SellingTool
     */
    private SellingTool querySellingTool(final ApplyDTO request, final Byte userType) {
        SellingTool sellingTool = new SellingTool();
        sellingTool.setIsDelete(ToolStatusEnum.NOT_DELETED.getStatus());
        sellingTool.setCreaterType(ToolStatusEnum.CUSTOMER.getStatus());
        sellingTool.setManagerId(request.getManagerId());
        sellingTool.setCreaterId(request.getCustomerId());
        sellingTool.setCreateTime(new Date());
        sellingTool.setToolCode(request.getToolCode());
        sellingTool.setStockCode(request.getStockCode());
        sellingTool.setStockName(request.getStockName());
        sellingTool.setEntrustNum(request.getEntrustNum());
        if (RoleCodeEnum.CUSTOMER.getCode().equals(userType)) {
            sellingTool.setShortDay(request.getShortDay());
            sellingTool.setLongDay(request.getLongDay());
            if (null != request.getTopDeviate()) {
                sellingTool.setTopDeviate(Double.valueOf(request.getTopDeviate()));
            }
            if (null != request.getLowerDeviate()) {
                sellingTool.setLowerDeviate(Double.valueOf(request.getLowerDeviate()));
            }
            sellingTool.setStatus(ToolStatusEnum.NORMAL.getStatus());
        } else {
            sellingTool.setStatus(ToolStatusEnum.TO_BE_CONFIRMED.getStatus());
        }
        return sellingTool;
    }

    /**
     * @param datas
     * @param toolCode
     * @param createrId
     * @return
     * @Title: querySellingTools
     * @author: lijie
     * @Description: 卖点工具数据
     * @return: List<SellingTool>
     */
    private List<SellingTool> querySellingTools(List<SellingToolDTO> datas, String toolCode, Integer createrId) {
        List<SellingTool> result = new ArrayList<>(datas.size());
        SellingTool tool;
        for (SellingToolDTO dto : datas) {
            tool = new SellingTool();
            BeanUtils.copyProperties(dto, tool);
            tool.setStatus(ToolStatusEnum.NORMAL.getStatus());
            tool.setIsDelete(ToolStatusEnum.NOT_DELETED.getStatus());
            tool.setCreaterType(ToolStatusEnum.CUSTOMER_MANAGER.getStatus());
            tool.setManagerId(createrId);
            tool.setCreaterId(createrId);
            tool.setToolCode(toolCode);
            tool.setCreateTime(new Date());
            tool.setTopDeviate(Double.valueOf(dto.getTopDeviate()));
            tool.setLowerDeviate(Double.valueOf(dto.getLowerDeviate()));
            tool.setLongDay(dto.getLongDay());
            tool.setShortDay(dto.getShortDay());
            result.add(tool);
        }
        return result;
    }

    /**
     * @param request
     * @return
     * @Title: queryTrendTool
     * @author: lijie
     * @Description: 封装趋势化
     * @return: TrendTool
     */
    private TrendTool queryTrendTool(final ApplyDTO request, final Byte userType) {
        TrendTool trendTool = new TrendTool();
        trendTool.setStatus(ToolStatusEnum.NORMAL.getStatus());
        trendTool.setIsDelete(ToolStatusEnum.NOT_DELETED.getStatus());
        trendTool.setCreaterType(ToolStatusEnum.CUSTOMER.getStatus());
        trendTool.setManagerId(request.getManagerId());
        trendTool.setCreaterId(request.getCustomerId());
        trendTool.setCreateTime(new Date());
        trendTool.setStockCode(request.getStockCode());
        trendTool.setStockName(request.getStockName());
        trendTool.setToolCode(request.getToolCode());
        trendTool.setToolType(ToolTypeEnum.ROUTINE.getType());
        return trendTool;
    }

    /**
     * @param datas
     * @param toolCode
     * @param createrId
     * @return
     * @Title: queryTrendTools
     * @author: lijie
     * @Description: 趋势化工具数据
     * @return: List<TrendTool>
     */
    private List<TrendTool> queryTrendTools(Byte type, List<TrendToolDTO> datas, String toolCode, Integer createrId) {
        List<TrendTool> result = new ArrayList<TrendTool>(datas.size());
        TrendTool tool;
        for (TrendToolDTO dto : datas) {
            tool = new TrendTool();
            BeanUtils.copyProperties(dto, tool);
            tool.setStatus(ToolStatusEnum.NORMAL.getStatus());
            tool.setIsDelete(ToolStatusEnum.NOT_DELETED.getStatus());
            tool.setCreaterType(ToolStatusEnum.CUSTOMER_MANAGER.getStatus());
            tool.setManagerId(createrId);
            tool.setCreaterId(createrId);
            tool.setToolCode(toolCode);
            tool.setCreateTime(new Date());
            tool.setToolType(type);
            result.add(tool);
        }
        return result;
    }

    @Override
    public Result getToolsByPage(Integer userId, PageBean pageBean) {
        log.info("查询个人工具入参={},userId={}", JSON.toJSONString(pageBean), userId);
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            List<WeChatToolVO> list = new ArrayList<>();
            if (null != pageBean) {
                PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
                List<WeChatToolVO> qlist = toolBindRecordMapper.selectToolBindRecord(userId);
                PageInfo<WeChatToolVO> pageInfo = new PageInfo<WeChatToolVO>(qlist);
                list = pageInfo.getList();
                result.setCount(pageInfo.getTotal());
            } else {
                list = toolBindRecordMapper.selectToolBindRecord(userId);
                result.setCount(list.size());
            }
            result.setData(list);
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("查询个人工具信息异常", e);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addGridTools(AddGridToolDTO dto) {
        log.info("添加网格工具入参={}", JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        try {
            String errorStr = dto.validateForm();
            if (StringUtils.isBlank(errorStr)) {
                errorStr = ClassUtil.checkRequest(dto.getDatas());
            }
            if (StringUtils.isNotBlank(errorStr)) {
                result.setMessage(errorStr);
            } else {
                gridToolMapper.insertList(
                        queryGridTools(dto.getDatas(), ToolCategoryEnum.GRID.getCode(), dto.getCreaterId()));
                return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
            }
            return result;
        } catch (Exception e) {
            log.error("添加网格工具异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addSellingTools(AddSellingToolDTO dto) {
        log.info("添加卖点工具入参={}", JSON.toJSONString(dto));
        final Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        try {
            String errorStr = dto.validateForm();
            if (StringUtils.isBlank(errorStr)) {
                errorStr = ClassUtil.checkRequest(dto.getDatas());
            }
            if (StringUtils.isNotBlank(errorStr)) {
                result.setMessage(errorStr);
            } else {
                sellingToolMapper.insertList(
                        querySellingTools(dto.getDatas(), ToolCategoryEnum.SELLING.getCode(), dto.getCreaterId()));
                return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
            }
            return result;
        } catch (Exception e) {
            log.error("添加卖点工具异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result addTrendTool(AddTrendToolDTO dto) {
        log.info("添加趋势化工具入参={}", JSON.toJSONString(dto));
        final Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        try {
            String errorStr = dto.validateForm();
            if (StringUtils.isBlank(errorStr)) {
                errorStr = ClassUtil.checkRequest(dto.getDatas());
            }
            if (StringUtils.isNotBlank(errorStr)) {
                result.setMessage(errorStr);
            } else {
                trendToolMapper.insertList(queryTrendTools(dto.getToolType(), dto.getDatas(),
                        ToolCategoryEnum.TREND.getCode(), dto.getCreaterId()));
                return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
            }
            return result;
        } catch (Exception e) {
            log.error("添加趋势化工具异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result bingTool(BingToolDTO dto) {
        log.info("推荐绑定工具入参={}", JSON.toJSONString(dto));
        final Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        String errorStr = dto.validateForm();
        if (StringUtils.isNotBlank(errorStr)) {
            result.setMessage(errorStr);
            return result;
        } else if (StringUtils.isBlank(dto.getCustomerIds()) && StringUtils.isBlank(dto.getManagerIds())) {
            result.setMessage("请选择推荐的客户");
            return result;
        }
        errorStr = checkToolNum(dto.getCategoryCode(), dto.getCustomerIds(), dto.getToolId());
        if (StringUtils.isNotBlank(errorStr)) {
            result.setMessage(errorStr);
            return result;
        }
        if (checkToolIsExists(dto.getCategoryCode(), dto.getToolId())) {
            // 处理新增记录数据
            return inertToolBindRecord(dto);
        } else {
            log.info("工具id:" + dto.getToolId() + "不存在");
            result.setMessage("该工具不存在或已被删除");
        }
        return result;
    }

    /**
     * @param @param  dto
     * @param @return 参数
     * @return Result    返回类型
     * @throws
     * @Title: inertToolBindRecord
     * @Description: 处理新增推荐记录
     */
    @Transactional(rollbackFor = Exception.class)
    private Result inertToolBindRecord(BingToolDTO dto) {
        try {
            // 初始化记录实体
            List<ToolBindRecord> records = initToolBindRecord(dto);
            // 处理客户推荐
            final List<ToolBindRecord> insertList = new ArrayList<>();
            insertList.addAll(queryCustomerToolBindRecord(dto.getCustomerIds(), records));
            // 处理客户经理推荐
            insertList.addAll(queryManagerToolBindRecord(dto.getManagerIds(), records));
            toolBindRecordMapper.insertList(insertList);
            final List<Integer> list = new ArrayList<>();
            for (ToolBindRecord bind : insertList) {
                if (null == bind) {
                    continue;
                }
                list.add(bind.getId());
            }
            // 异步执行数据同步
            synchroPython(list, ToolCategoryEnum.getToolCategory(dto.getCategoryCode()), ToolOperEnum.ADD,
                    dto.getCreaterId());
            return ResultUtil.getResult(RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("推荐绑定工具异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param @param  managerIds
     * @param @param  record
     * @param @return 参数
     * @return List<ToolBindRecord>    返回类型
     * @throws
     * @Title: queryManagerToolBindRecord
     * @Description: 封装客户经理推荐记录
     */
    private List<ToolBindRecord> queryManagerToolBindRecord(final String managerIds, final List<ToolBindRecord> records) {
        final List<ToolBindRecord> managers = new ArrayList<>();
        if (StringUtils.isNotBlank(managerIds)) {
            String[] strs = managerIds.split(",");
            ToolBindRecord insert;
            for (ToolBindRecord record : records
                    ) {
                for (String cuId : strs) {
                    insert = new ToolBindRecord();
                    BeanUtils.copyProperties(record, insert);
                    insert.setCreateTime(new Date());
                    insert.setUserId(Integer.valueOf(cuId));
                    insert.setUserType(RoleCodeEnum.MANAGER.getCode());
                    managers.add(insert);
                }
            }
        }

        return managers;
    }

    /**
     * @param @param  customerIds
     * @param @param  record
     * @param @return 参数
     * @return List<ToolBindRecord>    返回类型
     * @throws
     * @Title: queryCustomerToolBindRecord
     * @Description: 封装客户推荐记录
     */
    private List<ToolBindRecord> queryCustomerToolBindRecord(final String customerIds, final List<ToolBindRecord> records) {
        final List<ToolBindRecord> customers = new ArrayList<>();
        if (StringUtils.isNotBlank(customerIds)) {
            String[] strs = customerIds.split(",");
            ToolBindRecord insert;
            for (ToolBindRecord record : records
                    ) {
                for (String cuId : strs) {
                    insert = new ToolBindRecord();
                    BeanUtils.copyProperties(record, insert);
                    insert.setCreateTime(new Date());
                    insert.setUserId(Integer.valueOf(cuId));
                    insert.setUserType(RoleCodeEnum.CUSTOMER.getCode());
                    customers.add(insert);
                }
            }
        }

        return customers;
    }

    /**
     * @param @param  dto
     * @param @return 参数
     * @return ToolBindRecord    返回类型
     * @throws
     * @Title: initToolBindRecord
     * @Description: 初始化记录实体
     */
    private List<ToolBindRecord> initToolBindRecord(BingToolDTO dto) {
        List<ToolBindRecord> list = new ArrayList<>();
        for (String toolid :
                dto.getToolId().split(",")) {
            ToolBindRecord record = new ToolBindRecord();
            record.setCreaterId(dto.getCreaterId());
            record.setToolCategoryCode(dto.getCategoryCode());
            record.setToolId(Integer.parseInt(toolid));
            record.setIsDelete(ToolStatusEnum.NOT_DELETED.getStatus());
            record.setIsPush(ToolStatusEnum.PUSH.getStatus());
            record.setIsSynchro(ToolStatusEnum.NOT_SYNCHRO.getStatus());
            record.setIsSynchroDelete(ToolStatusEnum.NOT_DELETED.getStatus());
            list.add(record);
        }

        return list;
    }

    /**
     * @param @param  categoryCode
     * @param @param  customerIds
     * @param @return 参数
     * @return String    返回类型
     * @throws
     * @Title: checkToolNum
     * @Description: 校验工具推荐数量
     */
    private String checkToolNum(String categoryCode, String customerIds, String toolIds) {
        StringBuilder sbud = new StringBuilder();
        if (StringUtils.isNotBlank(customerIds)) {
            String[] ids = customerIds.split(",");
            int currentNum = 0;
            if (Objects.nonNull(toolIds)) {
                //若pc端添加工具，需要进一步处理
                String[] tools = toolIds.split(",");
                currentNum = currentNum + tools.length;
            }

            ToolBindRecord record = new ToolBindRecord();
            ToolSetUpVO vo;
            int bingNum;
            for (String id : ids) {
                record.setUserId(Integer.valueOf(id));
                record.setToolCategoryCode(categoryCode);
                record.setIsDelete(ToolStatusEnum.NOT_DELETED.getStatus());
                record.setUserType(ToolStatusEnum.CUSTOMER.getStatus());
                bingNum = toolBindRecordMapper.selectCount(record);
                vo = toolBindRecordMapper.selectToolSetUp(categoryCode, Integer.valueOf(id));
                if (null == vo) {
                    sbud.append("客户id：").append(id).append(",工具数据设置不存在;");
                    break;
                }
                if (bingNum >= vo.getNum()) {
                    sbud.append("客户：").append(vo.getRealName()).append(",工具数据已达到上限;");
                    break;
                }
                if (Objects.nonNull(toolIds)) {
                    //若pc端添加工具，需要进一步处理
                    //验证一下多个工具能不能推送
                    if (bingNum + currentNum > vo.getNum()) {
                        sbud.append("客户：").append(vo.getRealName()).append(",工具选择数据将超过上限;");
                        break;
                    }
                }
            }
        }
        return sbud.toString();
    }

    /**
     * @param @param  code
     * @param @param  toolIds:用逗号隔开的多个工具id
     * @param @return 参数
     * @return boolean    返回类型
     * @throws
     * @Title: checkToolIsExists
     * @Description: 校验工具是否存在
     */
    private boolean checkToolIsExists(final String code, final String toolIds) {
        final ToolCategoryEnum type = ToolCategoryEnum.getToolCategory(code);
        String[] tIds = toolIds.split(",");
        Set<Integer> toolIdList = new HashSet<>();
        //转化工具id
        for (String id : tIds) {
            toolIdList.add(Integer.parseInt(id));
        }
        int toolsNum = toolIdList.size();//工具数量
        List<Integer> integerList = new ArrayList<>(toolIdList);
        log.info("checkToolIsExists 方法入参参数处理后：code={}，toolIds={}", code, JSON.toJSONString(toolIdList));
        switch (type) {
            case GRID:
                return gridToolMapper.selectListGridToolsByIds(integerList).size() == toolsNum;
            case SELLING:
                return toolsNum == sellingToolMapper.selectListSellingToolsByIds(integerList).size();
            case TREND:
                return toolsNum == trendToolMapper.selectListTrendToolsByIds(integerList).size();
            default:
                break;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateGridTool(UpdateGridToolDTO dto) {
        log.info("修改网格工具入参={}", JSON.toJSONString(dto));
        final Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        try {
            String errorStr = dto.validateForm();
            if (StringUtils.isBlank(errorStr)) {
                errorStr = ClassUtil.checkRequest(dto.getData());
            }
            if (StringUtils.isNotBlank(errorStr)) {
                result.setMessage(errorStr);
                return result;
            }
            Integer toolId = checkToolId(ToolCategoryEnum.GRID, dto.getToolId());
            if (null == toolId) {
                result.setMessage("工具数据不存在");
                return result;
            }
            // 校验是否是申请的工具
            ToolBindRecord bindRecord = new ToolBindRecord();
            bindRecord.setToolCategoryCode(ToolCategoryEnum.GRID.getCode());
            bindRecord.setToolId(toolId);
            bindRecord = toolBindRecordMapper.selectOne(bindRecord);
            if (null == bindRecord) {
                result.setMessage("只能修改客户申请的工具");
                return result;
            }
            // 得到需要修改的数据
            GridTool tool = initUpdateGridTool(dto.getData());
            tool.setId(toolId);
            tool.setUpdaterId(dto.getUpdaterId());

            gridToolMapper.updateByPrimaryKeySelective(tool);

            List<Integer> bindIds = new ArrayList<>();
            bindIds.add(bindRecord.getId());
            // 同步操作
            synchroPython(bindIds, ToolCategoryEnum.GRID, ToolOperEnum.ADD, dto.getUpdaterId());
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("修改网格工具异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    private GridTool initUpdateGridTool(BaseGridTool data) {
        GridTool tool = new GridTool();
        BeanUtils.copyProperties(data, tool);
        tool.setBasePrice(new BigDecimal(data.getBasePrice()));
        tool.setDifferencePrice(new BigDecimal(data.getDifferencePrice()));
        tool.setLowerPrice(new BigDecimal(data.getLowerPrice()));
        tool.setUpperPrice(new BigDecimal(data.getUpperPrice()));
        tool.setUpdateTime(new Date());
        tool.setStatus(ToolStatusEnum.NORMAL.getStatus());
        return tool;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateSellingTool(UpdateSellingToolDTO dto) {
        log.info("修改卖点工具入参={}", JSON.toJSONString(dto));
        final Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        try {
            String errorStr = dto.validateForm();
            if (StringUtils.isBlank(errorStr)) {
                errorStr = ClassUtil.checkRequest(dto.getData());
            }
            if (StringUtils.isNotBlank(errorStr)) {
                result.setMessage(errorStr);
                return result;
            }
            Integer toolId = checkToolId(ToolCategoryEnum.SELLING, dto.getToolId());
            if (null == toolId) {
                result.setMessage("工具数据不存在");
                return result;
            }
            // 校验是否是客户申请的工具
            ToolBindRecord bindRecord = new ToolBindRecord();
            bindRecord.setToolCategoryCode(ToolCategoryEnum.SELLING.getCode());
            bindRecord.setToolId(toolId);
            bindRecord = toolBindRecordMapper.selectOne(bindRecord);
            if (null == bindRecord) {
                result.setMessage("只能修改客户申请的工具");
                return result;
            }
            // 得到修改的信息
            SellingTool tool = initUpdateSellingTool(dto.getData());
            tool.setId(toolId);
            tool.setUpdaterId(dto.getUpdaterId());
            sellingToolMapper.updateByPrimaryKeySelective(tool);

            List<Integer> bindIds = new ArrayList<>();
            bindIds.add(bindRecord.getId());
            synchroPython(bindIds, ToolCategoryEnum.SELLING, ToolOperEnum.ADD, dto.getUpdaterId());
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("修改卖点工具异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    private SellingTool initUpdateSellingTool(BaseSellingDTO data) {
        SellingTool tool = new SellingTool();
        BeanUtils.copyProperties(data, tool);
        tool.setTopDeviate(Double.valueOf(data.getTopDeviate()));
        tool.setLowerDeviate(Double.valueOf(data.getLowerDeviate()));
        tool.setStatus(ToolStatusEnum.NORMAL.getStatus());
        tool.setUpdateTime(new Date());
        return tool;
    }

    private Integer checkToolId(ToolCategoryEnum type, Integer toolId) {
        if (type == ToolCategoryEnum.GRID) {
            GridTool gidTool = gridToolMapper.selectByPrimaryKey(toolId);
            if (null != gidTool && ToolStatusEnum.NOT_DELETED.getStatus().equals(gidTool.getIsDelete())) {
                return gidTool.getId();
            }
        } else if (type == ToolCategoryEnum.SELLING) {
            SellingTool tool = sellingToolMapper.selectByPrimaryKey(toolId);
            if (null != tool && ToolStatusEnum.NOT_DELETED.getStatus().equals(tool.getIsDelete())) {
                return tool.getId();
            }
        }
        return null;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateTrendTool(UpdateTrendToolDTO dto) {
        log.info("修改趋势化工具入参={}", JSON.toJSONString(dto));
        final Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        try {
            String errorStr = dto.validateForm();
            if (StringUtils.isBlank(errorStr)) {
                errorStr = ClassUtil.checkRequest(dto.getData());
            }
            if (StringUtils.isNotBlank(errorStr)) {
                result.setMessage(errorStr);
                return result;
            }
            if (checkToolIsExists(ToolCategoryEnum.TREND.getCode(), dto.getToolId().toString())) {
                TrendTool tool = new TrendTool();
                TrendToolDTO data = dto.getData();
                BeanUtils.copyProperties(data, tool);
                tool.setId(dto.getToolId());
                tool.setStatus(ToolStatusEnum.NORMAL.getStatus());
                tool.setUpdaterId(dto.getUpdaterId());
                tool.setUpdateTime(new Date());
                trendToolMapper.updateByPrimaryKeySelective(tool);
                return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
            }
            return result;
        } catch (Exception e) {
            log.error("修改趋势化工具异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteTool(String toolCode, List<Integer> toolIds, List<Integer> toolBindIds, Integer operId) {
        log.info("删除工具入参toolCode={},toolIds={},toolBindIds={},operId={}", toolCode, toolIds, toolBindIds, operId);
        final Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        try {
            if ((null == toolIds || toolIds.isEmpty()) && (toolBindIds == null || toolBindIds.isEmpty())) {
                result.setMessage("需要删除的工具ID不能为空");
                return result;
            } else if (StringUtils.isBlank(toolCode)) {
                result.setMessage("工具类别编码不能为空");
                return result;
            } else if (null == operId) {
                result.setMessage("操作人ID不能为空");
                return result;
            }
            final ToolCategoryEnum type = ToolCategoryEnum.getToolCategory(toolCode);
            // 自己申请待确定的数据
            List<Integer> ownApplys = null;
            // 删除未绑定的工具数据(包含自己申请的但是未完善条件的)
            if (CollectionUtils.isNotEmpty(toolIds)) {
                switch (type) {
                    case GRID:
                        ownApplys = gridToolMapper.selectBindByOwnApplyGridIds(toolIds);
                        gridToolMapper.updateByIds(toolIds, operId, ToolStatusEnum.UNDELETED.getStatus());
                        break;
                    case SELLING:
                        ownApplys = sellingToolMapper.selectBindByOwnApplySellingIds(toolIds);
                        sellingToolMapper.updateByIds(toolIds, operId, ToolStatusEnum.UNDELETED.getStatus());
                        break;
                    case TREND:
                        ownApplys = trendToolMapper.selectBindByOwnApplyTrendIds(toolIds);
                        trendToolMapper.updateByIds(toolIds, operId, ToolStatusEnum.UNDELETED.getStatus());
                        break;
                    default:
                        break;
                }
            }
            // 此处针对自己申请的工具删除，做了特别的处理、自己申请的工具属于已经绑定的但是没有完善条件,同步删除只针对正常推荐绑定过的工具
            // synDelList 用于同步删除pyhton接口使用
            List<Integer> synDelList = null;
            if (CollectionUtils.isNotEmpty(toolBindIds)) {
                synDelList = new ArrayList<>(toolBindIds);
            }
            // 删除处理自己申请的
            if (CollectionUtils.isNotEmpty(ownApplys)) {
                if (null == toolBindIds) {
                    toolBindIds = new ArrayList<>();
                }
                toolBindIds.addAll(ownApplys);
            }
            // 删除已绑定的工具数据：toolBindIds用于本地删除（包含自己申请待确定的数据）
            if (CollectionUtils.isNotEmpty(toolBindIds)) {
                toolBindRecordMapper.updateByIds(toolBindIds, ToolStatusEnum.UNDELETED.getStatus());
            }
            // 异步调用python删除
            if (CollectionUtils.isNotEmpty(synDelList)) {
                synchroPython(synDelList, type, ToolOperEnum.DELETE, operId);
            }
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("删除工具异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result getPcToolsByPage(QueryToolDTO dto, PageBean pageBean) {
        log.info("查询工具入参={},pageBean={}", JSON.toJSONString(dto), JSON.toJSONString(pageBean));
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            String errorStr = dto.validateForm();
            if (StringUtils.isNotBlank(errorStr)) {
                result.setMessage(errorStr);
                return result;
            }
            if (null == pageBean) {
                pageBean = new PageBean();
            }
            // 处理排序字段
            dto = handleSortField(dto);
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<? extends BaseToolVO> list = null;
            final ToolCategoryEnum type = ToolCategoryEnum.getToolCategory(dto.getToolCode());
            switch (type) {
                case GRID:
                    list = gridToolMapper.selectGridTools(dto);
                    break;
                case SELLING:
                    list = sellingToolMapper.selectSellingTools(dto);
                    break;
                case TREND:
                    Byte toolType = dto.getToolType();
                    if (null == toolType) {
                        result.setMessage("工具类型不能为空");
                        return result;
                    }
                    toolType = ToolTypeEnum.type(toolType);
                    dto.setToolType(toolType);
                    list = trendToolMapper.selectTrendTools(dto);
                    break;
                default:
                    log.warn("查询工具数据，类型编码不存在");
                    break;
            }
            if (CollectionUtils.isNotEmpty(list)) {
                PageInfo<? extends BaseToolVO> pageInfo = new PageInfo<>(list);
                // 得到数据库查询的数据
                final List<? extends BaseToolVO> vlist = pageInfo.getList();
                // 实际返回结果列表数据
                final List<ToolVO> resultList = new ArrayList<ToolVO>();
                // 绑定的工具唯一标识、用于获取实时数据
                final List<String> identyIds = new ArrayList<String>();
                // 编码map,用于处理未绑定的工具，获取股票实时数据
                final Map<String, String> codeMap = new HashMap<>();
                ToolVO vo;
                for (Object obj : vlist) {
                    vo = new ToolVO();
                    BeanUtils.copyProperties(obj, vo);
                    if (null != vo.getBindId() && ToolStatusEnum.NORMAL.getStatus().equals(vo.getStatus())) {
                        vo.setToolIdenty(ToolUtils.getToolBindKey(vo.getBindId()));
                        identyIds.add(vo.getToolIdenty());
                    } else {
                        // 未绑定、未推荐的数据
                        vo.setToolIdenty(ToolUtils.getToolKey(vo.getId()));
                        codeMap.put(vo.getToolIdenty(), vo.getStockCode());
                    }
                    resultList.add(vo);
                }
                // 处理实时数据
                handlerTool(type, codeMap, identyIds, resultList, dto.getToolType());
                result.setData(resultList);
                result.setCount(pageInfo.getTotal());
            } else {
                result.setData(new ArrayList<>());
            }
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("查询工具异常", e);
        }
        return result;
    }

    /**
     * @return QueryToolDTO
     * @throws
     * @Title: handleSortField
     * @Description: 处理排序字段
     * @param: @param dto
     * @param: @return
     * @author lijie
     */
    private QueryToolDTO handleSortField(QueryToolDTO dto) {
        if (null != dto) {
            // 排序字段
            final String sortField = dto.getSortField();
            // 排序类型
            final String type = dto.getOrderType();
            dto.setSortField(ToolSortEnum.field(sortField));
            if (StringUtils.isBlank(sortField) || "null".equals(sortField)) {
                dto.setOrderType(OrderByTypeEnum.DESC.getType());
            }
            if (StringUtils.isBlank(type)) {
                if (ToolSortEnum.REALNAME.getField().equals(sortField)
                        || ToolSortEnum.STOCKNAME.getField().equals(sortField)) {

                    dto.setOrderType(OrderByTypeEnum.ASC.getType());
                }
            }
        }
        return dto;
    }

    /**
     * @param @param  type
     * @param @param  codeMap
     * @param @param  identyIds
     * @param @param  rList
     * @param @return 参数
     * @return List<ToolVO>    返回类型
     * @throws
     * @Title: handlerTool
     * @Description:获取实时计算的数据
     */
    private List<ToolVO> handlerTool(ToolCategoryEnum type, final Map<String, String> codeMap,
                                     final List<String> identyIds, final List<ToolVO> rList, final Byte toolType) {
        // 处理绑定的工具信息
        Map<String, ToolCacheVO> cacheInfo = queryCacheToolsByToolBindIds(identyIds, type, toolType);
        if (null != cacheInfo) {
            ToolCacheVO tc;
            for (ToolVO v : rList) {
                tc = cacheInfo.get(v.getToolIdenty());
                if (null == tc) {
                    continue;
                }
                v.setLatestPrice(tc.getLatestPrice());
                v.setDirection(tc.getDirection());
            }
        }
        // 处理未绑定的工具信息
        if (null != codeMap && !codeMap.isEmpty()) {
            Map<String, String> latestPrice = queryCodePrice(codeMap);
            for (ToolVO v : rList) {
                if (null != latestPrice.get(v.getToolIdenty())) {
                    v.setLatestPrice(latestPrice.get(v.getToolIdenty()));
                }
            }
        }
        return rList;
    }

    /**
     * @param @param  identyIds
     * @param @param  type
     * @param @return 参数
     * @return List<ToolCacheVO>    返回类型
     * @throws
     * @Title: getCacheTools
     * @Description:在缓存里面获取工具信息
     */
    @SuppressWarnings("unchecked")
    private Map<String, ToolCacheVO> queryCacheToolsByToolBindIds(List<String> identyIds, ToolCategoryEnum type,
                                                                  final Byte toolType) {
        Map<String, ToolCacheVO> map = null;
        Result result = toolCacheService.getToolBindRealTimeDatas(type, identyIds, toolType);
        log.info("缓存获取计算后的工具数据结果={}", JSON.toJSONString(result));
        if (result.isSuccess()) {
            map = (Map<String, ToolCacheVO>) result.getData();
        }
        return map;
    }

    /**
     * @param @param  map
     * @param @return 参数
     * @return
     * @throws
     * @Title: queryCodePrice
     * @Description: 根据未绑定的工具, 查询
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> queryCodePrice(Map<String, String> map) {
        Map<String, String> rmap = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.addAll(map.values());
        Result result = toolCacheService.getNewPriceByCode(list);
        log.info("根据code获取最新价返回数据={}", JSON.toJSONString(result));
        if (result.isSuccess()) {
            Map<String, String> data = (Map<String, String>) result.getData();
            if (null != data) {
                for (Map.Entry<String, String> me : map.entrySet()) {
                    rmap.put(me.getKey(), data.get(me.getValue()));
                }
            }
        }
        return rmap;
    }

    @Override
    public Result getBindTools(Integer managerId, Integer customerId, PageBean pageBean) {
        log.info("查询绑定的工具列表入参={},customerId={},pageBean={}", managerId, customerId, JSON.toJSONString(pageBean));
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (null == managerId) {
                result.setMessage("客户经理不能为空");
                return result;
            }
            if (null == pageBean) {
                pageBean = new PageBean();
            }
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<ToolBindVO> list = toolBindRecordMapper.selectToolBinds(managerId, customerId);
            PageInfo<ToolBindVO> pageInfo = new PageInfo<ToolBindVO>(list);
            List<ToolBindVO> rlist = pageInfo.getList();
            for (ToolBindVO vo : rlist) {
                if (null != vo.getBindId()) {
                    vo.setToolIdenty(ToolUtils.getToolBindKey(vo.getBindId()));
                }
            }
            result.setData(rlist);
            result.setCount(pageInfo.getTotal());
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("查询绑定的工具列表异常", e);
        }
        return result;
    }

    @Override
    public Result getBindToolInfo(Integer bindId, String categoryCode, PageBean pageBean) {
        log.info("查询绑定的工具详情入参={},categoryCode={}", bindId, categoryCode);
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (null == bindId) {
                result.setMessage("推荐ID不能为空");
                return result;
            }
            if (StringUtils.isBlank(categoryCode)) {
                result.setMessage("工具类别编码不能为空");
                return result;
            }
            if (null == pageBean) {
                pageBean = new PageBean();
            }
            ToolBindInfoVO info = toolBindRecordMapper.selectToolBindInfo(bindId, categoryCode);
            if (null != info) {
                PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
                List<ToolPushVO> list = toolPushMapper.selectToolPushs(bindId);
                PageInfo<ToolPushVO> pageInfo = new PageInfo<ToolPushVO>(list);
                result.setCount(pageInfo.getTotal());
                info.setPushs(pageInfo.getList());
            }
            result.setData(info);
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("查询绑定的工具详情异常", e);
        }
        return result;
    }

    @Override
    public Result getPushsByPage(Integer bindId, PageBean pageBean) {
        log.info("查询单个工具推送列表入参={},pageBean={}", bindId, JSON.toJSONString(pageBean));
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (null == bindId) {
                result.setMessage("推荐ID不能为空");
                return result;
            }
            if (null == pageBean) {
                pageBean = new PageBean();
            }
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<ToolPushVO> list = toolPushMapper.selectToolPushs(bindId);
            PageInfo<ToolPushVO> pageInfo = new PageInfo<ToolPushVO>(list);
            result.setCount(pageInfo.getTotal());
            result.setData(pageInfo.getList());
            return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("查询单个工具推送列表异常", e);
        }
        return result;
    }

    @Override
    public Result toolPush(ToolPushDTO dto) {
        log.info("工具推送入参={}", JSON.toJSONString(dto));
        Result result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
        String errorStr = dto.validateForm();
        if (StringUtils.isNotBlank(errorStr)) {
            result.setMessage(errorStr);
            return result;
        }
        ToolBindUserInfoVO toolBindUserInfo = null;
        Integer bindId = ToolUtils.getToolIdByBind(dto.getToolIdenty());
        if (null != bindId) {
            // 此处只查询了趋势量化工具的工具类型（toolType：常规/专用），如后续需要添加其它工具则需要扩展
            toolBindUserInfo = toolBindRecordMapper.selectCustomerOpenId(bindId);
        }
        if (null == toolBindUserInfo) {
            log.warn("工具标识：" + dto.getToolIdenty() + ",信息不存在");
            result.setMessage("工具不存在");
            return result;
        }
        if (StringUtils.isBlank(toolBindUserInfo.getOpenId())) {
            result.setMessage("用户未绑定微信");
            return result;
        }
        // 得到推送类型
        ToolTypeEnum toolType = ToolTypeEnum.ROUTINE;
        if (null != toolBindUserInfo.getToolType()) {
            toolType = ToolTypeEnum.typeEnum(toolBindUserInfo.getToolType());
        }
        // 得到工具类别
        final ToolCategoryEnum type = ToolCategoryEnum.getToolCategory(toolBindUserInfo.getToolCategoryCode());
        // 趋势量化工具并且是专用工具时，将持续看多当作看多处理
        if (ToolCategoryEnum.TREND == type && toolType == ToolTypeEnum.SPECIAL_PURPOSE) {
            if (Pushdirection.CONTINUED_PURCHASE.getDirection().equals(dto.getDirection())) {
                dto.setDirection(Pushdirection.PURCHASE.getDirection());
            }
        }
        final ToolPushTacticsContext tacticsContext = new ToolPushTacticsContext(toolType);
        // 封装模板生成请求数据
        ToolTemplateData data = new ToolTemplateData();
        data.setDirection(dto.getDirection());
        data.setOpenId(toolBindUserInfo.getOpenId());
        data.setPrice(dto.getPrice());
        data.setStockCode(dto.getStockCode());
        data.setStockName(dto.getStockName());
        data.setUserName(toolBindUserInfo.getUserName());
        data.setToolType(toolType);
        // 得到微信模板数据并执行推送
        ToolPushData pushData = new ToolPushData();
        pushData.setBindId(bindId);
        pushData.setDto(dto);
        pushData.setTemplate(pushTemplateFactory.createTemplate(type).createWechatPushTemplate(data));
        pushData.setType(type);
        result = tacticsContext.push(pushData);
        log.info("工具推送结果={}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public Result timingUpdateToolSynchro() {
        log.info("定时修改工具未同步到python的数据start");
        try {
            ToolBindRecord record = new ToolBindRecord();
            record.setIsDelete(ToolStatusEnum.NOT_DELETED.getStatus());
            record.setIsSynchro(ToolStatusEnum.NOT_SYNCHRO.getStatus());
            List<ToolBindRecord> list = toolBindRecordMapper.select(record);
            // 处理同步新增
            if (CollectionUtils.isNotEmpty(list)) {
                Map<String, List<Integer>> addMap = new HashMap<>();
                List<Integer> bindIds;
                for (ToolBindRecord b : list) {
                    bindIds = addMap.get(b.getToolCategoryCode());
                    if (null == bindIds) {
                        bindIds = new ArrayList<Integer>();
                        addMap.put(b.getToolCategoryCode(), bindIds);
                    }
                    bindIds.add(b.getId());
                }
                for (Map.Entry<String, List<Integer>> add : addMap.entrySet()) {
                    synchroPython(add.getValue(), ToolCategoryEnum.getToolCategory(add.getKey()), ToolOperEnum.ADD, 0);
                }
            }
            // 处理删除
            record = new ToolBindRecord();
            record.setIsDelete(ToolStatusEnum.UNDELETED.getStatus());
            record.setIsSynchroDelete(ToolStatusEnum.NOT_DELETED.getStatus());
            list = toolBindRecordMapper.select(record);
            if (CollectionUtils.isNotEmpty(list)) {
                List<Integer> bindIds;
                Map<String, List<Integer>> delMap = new HashMap<>();
                for (ToolBindRecord b : list) {
                    bindIds = delMap.get(b.getToolCategoryCode());
                    if (null == bindIds) {
                        bindIds = new ArrayList<Integer>();
                        delMap.put(b.getToolCategoryCode(), bindIds);
                    }
                    bindIds.add(b.getId());
                }
                for (Map.Entry<String, List<Integer>> del : delMap.entrySet()) {
                    synchroPython(del.getValue(), ToolCategoryEnum.getToolCategory(del.getKey()), ToolOperEnum.DELETE,
                            0);
                }
            }
            return ResultUtil.getResult(RespCode.Code.SUCCESS);
        } catch (Exception e) {
            log.error("定时修改工具未同步到python的数据异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Result getStockInfo(String stockCode) {
        log.info("根据股票代码获取股票信息入参={}", stockCode);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            if (StringUtils.isBlank(stockCode)) {
                result.setMessage("股票代码不能为空");
                return result;
            }
            result = toolCacheService.getCacheStockInfo(stockCode);
            if (result.isSuccess()) {
                StockInfoVO info = result.getData(StockInfoVO.class);
                result.setData(info.getName());
            }
        } catch (Exception e) {
            log.error("根据股票代码获取股票信息异常", e);
        }
        return result;
    }

    @Override
    public Result getApplyToolSurplusNum(Integer id, String toolCategoryCode) {
        log.info("获取客户工具数量入参={},{}", id, toolCategoryCode);
        try {
            ToolNumberVO vo = new ToolNumberVO();
            vo.setMaxNumber(0);
            vo.setSurplusNumber(0);
            List<Integer> list = toolBindRecordMapper.selectToolNumber(id, toolCategoryCode);
            if (CollectionUtils.isNotEmpty(list) && list.size() == 2) {
                vo.setMaxNumber(list.get(0));
                int num = list.get(0) - list.get(1);
                vo.setSurplusNumber(num < 0 ? 0 : num);
            }
            return ResultUtil.getResult(RespCode.Code.SUCCESS, vo);
        } catch (Exception e) {
            log.error("获取客户工具数量异常", e);
            return ResultUtil.getResult(RespCode.Code.FAIL);
        }
    }

    @Deprecated
    @Override
    public Result synchroTrendToolToRedis() {
        log.info("同步绑定趋势量化工具数据到redis -- start --");
        final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            ToolBindRecord record = new ToolBindRecord();
            record.setIsDelete(ToolStatusEnum.NOT_DELETED.getStatus());
            record.setIsPush(ToolStatusEnum.PUSH.getStatus());
            record.setToolCategoryCode(ToolCategoryEnum.TREND.getCode());
            List<ToolBindRecord> list = toolBindRecordMapper.select(record);
            if (CollectionUtils.isNotEmpty(list)) {
                Map<String, String> tools = new HashMap<String, String>();
                List<Object> values;
                JSONArray array = new JSONArray();
                ToolBindInfoVO info;
                final List<Integer> successs = new ArrayList<>();
                for (ToolBindRecord rinfo : list) {
                    info = toolBindRecordMapper.selectToolBindInfo(rinfo.getId(), ToolCategoryEnum.TREND.getCode());
                    if (null == info) {
                        continue;
                    }
                    values = new ArrayList<>();
                    String toolIdenty = ToolUtils.getToolBindKey(rinfo.getId());
                    values.add(info.getStockCode());
                    JSONObject add = new JSONObject();
                    values.add(toolIdenty);
                    values.add(info.getStockName());
                    values.add("0");
                    values.add("_");
                    values.add("0");
                    add.put("code", info.getStockCode());
                    add.put("toolIdenty", toolIdenty);
                    add.put("name", info.getStockName());
                    array.add(add);
                    tools.put(toolIdenty, JSON.toJSONString(values));
                    successs.add(rinfo.getId());
                }
                log.info("同步绑定趋势量化工具数据到redis,新增缓存数据tools={}", tools);
                Result insertResult = toolCacheService.addToolBaseDatas(ToolCategoryEnum.TREND, tools);
                log.info("同步绑定趋势量化工具数据到redis,返回信息={}", JSON.toJSONString(insertResult));
                if (insertResult.isSuccess()) {
                    affairService.updateToolIsSynchro(successs);
                    // 触发计算
                    JSONObject tadd = new JSONObject();
                    tadd.put("aqTools", array);
                    send(PYTHON_CONFIG.getAddTrendQOneItem(), tadd.toJSONString());
                    return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
                }
            }
        } catch (Exception e) {
            log.error("同步绑定趋势量化工具数据到redis异常", e);
            throw new ToolException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    /**
     * @auth: zhangxia
     * @desc: 定期计算工具收益率
     * @methodName: timingCountToolIncomeRate
     * @params: []
     * @return: com.aq.util.result.Result
     * @createTime: 2018/4/26 11:15
     * @version:2.1.6
     */
    @Override
    public Result timingCountToolIncomeRate(boolean isHistory) throws ParseException {
        log.info("定期计算工具收益率 service入参参数isHistory={}",isHistory);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        try {
            List<ToolIncomeRate> toolIncomeRates = new ArrayList<>();//需要插入的数据量
            //先查出所有的工具
            TrendTool trendTool = new TrendTool();
            trendTool.setToolType(ToolTypeEnum.SPECIAL_PURPOSE.getType());
            List<TrendTool> trendTools = trendToolMapper.select(trendTool);
            for (TrendTool tool : trendTools) {
                //进一步查出所有推荐的人
                ToolBindRecord toolBind = new ToolBindRecord();
                toolBind.setToolId(tool.getId());
                toolBind.setToolCategoryCode(ToolCategoryEnum.TREND.getCode());
                List<ToolBindRecord> toolBindRecordList = toolBindRecordMapper.select(toolBind);
                for (ToolBindRecord toolBindRecord : toolBindRecordList) {
                    //查找推送的记录
                    List<ToolPushVO> toolPushVOS = toolPushMapper.selectToolPushs(toolBindRecord.getId());
                    if (toolPushVOS.size() < 2) {
                        continue;
                    }
                    ToolPushVO vo1;
                    ToolPushVO vo2;
                    //对历史记录进行处理和计算--star
                    if (isHistory) {
                        for (int i = toolPushVOS.size() - 2; i >= 0; i--) {
                            vo1 = toolPushVOS.get(i + 1);  //前一天的推送记录（是否为买）
                            vo2 = toolPushVOS.get(i);    //当前天的推送记录（是否为卖）
                            if (0 == Integer.parseInt(vo2.getDirection().toString()) && 1 == Integer.parseInt(vo1.getDirection().toString())) {
                                //只有当天为看空，且前一天为看多的情况才进行计算
                                BigDecimal rate = new BigDecimal(vo2.getTranPrice()).divide(new BigDecimal(vo1.getTranPrice()),4,BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1"));
                                log.info("历史方式--需要计算当天的看空的价={}；前一天的看多的价={}，计算后的收益比例={}", vo1.getTranPrice(), vo2.getTranPrice(), rate.toString());
                                ToolIncomeRate incomeRate = new ToolIncomeRate();
                                incomeRate.setBuyPushId(vo1.getId());
                                incomeRate.setToBuyDate(DateUtil.format(vo1.getCreateTime(), DateUtil.genFormatter(DateUtil.YYYYMMDD)));
                                incomeRate.setSellPushId(vo2.getId());
                                incomeRate.setToSellDate(DateUtil.format(vo2.getCreateTime(), DateUtil.genFormatter(DateUtil.YYYYMMDD)));
                                incomeRate.setIncomeRate(rate.doubleValue());
                                incomeRate.setStockCode(tool.getStockCode());
                                incomeRate.setUserId(toolBindRecord.getUserId());
                                incomeRate.setUserType(toolBindRecord.getUserType());
                                incomeRate.setBindId(toolBindRecord.getId());
                                incomeRate.setToolId(tool.getId());
                                log.info("历史需要计算的记录后为：{}", JSON.toJSONString(incomeRate));
                                toolIncomeRates.add(incomeRate);
                            } else {
                                continue;
                            }
                        }
                    }
                    //对历史记录进行处理和计算--end

                    //以后今日数据进行计算--star
                    if (!isHistory) {
                        vo1 = toolPushVOS.get(1);//昨天的推送记录
                        vo2 = toolPushVOS.get(0);//今天的推送记录
                        if (0 == Integer.parseInt(vo2.getDirection().toString())
                                && 1 == Integer.parseInt(vo1.getDirection().toString())
                                && vo2.getCreateTime().compareTo(new Date())<0) {
                            //只有当天为看空，且前一天为看多的情况才进行计算
                            BigDecimal rate = new BigDecimal(vo2.getTranPrice()).divide(new BigDecimal(vo1.getTranPrice()),4,BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1"));
                            log.info("今日方式--需要计算当天的看空的价={}；前一天的看多的价={}，计算后的收益比例={}", vo1.getTranPrice(), vo2.getTranPrice(), rate.toString());
                            ToolIncomeRate incomeRate = new ToolIncomeRate();
                            incomeRate.setBuyPushId(vo1.getId());
                            incomeRate.setToBuyDate(DateUtil.format(vo1.getCreateTime(), DateUtil.genFormatter(DateUtil.YYYYMMDD)));
                            incomeRate.setSellPushId(vo2.getId());
                            incomeRate.setToSellDate(DateUtil.format(vo2.getCreateTime(), DateUtil.genFormatter(DateUtil.YYYYMMDD)));
                            incomeRate.setIncomeRate(rate.doubleValue());
                            incomeRate.setStockCode(tool.getStockCode());
                            incomeRate.setUserId(toolBindRecord.getUserId());
                            incomeRate.setUserType(toolBindRecord.getUserType());
                            incomeRate.setBindId(toolBindRecord.getId());
                            incomeRate.setToolId(tool.getId());
                            log.info("当天需要计算的记录后为：{}", JSON.toJSONString(incomeRate));
                            toolIncomeRates.add(incomeRate);
                        }
                    }
                    //以后今日数据进行计算--end
                }
            }
            //进行插入数据
            log.info("进行插入的数据条数为：{}，toolIncomeRates={}",toolIncomeRates.size(),JSON.toJSONString(toolIncomeRates));
            if (toolIncomeRates.size()>0){
                int i = toolIncomeRateMapper.insertList(toolIncomeRates);
                log.info("定期计算工具收益率 插入成功条数={}",i);
            }else {
                log.info("定期计算工具收益率，没有需要插入的数据");
            }

        } catch (Exception e) {
            log.info("定期计算工具收益率异常e={}",e);
            result.setMessage("定期计算工具收益率异常");
        }
        log.info("定期计算工具收益率  处理结果result={}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public Result getSpecialToolInfo(ToolSpecialDTO toolSpecialDTO, PageBean pageBean) {
        log.info("专用趋势化工具详情 toolSpecialDTO={}", JSON.toJSONString(toolSpecialDTO));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            String message = toolSpecialDTO.validateForm();
            if (StringUtils.isNotBlank(message)) {
                result.setMessage(message);
                return result;
            }
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<ToolSpecialVO> qlist = toolIncomeRateMapper.selectToolSpecial(toolSpecialDTO);
            PageInfo<ToolSpecialVO> pageInfo = new PageInfo<>(qlist);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
            log.info("专用趋势化工具详情 result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("专用趋势化工具详情 异常", e);
            return ResultUtil.getResult(RespCode.Code.FAIL);
        }
    }
}
