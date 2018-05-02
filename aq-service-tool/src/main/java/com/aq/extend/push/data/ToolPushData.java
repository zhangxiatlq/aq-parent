package com.aq.extend.push.data;

import java.io.Serializable;

import com.aq.core.constant.ToolCategoryEnum;
import com.aq.core.wechat.dto.WechatPushTemplateDTO;
import com.aq.facade.dto.ToolPushDTO;

import lombok.Data;
/**
 * 
 * @ClassName: ToolPushData
 * @Description: TODO
 * @author: lijie
 * @date: 2018年4月17日 下午12:15:48
 */
@Data
public class ToolPushData implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;

	private WechatPushTemplateDTO template;
	
	private Integer bindId;
	
	private ToolPushDTO dto;
	
	private ToolCategoryEnum type;
}
