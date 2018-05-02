package com.aq.util.wechat.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @ClassName: SignUtil
 * @Description: 微信公众平台Token验证
 * @author: lijie
 * @date: 2018年3月14日 下午2:42:28
 */
@Slf4j
public class SignUtil {

	/**
	 * 
	* @Title: checkSignature  
	* @Description: 验证签名 
	* @param: @param token
	* @param: @param signature
	* @param: @param timestamp
	* @param: @param nonce
	* @param: @return
	* @return boolean
	* @author lijie
	* @throws
	 */
	public static boolean checkSignature(String token, String signature, String timestamp, String nonce) {
		String[] arr = new String[] { token, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			log.error("微信公众平台Token验证/验证签名失败", e);
		}
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}

    /**
     * 
    * @Title: byteToStr  
    * @Description: 将字节数组转换为十六进制字符串 
    * @param: @param byteArray
    * @param: @return
    * @return String
    * @author lijie
    * @throws
     */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

    /**
     * 
    * @Title: byteToHexStr  
    * @Description: 将字节转换为十六进制字符串 
    * @param: @param mByte
    * @param: @return
    * @return String
    * @author lijie
    * @throws
     */
	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];
		return new String(tempArr);
	}
}
