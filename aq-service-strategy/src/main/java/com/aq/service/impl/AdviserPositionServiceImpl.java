package com.aq.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.facade.contant.AdviserQueryEnum;
import com.aq.facade.entity.Adviser;
import com.aq.facade.entity.AdviserPosition;
import com.aq.facade.service.IAdviserPositionService;
import com.aq.facade.vo.AdviserBaseVO;
import com.aq.facade.vo.AdviserPositionAndTradeVO;
import com.aq.facade.vo.AdviserPositionBaseVO;
import com.aq.facade.vo.AdviserPositionVO;
import com.aq.facade.vo.AdviserTradeRecordVO;
import com.aq.facade.vo.RealTimeAdviserVO;
import com.aq.mapper.AdviserMapper;
import com.aq.mapper.AdviserPositionMapper;
import com.aq.util.container.ClassUtil;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.aq.util.transmoney.MoneyTransUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: AdviserPositionServiceImpl
 * @Description: 投顾当前持仓服务实现
 * @author: lijie
 * @date: 2018年3月9日 下午3:21:53
 */
@Slf4j
@Service(version = "1.0.0")
public class AdviserPositionServiceImpl implements IAdviserPositionService {
    /**
     * 投顾当前持仓mapper
     */
    @Autowired
    private AdviserPositionMapper adviserPositionMapper;

    @Autowired
    private AdviserPythonService adviserPythonService;

    @Autowired
    private AdviserMapper adviserMapper;

