package com.aq.extend;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.aq.core.wechat.util.WeChatPushUtil;
import com.aq.extend.push.data.ToolPushData;
import com.aq.facade.contant.ToolStatusEnum;
import com.aq.facade.dto.ToolPushDTO;
import com.aq.facade.entity.ToolPush;
import com.aq.mapper.ToolBindRecordMapper;
import com.aq.mapper.ToolPushMapper;
import com.aq.util.date.SyncDateUtil;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: IndependentAffairService
 * @Description: 用于独立事务控制减少查询时的附加资源消耗
 * @author: lijie
 * @date: 2018年4月17日 下午4:06:09
 */
@Slf4j
@Service
public class IndependentAffairService {

	@Autowired
	private ToolBindRecordMapper toolBindRecordMapper;
	
	@Autowired
	protected ToolPushMapper toolPushMapper;
	
	@Transactional
	public void updateToolIsSynchro(final List<Integer> bindIds) {
		if (CollectionUtils.isNotEmpty(bindIds)) {
			toolBindRecordMapper.updateToolIsSynchro(bindIds, ToolStatusEnum.SYNCHRO.getStatus());
		}
	}
	/**
	 * 
	* @Title: handlePush  
	* @Description: 执行推送 
	* @param: @param data
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	@Transactional
	public Result handlePush(ToolPushData data) {
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			// 新增推送记录
			insertToolPush(data.getBindId(), data.getDto());
			// 推送成功保存推送记录
			if (!WeChatPushUtil.pushWechat(data.getTemplate())) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			} else {
				return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
			}
		} catch (Exception e) {
			log.error("工具推送异常", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}
	/**
	 * 
	* @Title: insertToolPush  
	* @Description: 新增记录 
	* @param: @param bindId
	* @param: @param dto
	* @return void
	* @author lijie
	* @throws
	 */
	private void insertToolPush(final Integer bindId, ToolPushDTO dto) {
		ToolPush record = new ToolPush();
		record.setBindId(bindId);
		record.setCreateTime(new Date());
		record.setIsDelete(ToolStatusEnum.NOT_DELETED.getStatus());
		// 参数完善
		record.setDirection(ToolStatusEnum.getStatus(dto.getDirection()));
		BigDecimal price = new BigDecimal(dto.getPrice());
		record.setNum(dto.getNumber());
		if (null != dto.getNumber()) {
			record.setTotalPrice(price.multiply(new BigDecimal(dto.getNumber())));
		} else {
			record.setTotalPrice(price);
		}
		record.setTradingTime(SyncDateUtil.strToDate(dto.getTradingTime(), true));
		record.setTranPrice(price);
		toolPushMapper.insert(record);
	}
}
