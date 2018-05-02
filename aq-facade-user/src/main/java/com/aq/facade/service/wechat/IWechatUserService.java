package com.aq.facade.service.wechat;

import com.aq.facade.entity.wechat.WechatPosition;
import com.aq.util.result.Result;
/**
 *
 * @ClassName: IWechatUserService
 * @Description: 微信用户相关服务
 * @author: lijie
 * @date: 2018年3月21日 下午2:37:44
 */
public interface IWechatUserService {
	/**
	 *
	* @Title: addUserTag
	* @Description: 为用户添加标签
	* @param: @param tagId
	* @param: @param openId
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	Result addUserTag(Integer tagId, String openId);
	/**
	 *
	* @Title: addLocationInfo
	* @Description: 添加地理位置信息
	* @param: @param position
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	Result addLocationInfo(WechatPosition position);
	/**
	 *
	* @Title: handleFollow
	* @Description: 处理用户关注信息
	* @param: @param openId
	* @param: @param status
	* @param: @return
	* @return Result
	* @author lijie
	* @throws
	 */
	Result handleFollow(String openId, Byte status);

}
