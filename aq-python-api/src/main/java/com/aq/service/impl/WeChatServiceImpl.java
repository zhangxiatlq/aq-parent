package com.aq.service.impl;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.core.config.properties.WeChatCoreProperties;
import com.aq.core.constant.AttentionWchatEnum;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.core.wechat.constant.MessageCueEnum;
import com.aq.core.wechat.constant.TagEnum;
import com.aq.core.wechat.constant.WheChatConstant;
import com.aq.facade.dto.WeChatBindDTO;
import com.aq.facade.entity.wechat.WechatPosition;
import com.aq.facade.exception.customer.CustomerExceptionEnum;
import com.aq.facade.exception.manage.ManageExceptionEnum;
import com.aq.facade.service.customer.ICustomerManageService;
import com.aq.facade.service.customer.ICustomerService;
import com.aq.facade.service.wechat.IWechatUserService;
import com.aq.facade.vo.WechatBindFailVO;
import com.aq.service.IWeChatService;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.aq.util.string.StringTools;
import com.aq.util.wechat.domain.Article;
import com.aq.util.wechat.util.MessageUtil;
import com.aq.util.wechat.util.XmlUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: OpenIdService
 * @Description: 微信服务
 * @author: lijie
 * @date: 2018年3月8日 下午5:31:12
 */
@Slf4j
@Service
public class WeChatServiceImpl implements IWeChatService {

	/**
     * 客户信息
     */
    @Reference(version = "1.0.0", check = false)
    private ICustomerService customerService;
    /**
     * 客户经理信息数据
     */
    @Reference(version = "1.0.0", check = false)
    private ICustomerManageService customerManageService;

    @Reference(version = "1.0.0", check = false)
    private IWechatUserService wechatUserService;

