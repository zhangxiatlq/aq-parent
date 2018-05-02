package com.aq.util.other.prize;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LuckDrawUtil {

	/**
	 * 
	 * @Title: luckDraw
	 * @author: lijie 
	 * @Description: 解析字符串并随机抽奖
	 * @param prizearr
	 * @return
	 * @return: PrizeResponse
	 */
	public static PrizeResponse luckDraw(String prizearr) {
		Assert.notNull(prizearr, "prizearr isnull");
		PrizeResponse response = new PrizeResponse();
		JSONArray jsonarray = JSON.parseArray(prizearr);
		Object[] objs = jsonarray.toArray();
		int len = objs.length;
		Integer[] obj = new Integer[len];
		List<Object[]> list = new ArrayList<>();
		Object[] ps;
		for (int i = 0; i < len; i++) {
			ps = JSON.parseArray(objs[i].toString()).toArray();
			obj[i] = (Integer) ps[1];
			list.add(ps);
		}
		Integer prizeId = getRand(obj, len);
		response.setIndex(prizeId);
		response.setValue(list.get(prizeId)[0]);
		response.setGrade(list.get(prizeId)[2]);
		return response;
	}
	
	public static void main(String[] args) {
		String prizearr = "[[5, 10,1],[2, 50,2]]";
		PrizeResponse response = luckDraw(prizearr);
		System.out.println(response);
	}
	/**
	 * 
	 * @Title: getRand
	 * @author: lijie 
	 * @Description: 概率计算
	 * @param obj
	 * @return
	 * @return: Integer
	 */
	private static Integer getRand(final Integer[] obj, final int len) {
		int result = len - 1;
		try {
			// 概率数组的总概率精度
			int sum = 0;
			for (int i = 0; i < obj.length; i++) {
				sum += obj[i];
			}
			// 概率数组循环
			for (int i = 0; i < obj.length; i++) {
				// 随机生成1到sum的整数
				int randomNum = new Random().nextInt(sum);
				// 中奖
				if (randomNum < obj[i]) {
					result = i;
					break;
				} else {
					sum -= obj[i];
				}
			}
		} catch (Exception e) {
			log.error("random prize error", e);
		}
		return result;
	}
}
