package com.aq.service.impl.wechat;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.core.lock.RedisDistributionLock;
import com.aq.core.wechat.tags.WeChatTagsUtil;
import com.aq.facade.entity.wechat.WechatPosition;
import com.aq.facade.entity.wechat.WhechatFollow;
import com.aq.facade.entity.wechat.WhechatTagRecord;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.service.wechat.IWechatUserService;
import com.aq.mapper.wechat.WechatPositionMapper;
import com.aq.mapper.wechat.WhechatFollowMapper;
import com.aq.mapper.wechat.WhechatTagRecordMapper;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @ClassName: WechatUserServiceImpl
 * @Description: 微信用户相关服务
 * @author: lijie
 * @date: 2018年3月21日 下午2:36:02
 */
@Slf4j
@Service(version = "1.0.0")
public class WechatUserServiceImpl implements IWechatUserService {

	@Autowired
	private WhechatTagRecordMapper tagRecordMapper;

	@Autowired
	private WechatPositionMapper positionMapper;

	@Autowired
	private WhechatFollowMapper followMapper;

	@Autowired
	private RedisDistributionLock lock;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result addUserTag(Integer tagId, String openId) {
		log.info("为用户添加标签入参tagId={},openIds={}", tagId, openId);
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		if (null == tagId || StringUtils.isBlank(openId)) {
			result.setMessage("标签或用户微信标识不能为空");
			return result;
		}
		String lockKey = "TAG_" + openId;
		try {
			lock.lock(lockKey);
			try {
				WhechatTagRecord selectOne = new WhechatTagRecord();
				selectOne.setOpenId(openId);
				selectOne = tagRecordMapper.selectOne(selectOne);
				if (null != selectOne) {
					tagRecordMapper.deleteByPrimaryKey(selectOne.getId());
				}
			} finally {
				lock.unLock(lockKey);
			}
			WhechatTagRecord record = new WhechatTagRecord();
			record.setCreateTime(new Date());
			record.setOpenId(openId);
			record.setTagId(tagId);
			record.setStatus((byte) 2);
			tagRecordMapper.insertUseGeneratedKeys(record);
			List<String> openIds = new ArrayList<>();
			openIds.add(openId);
			Result tagr = WeChatTagsUtil.batchTagging(tagId, openIds);
			log.info("为用户设置标签返回数据={}", JSON.toJSONString(tagr));
			if (tagr.isSuccess()) {
				WhechatTagRecord update = new WhechatTagRecord();
				update.setId(record.getId());
				update.setUpdateTime(new Date());
				update.setStatus((byte) 1);
				tagRecordMapper.updateByPrimaryKeySelective(update);
			}
			return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("为用户添加标签异常", e);
			throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result addLocationInfo(WechatPosition position) {
		log.info("添加用户地理位置信息入参={}", JSON.toJSONString(position));
		try {
			String lockKey = "LOCATION_" + position.getOpenId();
			lock.lock(lockKey);
			try {
				WechatPosition record = new WechatPosition();
				record.setOpenId(position.getOpenId());
				record = positionMapper.selectOne(record);
				if (null == record) {
					positionMapper.insertUseGeneratedKeys(position);
				} else {
					WechatPosition update = new WechatPosition();
					BeanUtils.copyProperties(position, update);
					update.setId(record.getId());
					positionMapper.updateByPrimaryKeySelective(update);
				}
			} finally {
				lock.unLock(lockKey);
			}
			return ResultUtil.getResult(RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("添加用户地理位置信息异常", e);
			throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Result handleFollow(String openId, Byte status) {
		log.info("操作用关注信息入参={},status={}", openId, status);
		final Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			if (StringUtils.isBlank(openId) || null == status) {
				result.setMessage("openId或关注状态不能为空");
				return result;
			}
			String lockKey = "HANDLEFOLLOW_" + openId;
			lock.lock(lockKey);
			try {
				WhechatFollow selectOne = new WhechatFollow();
				selectOne.setOpenId(openId);
				selectOne = followMapper.selectOne(selectOne);
				if (null != selectOne) {
					WhechatFollow update = new WhechatFollow();
					update.setId(selectOne.getId());
					if (status == 1) {
						update.setUnfollowTime(new Date());
					} else if (status == 2) {
						update.setFollowTime(new Date());
					}
					update.setStatus(status);
					followMapper.updateByPrimaryKeySelective(update);
				} else {
					WhechatFollow insert = new WhechatFollow();
					Date date = new Date();
					insert.setCreateTime(date);
					insert.setOpenId(openId);
					insert.setStatus(status);
					if (status == 1) {
						insert.setUnfollowTime(date);
					} else if (status == 2) {
						insert.setFollowTime(date);
					}
					followMapper.insertUseGeneratedKeys(insert);
				}
			} finally {
				lock.unLock(lockKey);
			}
			return ResultUtil.setResult(result, RespCode.Code.SUCCESS);
		} catch (Exception e) {
			log.error("操作用户关注信息异常", e);
			throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
		}
	}

}
