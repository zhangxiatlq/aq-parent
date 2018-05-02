package com.aq.util.wechat.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @version 1.0
 * @项目：aq项目
 * @describe：
 * @author： 张霞
 * @createTime： 2018/03/05
 * @Copyright @2017 by zhangxia
 */
public class CheckSignatureUtil {
	
	public static final String token = "bifengmiaozhuan";

	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		ArrayList<String> list = new ArrayList<String>();
		list.add(token);
		list.add(timestamp);
		list.add(nonce);
		Collections.sort(list);
		StringBuilder content = new StringBuilder();
		for (String str : list) {
			content.append(str);
		}
		return signature.equals(HashUtil.hash(content.toString(), "SHA1"));
	}
}
