package com.aq.core.wechat.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aq.core.wechat.constant.WheChatConstant;
import com.aq.core.wechat.util.WeChatRquestUtil;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信标签
 *
 * @author 郑朋
 * @create 2018/2/28 0028
 **/
@Slf4j
public class WeChatTagsUtil {

    /**
     * 批量为用户打标签 地址
     */
    private static final String BATCH_TAGGING = "https://api.weixin.qq.com/cgi-bin/tags/members/batchtagging";

    /**
     * 批量为用户取消标签 地址
     */
    private static final String UN_BATCH_TAGGING = "https://api.weixin.qq.com/cgi-bin/tags/members/batchuntagging";
    /**
     * 批量为用户打标签
     *
     * @param tagid
     * @param openIds
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/28
     */
    public static Result batchTagging(Object tagid, List<String> openIds) {
        return Tagging(BATCH_TAGGING, tagid, openIds);
    }

    /**
     * 批量为用户取消标签
     *
     * @param tagid
     * @param openIds
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/28
     */
    public static Result batchUnTagging(Object tagid, List<String> openIds) {
        return Tagging(UN_BATCH_TAGGING, tagid, openIds);
    }

	private static Result Tagging(String url, Object tagid, List<String> openIds) {
		Result result = ResultUtil.getResult(RespCode.Code.FAIL);
		try {
			if (CollectionUtils.isEmpty(openIds) || null == tagid) {
				result.setMessage(RespCode.Code.REQUEST_DATA_ERROR.getMsg());
				return result;
			}
			if (openIds.size() > 50) {
				result.setMessage("每次传入的openid列表个数不能超过50个");
				return result;
			}
			Map<String, Object> json = new HashMap<>(2);
			json.put("tagid", tagid);
			json.put("openid_list", openIds);

			String msg = WeChatRquestUtil.sendPostJsonByToken(url, JSON.toJSONString(json));
			log.info("批量为用户操作标签结果:{}", msg);
			JSONObject jsonObject = JSON.parseObject(msg);
			String errCode = jsonObject.getString("errcode");
			if (WheChatConstant.SUCCESS_STATE.equals(errCode)) {
				result = ResultUtil.getResult(RespCode.Code.SUCCESS);
			} else {
				result.setMessage(jsonObject.getString("errmsg"));
			}
		} catch (Exception e) {
			log.error("批量为用户操作标签异常，e={}", e);
		}
		return result;
	}
	
}