    @Autowired
    private WeChatCoreProperties weChatCoreProperties;
    /**
     * 
    * @Title: bindOpenId  
    * @Description: 绑定微信openID  
    * @param @param dto
    * @param @return    参数  
    * @return Result    返回类型  
    * @throws
     */
	@Override
	public Result bindOpenId(WeChatBindDTO dto) {
		log.info("绑定openID入参={}", JSON.toJSONString(dto));
		String errorStr = dto.validateForm();
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			if (StringUtils.isNotBlank(errorStr)) {
				result.setMessage(errorStr);
				return result;
			}
			RoleCodeEnum code = RoleCodeEnum.getRoleEnumByCode(dto.getRoleCode().byteValue());
			if (null == code) {
				result.setMessage("角色不存在");
				return result;
			}
			if (code == RoleCodeEnum.CUSTOMER) {
				result = customerService.updateCustomerOpenId(dto.getAccount(), dto.getOpenId());
			} else if (code == RoleCodeEnum.MANAGER) {
				result = customerManageService.updateCustomerManageOpenId(dto.getAccount(), dto.getOpenId());
			}
		} catch (Exception e) {
			log.error("绑定微信/设置openId异常", e);
		}
		log.info("绑定openID返回数据result={}", JSON.toJSONString(result));
		return result;
	}
	/**
	 * 
	* @Title: processRequest  
	* @Description: 处理事件，可扩展其它事件 
	* @param: @param request
	* @return void
	* @author lijie
	* @throws
	 */
	@Override
	public void processEvent(HttpServletRequest request, HttpServletResponse response) {
		try {
			// xml请求解析
			Map<String, String> requestMap = XmlUtil.xmlToMap(request);
			log.info("微信事件通知解析结果={}", requestMap);
			String toUserName = requestMap.get("ToUserName");
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			// 事件处理开始
			if (MessageUtil.REQ_MESSAGE_TYPE_EVENT.equals(msgType)) {
				// EventKey:客户信息
				String eventKey = requestMap.get("EventKey");
				// 事件类型
				String eventType = requestMap.get("Event");

				if (MessageUtil.EVENT_TYPE_SUBSCRIBE.equals(eventType)) {
					// 关注事件：关注成功
					// 关注微信、处理绑定状态
					Result result = bindOpenIdByEvent(eventKey, fromUserName, toUserName, response);
					log.info("关注事件：关注成功 >> 绑定微信/设置openid返回数据={}", JSON.toJSONString(result));
					String sendMessage = MessageUtil.REPLY_FOLLOW;
					Object message = result.getData();
					if (null != message) {
						sendMessage = message + "\n\n" + sendMessage;
					}
					// TODO:回复自然关注信息
					sendMessage(response, MessageUtil.getTextMsg(toUserName, fromUserName, sendMessage));
					// 修改关注状态
					updateFollow(fromUserName, AttentionWchatEnum.ATTENTION_WCHAT_ENUM.getCode(), eventKey);
					// 处理关注
					handleFollow(fromUserName, AttentionWchatEnum.ATTENTION_WCHAT_ENUM.getCode());
				} else if (MessageUtil.EVENT_TYPE_UNSUBSCRIBE.equals(eventType)) {
					// 取消关注事件,用户接受不到我们发送的消息了，可以在这里记录用户取消关注的日志信息
					// 修改关注状态
					updateFollow(fromUserName, AttentionWchatEnum.NOT_ATTENTION_WCHAT_ENUM.getCode(), eventKey);
					// 处理取消关注
					handleFollow(fromUserName, AttentionWchatEnum.NOT_ATTENTION_WCHAT_ENUM.getCode());
				} else if (MessageUtil.EVENT_TYPE_SCAN.equals(eventType)) {
					// 已关注、处理绑定状态
					Result result = bindOpenIdByEvent(eventKey, fromUserName, toUserName, response);
					log.info("已关注事件：已关注 >> 绑定微信/设置openid返回数据={}", JSON.toJSONString(result));
					Object data = result.getData();
					if (null != data) {
						sendMessage(response, MessageUtil.getTextMsg(toUserName, fromUserName, data.toString()));
					}
				} else if (MessageUtil.EVENT_TYPE_LOCATION.equals(eventType)) {
					// 处理上报的地理位置信息
					WechatPosition position = new WechatPosition();
					position.setCreateTime(new Date());
					String latitude = requestMap.get("Latitude");
					String longitude = requestMap.get("Longitude");
					String precision = requestMap.get("Precision");
					if (StringUtils.isNotBlank(latitude)) {
						position.setLatitude(Double.valueOf(latitude));
					}
					if (StringUtils.isNotBlank(longitude)) {
						position.setLongitude(Double.valueOf(longitude));
					}
					if (StringUtils.isNotBlank(precision)) {
						position.setPrecisions(Double.valueOf(precision));
					}
					position.setOpenId(fromUserName);
					addLocation(position);
				}
			} // 处理文本信息
			else if (MessageUtil.MESSAGE_TEXT.equals(msgType)) {
				// 回复引导信息
				String content = requestMap.get("Content");
				if (StringUtils.isNotBlank(content)) {
					String message = replyTextMessage(content, toUserName, fromUserName);
					if (StringUtils.isNotBlank(message)) {
						sendMessage(response, message);
					}
				}
			}

		} catch (Exception e) {
			log.info("处理微信公众号事件通知异常", e);
		}
	}
	/**
	 *
	* @Title: replyTextMessage
	* @Description: 回复文本消息
	* @param: @param content
	* @return void
	* @author lijie
	* @throws
	 */
	private String replyTextMessage(String content, String toUserName, String fromUserName) {
		String result = "";
		if (StringUtils.isNotBlank(content)) {
			MessageCueEnum message = MessageCueEnum.info(content);
			if (null != message) {
				List<Article> articles = new ArrayList<>();
				Article am = new Article();
				am.setDescription(message.getDescription());
				am.setPicUrl(message.getPicUrl());
				am.setTitle(message.getTitle());
				am.setUrl(weChatCoreProperties.getViewUrl() + message.getUrl());
				articles.add(am);
				result = MessageUtil.getImageTextMsg(toUserName, fromUserName, articles);
			}
		}
		return result;
	}
	/**
	 *
	* @Title: addLocation
	* @Description: 保存上报位置信息
	* @param: @param position
	* @return void
	* @author lijie
	* @throws
	 */
	private void addLocation(WechatPosition position) {
		try {
			Result result = wechatUserService.addLocationInfo(position);
			log.info("保存用户地理位置返回数据={}", JSON.toJSONString(result));
		} catch (Exception e) {
			log.error("保存用户地理位置失败", e);
		}
	}

	private void updateFollow(final String openId, final Byte status, final String eventKey) {
		try {
			customerService.updateWeChatAttention(openId, status);
			customerManageService.updateWeChatAttention(openId, status);
		} catch (Exception e) {
			log.error("微信操作关注状态异常", e);
		}
	}

	/**
	 * 
	* @Title: bindOpenId  
	* @Description: 绑定openId 
	* @param: @param eventKey
	* @param: @param openId
	* @return void
	* @author lijie
	* @throws
	 */
	private Result bindOpenIdByEvent(final String eventKey, final String fromUserName, String toUserName,
			HttpServletResponse response) {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		// 如果说是二维码跳转过来则绑定
		try {
			// 是否是自然关注（true 是【通过搜索关注】false 否【通过扫描二维码】）
			boolean isNatural = true;
			if (StringUtils.isNotBlank(eventKey)) {
				// 绑定openId操作
				WeChatBindDTO bindDto = initWeChatBindDTO(eventKey, fromUserName);
				isNatural = (null == bindDto);
				if (!isNatural) {
					result = bindOpenId(bindDto);
					Byte roleCode = bindDto.getRoleCode();
					// 绑定失败根据状态码主动发送信息给用户
					if (!result.isSuccess()) {
						String code = result.getCode();
						result.setData(getFailMsg(code, result.getData(), roleCode));
					} else {
						result.setData(getSuccessMsg(roleCode, StringTools.telphoneChange(bindDto.getAccount())));
						// 成功后给用户打标签并发送成功信息
						if (RoleCodeEnum.CUSTOMER.getCode().equals(roleCode)) {
							// 为客户打上标签
							wechatUserService.addUserTag(TagEnum.FOLLOW_CUSTOMER_TAG.getTagId(), fromUserName);
						} else if (RoleCodeEnum.MANAGER.getCode().equals(roleCode)) {
							// 为客户经理打上标签
							wechatUserService.addUserTag(TagEnum.FOLLOW_MANAGER_TAG.getTagId(), fromUserName);
						}
					}
				}
			}
			// 处理自然关注
			if (isNatural) {
				// 为已绑定过的用户打上标签
				Result cr = customerService.getCustomerByOpenId(fromUserName);
				if (cr.isSuccess()) {
					// 为客户打上标签
					wechatUserService.addUserTag(TagEnum.FOLLOW_CUSTOMER_TAG.getTagId(), fromUserName);
				}
				Result mr = customerManageService.getManagerByOpenId(fromUserName);
				if (mr.isSuccess()) {
					// 为客户经理打上标签
					wechatUserService.addUserTag(TagEnum.FOLLOW_MANAGER_TAG.getTagId(), fromUserName);
				}
			}
		} catch (Exception e) {
			log.error("处理用户绑定信息异常", e);
		}
		log.info("微信绑定返回结果信息={}", JSON.toJSONString(result));
		return result;
	}

	public String getSuccessMsg(final Byte roleCode, final String account) {
		String result = "";
		if (RoleCodeEnum.CUSTOMER.getCode().equals(roleCode)) {
			result = String.format(MessageUtil.REPLY_SUCCESS, MessageUtil.CUSTOMER, account);
		} else if (RoleCodeEnum.MANAGER.getCode().equals(roleCode)) {
			result = String.format(MessageUtil.REPLY_SUCCESS, MessageUtil.MANAGER, account);
		}
		return result;
	}
	/**
	 *
	* @Title: getBindFailMessage
	* @Description: 封装失败的提示消息
	* @param: @param code
	* @param: @param toUserName
	* @param: @param fromUserName
	* @param: @param telphone
	* @param: @param roleCode
	* @param: @return
	* @return String
	* @author lijie
	* @throws
	 */
	private String getFailMsg(final String code, Object data, final Byte roleCode) {
		String result = "";
		if (CustomerExceptionEnum.ACCOUNT_ALREADY_BIND.getCode().equals(code)) {
			WechatBindFailVO fail = (WechatBindFailVO) data;
			result = String.format(MessageUtil.REPLY_ACCOUNT_EXISTS, MessageUtil.CUSTOMER, fail.getTelphone(),
					fail.getNickname());
		} else if (CustomerExceptionEnum.OPENID_EXISTS.getCode().equals(code)) {
			result = String.format(MessageUtil.REPLY_OPENID_EXISTS, MessageUtil.CUSTOMER, data);
		} else if (ManageExceptionEnum.ACCOUNT_ALREADY_BIND.getCode().equals(code)) {
			WechatBindFailVO fail = (WechatBindFailVO) data;
			result = String.format(MessageUtil.REPLY_ACCOUNT_EXISTS, MessageUtil.MANAGER, fail.getTelphone(),
					fail.getNickname());
		} else if (ManageExceptionEnum.OPENID_EXISTS.getCode().equals(code)) {
			result = String.format(MessageUtil.REPLY_OPENID_EXISTS, MessageUtil.MANAGER, data);
		} else {
			// 未知失败错误提示
			if (RoleCodeEnum.CUSTOMER.getCode().equals(roleCode)) {
				result = String.format(MessageUtil.REPLY_FAIL, MessageUtil.CUSTOMER);
			} else if (RoleCodeEnum.MANAGER.getCode().equals(roleCode)) {
				result = String.format(MessageUtil.REPLY_FAIL, MessageUtil.MANAGER);
			}
		}
		return result;
	}

	private void handleFollow(final String openId, final Byte status) {
		try {
			Result result = wechatUserService.handleFollow(openId, status);
			log.info("处理关注信息返回数据={}", JSON.toJSONString(result));
		} catch (Exception e) {
			log.error("处理关注信息异常", e);
		}
	}
	/**
	 * 
	* @Title: initWeChatBindDTO  
	* @Description: TODO 
	* @param: @param qrCodeInfo
	* @param: @param openId
	* @param: @return
	* @return WeChatBindDTO
	* @author lijie
	* @throws
	 */
	private WeChatBindDTO initWeChatBindDTO(String qrCodeInfo, String openId) {
		WeChatBindDTO bindDto = null;
		if (qrCodeInfo.indexOf(WheChatConstant.BIND_IDEN) != -1) {
			if (qrCodeInfo.startsWith("qrscene_")) {
				qrCodeInfo = qrCodeInfo.substring("qrscene_".length());
			}
			String[] strs = qrCodeInfo.split(WheChatConstant.BIND_IDEN);
			bindDto = new WeChatBindDTO();
			bindDto.setAccount(strs[1]);
			bindDto.setRoleCode(Byte.valueOf(strs[0]));
			bindDto.setOpenId(openId);
		}
		return bindDto;
	}

	private void sendMessage(HttpServletResponse response, String message) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/xml;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		try {
			PrintWriter pw = response.getWriter();
			pw.println(message);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			log.error("发送信息异常", e);
		}
	}
}
