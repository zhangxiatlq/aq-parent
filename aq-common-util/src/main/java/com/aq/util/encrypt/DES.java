package com.aq.util.encrypt;


import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.AlgorithmParameterSpec;

@Slf4j
public class DES {

	// public static final String DES_ALGORITHM = "DES/CBC/PKCS5Padding";
	// 若采用NoPadding模式，data长度必须是8的倍数
	public static final String DES_ALGORITHM = "DES/CBC/NoPadding";

	/**
	 * DES-CBC模式加密
	 * 
	 * @param plainData
	 *            明文
	 * @param keyData
	 *            密钥
	 * @param ivData
	 *            初始化向量
	 * @return 密文
	 */
	public static final byte[] encryptCBC(byte[] plainData, byte[] keyData, byte[] ivData) {
		byte[] encryptData = null;

		try {
			Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivData);
			SecretKey key = new SecretKeySpec(keyData, "DES");// key
			cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			encryptData = cipher.doFinal(plainData);
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return encryptData;
	}

	/**
	 * DES-CBC模式解密
	 * 
	 * @param encryptData
	 *            密文
	 * @param keyData
	 *            密钥
	 * @param ivData
	 *            初始化向量
	 * @return 明文
	 */
	public static final byte[] decryptCBC(byte[] encryptData, byte[] keyData, byte[] ivData) {
		byte[] plainData = null;

		try {
			Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
			AlgorithmParameterSpec paramSpec = new IvParameterSpec(ivData);
			SecretKey key = new SecretKeySpec(keyData, "DES");// key
			cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			plainData = cipher.doFinal(encryptData);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return plainData;
	}

}
