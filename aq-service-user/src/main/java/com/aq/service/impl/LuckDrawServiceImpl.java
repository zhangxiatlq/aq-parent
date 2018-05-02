package com.aq.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.aq.facade.entity.AuthUser;
import com.aq.facade.entity.LuckDrawNum;
import com.aq.facade.entity.Prize;
import com.aq.facade.entity.UserVip;
import com.aq.facade.entity.WinPrizeRecord;
import com.aq.facade.enums.PrizeEnum;
import com.aq.facade.enums.PrizeTypeEnum;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.exception.permission.UserExceptionEnum;
import com.aq.facade.service.ILuckDrawService;
import com.aq.facade.vo.LuckDrawNumVO;
import com.aq.mapper.AuthUserMapper;
import com.aq.mapper.LuckDrawNumMapper;
import com.aq.mapper.PrizeMapper;
import com.aq.mapper.UserVipMapper;
import com.aq.mapper.WinPrizeRecordMapper;
import com.aq.util.date.DateUtil;
import com.aq.util.other.prize.LuckDrawUtil;
import com.aq.util.other.prize.PrizeResponse;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: LuckDrawServiceImpl
 * @Description: 抽奖相关服务
 * @author: lijie
 * @date: 2018年1月22日 下午3:11:07
 */
@Slf4j
@Service(version = "1.0.0", timeout = 60000, retries = 0)
public class LuckDrawServiceImpl implements ILuckDrawService {
	
	@Autowired
	private UserVipMapper userVipMapper;
	
	@Autowired
	private AuthUserMapper authUserMapper;
	
	@Autowired
	private LuckDrawNumMapper luckDrawNumMapper;
	
	@Autowired
	private PrizeMapper prizeMapper;
	
