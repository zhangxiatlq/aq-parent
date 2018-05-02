package com.aq.util.other.prize;

import java.io.Serializable;

import lombok.Data;
/**
 * 
 * @ClassName: PrizeResponse
 * @Description: 中奖响应数据
 * @author: lijie
 * @date: 2018年1月25日 下午6:07:27
 */
@Data
public class PrizeResponse implements Serializable {
	
	/**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 下标
	 */
	private int index;
	/**
	 * 奖项值
	 */
	private Object value;
	/**
	 * 奖项等级
	 */
	private Object grade;
	
	@Override
	public String toString() {
		return "PrizeResponse [index=" + index + ", value=" + value + ", grade=" + grade + "]";
	}
	
	
}