    @Override
    public Result getAdviserPositionByPage(Byte isQueryTrade, Integer adviserId, PageBean pageBean) {
        log.info("查询投顾当前持仓列表数据入参isQueryTrade={},adviserId={},pageBean={}", isQueryTrade, adviserId,
                JSON.toJSONString(pageBean));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        if (null == isQueryTrade) {
            result.setMessage("查询类型不能为空");
            return result;
        }
        if (null == adviserId) {
            result.setMessage("投顾ID不能为空");
            return result;
        }
		if (null == pageBean) {
			pageBean = new PageBean();
		}
		try {
			PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
			List<AdviserPosition> list = adviserPositionMapper.selectAdviserPositionByPage(adviserId);
			PageInfo<AdviserPosition> pageInfo = new PageInfo<>(list);
			// 封装返回值
			AdviserPositionAndTradeVO rvo = new AdviserPositionAndTradeVO();
			BeanUtils.copyProperties(initAdviserPositionAndTrade(), rvo);
			if (AdviserQueryEnum.QUERY_TRADE.getState().equals(isQueryTrade)) {
				// 从缓存获取数据
				AdviserTradeRecordVO trade = adviserPythonService.getAdviserTradeRecord(adviserId);
				if (null != trade) {
					rvo.setAvailableAssets(MoneyTransUtil.strToBdScale(trade.getAvailableAssets()));
					rvo.setMarketValue(MoneyTransUtil.strToBdScale(trade.getMarketValue()));
					rvo.setTotalAssets(MoneyTransUtil.strToBdScale(trade.getTotalAssets()));
					rvo.setReferenceProfit(
							MoneyTransUtil.subMoneyScale(trade.getTotalAssets(), trade.getInitTotalPrice()));
					rvo.setTodayRate(MoneyTransUtil.strToBdScale(trade.getTodayRate()));
					rvo.setTotalFreezeNum(MoneyTransUtil.strToBdScale(trade.getTotalFreezeNum()));
				} else {
					// 缓存没有获取到数据则在数据库获取
					Adviser info = adviserMapper.selectByPrimaryKey(adviserId);
					if (null != info) {
						rvo.setTotalAssets(info.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
						rvo.setAvailableAssets(info.getTotalPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					}
				}
			}
			rvo.setPositions(handleAdviserPosition(pageInfo.getList()));
			// 改变默认值
			ClassUtil.setDefvalue(rvo);
			result.setData(rvo);
			result.setCount(pageInfo.getTotal());
			ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("查询投顾当前持仓列表数据异常", e);
		}
		return result;
	}

	private AdviserBaseVO initAdviserPositionAndTrade() {
		AdviserBaseVO result = new AdviserBaseVO();
		result.setAvailableAssets("0.00");
		result.setMarketValue("0.00");
		result.setTotalAssets("0.00");
		result.setReferenceProfit("0.00");
		result.setTodayRate("0.00");
		result.setTotalFreezeNum("0.00");
		return result;
	}

    /**
     * @param @param  list
     * @param @return 参数
     * @return List<AdviserPositionVO>    返回类型
     * @throws
     * @Title: handleAdviserPosition
     * @Description: 处理投顾持仓数据
     */
	private List<AdviserPositionVO> handleAdviserPosition(List<AdviserPosition> list) {
		List<AdviserPositionVO> result = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			AdviserPositionVO apv;
			AdviserPositionVO cache;
			for (AdviserPosition ap : list) {
				apv = new AdviserPositionVO();
				BeanUtils.copyProperties(ap, apv);
				cache = adviserPythonService.getAdviserPositions(ap.getAdviserId(), ap.getSharesCode());
				if (null != cache) {
					apv.setFreezeNum(cache.getFreezeNum());
					if (StringUtils.isNotBlank(cache.getReferenceCost())) {
						apv.setReferenceCost(new BigDecimal(cache.getReferenceCost())
								.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					}
					if (StringUtils.isNotBlank(cache.getProfitRatio())) {
						apv.setProfitRatio(new BigDecimal(cache.getProfitRatio()).setScale(2, BigDecimal.ROUND_HALF_UP)
								.toString());
					}
					if (StringUtils.isNotBlank(cache.getReferenceProfit())) {
						apv.setReferenceProfit(new BigDecimal(cache.getReferenceProfit())
								.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
					}
				}
				// 改变默认值
				ClassUtil.setDefvalue(apv, "id");
				result.add(apv);
			}
		}
		return result;
	}

	@Override
	public Result getRealTimeAdviserPosition(Integer adviserId, List<String> codes) {
		log.info("查询投顾当前持仓列表实时数据入参adviserId={},codes={}", adviserId, codes);
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			final RealTimeAdviserVO rvo = new RealTimeAdviserVO();
			BeanUtils.copyProperties(initAdviserPositionAndTrade(), rvo);
			// 从缓存获取总的相关数据
			AdviserTradeRecordVO trade = adviserPythonService.getAdviserTradeRecord(adviserId);
			if (null != trade) {
				rvo.setAvailableAssets(MoneyTransUtil.strToBdScale(trade.getAvailableAssets()));
				rvo.setMarketValue(MoneyTransUtil.strToBdScale(trade.getMarketValue()));
				rvo.setTotalAssets(MoneyTransUtil.strToBdScale(trade.getTotalAssets()));
				rvo.setReferenceProfit(MoneyTransUtil.subMoneyScale(trade.getTotalAssets(), trade.getInitTotalPrice()));
				rvo.setTodayRate(MoneyTransUtil.strToBdScale(trade.getTodayRate()));
				rvo.setTotalFreezeNum(MoneyTransUtil.strToBdScale(trade.getTotalFreezeNum()));
			}
			// 获取列表(投顾持仓)实时数据
			if (CollectionUtils.isNotEmpty(codes)) {
				final List<String> keys = new ArrayList<>();
				for (int i = 0; i < codes.size(); i++) {
					keys.add(AdviserPythonService.getAdviserKey(adviserId, codes.get(i)) );
				}
				final List<AdviserPositionBaseVO> list = adviserPythonService.getAdviserPositions(keys);
				if (CollectionUtils.isNotEmpty(list)) {
					for (AdviserPositionBaseVO v : list) {
						if (StringUtils.isNotBlank(v.getReferenceCost())) {
							v.setReferenceCost(new BigDecimal(v.getReferenceCost())
									.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
						}
						if (StringUtils.isNotBlank(v.getProfitRatio())) {
							v.setProfitRatio(new BigDecimal(v.getProfitRatio()).setScale(2, BigDecimal.ROUND_HALF_UP)
									.toString());
						}
						if (StringUtils.isNotBlank(v.getReferenceProfit())) {
							v.setReferenceProfit(new BigDecimal(v.getReferenceProfit())
									.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
						}
					}
					rvo.setPositions(list);
				}
			}
			// 改变默认值
			ClassUtil.setDefvalue(rvo);
			return ResultUtil.setResult(result, RespCode.Code.SUCCESS, rvo);
		} catch (Exception e) {
			log.error("查询投顾当前持仓列表实时数据异常", e);
		}
		return result;
	}
}