	@Autowired
	private WinPrizeRecordMapper winPrizeRecordMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result startLuckDraw(Integer userId, String prizeCode) {
		log.info("抽奖入参={},prizeCode={}", userId, prizeCode);
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			AuthUser authUser = authUserMapper.selectByPrimaryKey(userId);
			if (null == authUser) {
				result.setMessage("用户不存在");
				return result;
			}
			final Prize prize = queryPrize(prizeCode);
			if (null == prize) {
				result.setMessage("抽奖活动已停止");
				return result;
			}
			if (!checkNum(userId, prizeCode)) {
				result.setMessage("抽奖次数已用完");
				return result;
			}
			PrizeResponse prizeResponse = LuckDrawUtil.luckDraw(prize.getValue());
			WinPrizeRecord record = new WinPrizeRecord();
			record.setCreaterId(userId);
			record.setCreateTime(new Date());
			record.setIsReceive(PrizeEnum.NOT_RECEIVE.getStatus());
			record.setPrizeCode(prizeCode);
			record.setUserId(userId);
			record.setWinValue((int) prizeResponse.getValue());
			record.setWinCode(String.valueOf(System.currentTimeMillis()));
			winPrizeRecordMapper.insertUseGeneratedKeys(record);
			Map<String, Object> map = new HashMap<>(16);
			map.put("id", record.getId());
			if (prizeCode.equals(PrizeTypeEnum.BUY_GIVE_DAY.getCode())) {
				map.put("index", (int) prizeResponse.getGrade() + 6);
			} else {
				map.put("index", prizeResponse.getGrade());
			}
			return ResultUtil.setResult(result, RespCode.Code.SUCCESS, map);
		} catch (Exception e) {
			log.error("抽奖异常", e);
			throw new UserBizException(UserExceptionEnum.LUCK_DRAW_FAIL);
		}
	}
	
	/**
	 * 
	 * @Title: queryPrize
	 * @author: lijie 
	 * @Description: 
	 * @param prizeCode
	 * @return
	 * @return: boolean
	 */
	private Prize queryPrize(final String prizeCode) {
		Prize p = new Prize();
		p.setCode(prizeCode);
		p.setIsValid(PrizeEnum.VALID.getStatus());
		return prizeMapper.selectOne(p);
	}
	
	private boolean checkNum(final Integer userId, final String prizeCode) {
		LuckDrawNum ld = new LuckDrawNum();
		ld.setUserId(userId);
		ld.setPrizeCode(prizeCode);
		ld = luckDrawNumMapper.selectOne(ld);
		if (null == ld || ld.getNum() == 0) {
			return false;
		} else {
			LuckDrawNum update = new LuckDrawNum();
			update.setId(ld.getId());
			update.setNum(ld.getNum() - 1);
			luckDrawNumMapper.updateByPrimaryKeySelective(update);
			return true;
		}
	}
	

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result addLuckDraw(Integer userId) {
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		log.info("初始化抽奖次数如参：", userId);
		try {
			AuthUser authUser = authUserMapper.selectByPrimaryKey(userId);
			if (null == authUser) {
				result.setMessage("用户不存在");
				return result;
			}
			if (initLuckDrawNum(PrizeTypeEnum.VIP_DAY, userId)) {
				return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
			} else {
				result.setMessage("你已参加抽奖");
			}
			return result;
		} catch (Exception e) {
			log.error("添加抽奖信息异常", e);
			throw new UserBizException(UserExceptionEnum.ADD_LUCKDRAWNUM_FAIL);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result receivePrize(Integer userId, Integer id) {
		log.info("领取奖励入参={},id={}", userId, id);
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			AuthUser authUser = authUserMapper.selectByPrimaryKey(userId);
			if (null == authUser) {
				result.setMessage("用户不存在");
				return result;
			}
			WinPrizeRecord record = new WinPrizeRecord();
			record.setUserId(userId);
			record.setId(id);
			record.setIsReceive((byte) 0);
			record = winPrizeRecordMapper.selectByPrimaryKey(record);
			if (null == record) {
				result.setMessage("用户没有可领取的奖励");
				return result;
			}
			// 校验奖项枚举
			final PrizeTypeEnum type = PrizeTypeEnum.getPrizeType(record.getPrizeCode());
			if (PrizeTypeEnum.VIP_DAY == type) {
				vipDayReceive(userId, authUser.getUsername(), record);
			}
			WinPrizeRecord urecord = new WinPrizeRecord();
			urecord.setId(record.getId());
			urecord.setIsReceive(PrizeEnum.RECEIVE.getStatus());
			winPrizeRecordMapper.updateByPrimaryKeySelective(urecord);
			return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("领取奖励异常", e);
			throw new UserBizException(UserExceptionEnum.RECEIVEPRIZE_FAIL);
		}
	}
	
	/**
	 * 
	 * @Title: vipDayReceive
	 * @author: lijie 
	 * @Description: 领取vip天数，修改vip数据
	 * @param userId
	 * @param userName
	 * @param record
	 * @throws ParseException
	 * @return: void
	 */
	private void vipDayReceive(final Integer userId, final String userName, final WinPrizeRecord record)
			throws ParseException {
		UserVip userVip = new UserVip();
		userVip.setUserId(userId);
		userVip = userVipMapper.selectOne(userVip);
		final SimpleDateFormat formatter = DateUtil.genFormatter(DateUtil.YYYYMMDD);
		if (null != userVip) {
			UserVip update = new UserVip();
			Date time = DateUtil.format(new Date(), formatter);
			if (userVip.getVipEndTime().after(time) || userVip.getVipEndTime().getTime() == time.getTime()) {
				update.setVipEndTime(DateUtil.getAddDayDate(userVip.getVipEndTime(), record.getWinValue()));
			} else {
				update.setVipEndTime(DateUtil.format(
						DateUtil.getAddDayDate(new Date(), record.getWinValue()), formatter));
			}
			update.setId(userVip.getId());
			userVipMapper.updateByPrimaryKeySelective(update);
		} else {
			UserVip insert = new UserVip();
			insert.setCreateDate(new Date());
			insert.setUserId(userId);
			// TODO:此处需优化
			insert.setVipType(1);
			insert.setUsername(userName);
			insert.setStatus(0);
			insert.setVipEndTime(DateUtil.format(
					DateUtil.getAddDayDate(new Date(), record.getWinValue()), formatter));
			userVipMapper.insertUseGeneratedKeys(insert);
		}
	}

	@Override
	public Result getLuckDrawNum(Integer userId) {
		log.info("查询抽奖次数入参={}", userId);
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			final List<LuckDrawNumVO> rlist = new ArrayList<LuckDrawNumVO>();
			LuckDrawNum select = new LuckDrawNum();
			List<LuckDrawNum> list = luckDrawNumMapper.select(select);
			if (null != list && !list.isEmpty()) {
				LuckDrawNumVO vo;
				for (LuckDrawNum dn : list) {
					vo = new LuckDrawNumVO();
					vo.setPrizeCode(dn.getPrizeCode());
					vo.setNum(dn.getNum());
					rlist.add(vo);
				}
			}
			result.setData(rlist);
			result.setCount(rlist.size());
			return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("查询抽奖次数异常", e);
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result vipExpireGive() {
		try {
			List<UserVip> list = userVipMapper.selectAll();
			if (null != list && !list.isEmpty()) {
				Date local = new Date();
				for (UserVip vip : list) {
					if (DateUtils.isSameDay(vip.getVipEndTime(), local)) {
						initLuckDrawNum(PrizeTypeEnum.BUY_GIVE_DAY, vip.getUserId());
					}
				}
			}
			return ResultUtil.getResult(RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("vip到期赠送购买抽奖机会异常", e);
			throw new UserBizException(UserExceptionEnum.VIP_EXPIRE_GIVE);
		}
	}
	/**
	 * 
	 * @Title: initLuckDrawNum
	 * @author: lijie 
	 * @Description: 初始化抽奖次数
	 * @param type
	 * @param userId
	 * @return: void
	 */
	private synchronized boolean initLuckDrawNum(final PrizeTypeEnum type, final Integer userId) {
		LuckDrawNum selectOne = new LuckDrawNum();
		selectOne.setUserId(userId);
		selectOne.setPrizeCode(type.getCode());
		selectOne = luckDrawNumMapper.selectOne(selectOne);
		if (null == selectOne) {
			LuckDrawNum insert = new LuckDrawNum();
			insert.setUserId(userId);
			insert.setNum(1);
			insert.setPrizeCode(type.getCode());
			luckDrawNumMapper.insertUseGeneratedKeys(insert);
			return true;
		} else {
			// 到期购买赠送
			if (type == PrizeTypeEnum.BUY_GIVE_DAY) {
				LuckDrawNum update = new LuckDrawNum();
				update.setId(selectOne.getId());
				update.setNum(selectOne.getNum() + 1);
				luckDrawNumMapper.updateByPrimaryKeySelective(update);
				return true;
			}
		}
		return false;
	}
}
