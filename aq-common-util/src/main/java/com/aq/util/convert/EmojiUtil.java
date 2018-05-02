package com.aq.util.convert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
* @ClassName: emoji 
* @Description:emoji表情编码转换 
* @author lijie
* @date 2017年9月1日 上午11:13:55 
*
 */
public class EmojiUtil {

	/**
	 * 
	* @Title: resolveToByteFromEmoji 
	* @Description: 将str中的emoji表情转为byte数组
	* @param @param str
	* @param @return    设定文件 
	* @return String    返回类型 
	* @author lijie
	* @throws
	 */
	public static String resolveToByteFromEmoji(String str) {
		Pattern pattern = Pattern.compile(
				"[^(\u2E80-\u9FFF\\w\\s`~!@#\\$%\\^&\\*\\(\\)_+-？（）——=\\[\\]{}\\|;。，、《》”：；“！……’:'\"<,>\\.?/\\\\*)]");
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, resolveToByte(matcher.group(0)));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 
	* @Title: resolveToEmojiFromByte 
	* @Description: 将str中的byte数组类型的emoji表情转为正常显示的emoji表情 
	* @param @param str
	* @param @return    设定文件 
	* @return String    返回类型 
	* @author lijie
	* @throws
	 */
	public static String resolveToEmojiFromByte(String str) {
		Pattern pattern = Pattern.compile("<:([[-]\\d*[,]]+):>");
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, resolveToEmoji(matcher.group(0)));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	private static String resolveToByte(String str) {
		byte[] b = str.getBytes();
		StringBuilder sb = new StringBuilder();
		sb.append("<:");
		for (int i = 0; i < b.length; i++) {
			if (i < b.length - 1) {
				sb.append(Byte.valueOf(b[i]).toString() + ",");
			} else {
				sb.append(Byte.valueOf(b[i]).toString());
			}
		}
		sb.append(":>");
		return sb.toString();
	}

	private static String resolveToEmoji(String str) {
		str = str.replaceAll("<:", "").replaceAll(":>", "");
		String[] s = str.split(",");
		byte[] b = new byte[s.length];
		for (int i = 0; i < s.length; i++) {
			b[i] = Byte.valueOf(s[i]);
		}
		return new String(b);
	}
}
