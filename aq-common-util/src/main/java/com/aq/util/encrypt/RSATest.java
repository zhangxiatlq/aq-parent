package com.aq.util.encrypt;

import org.apache.commons.codec.binary.Base64;

public class RSATest {
	
	
	public static void main(String[] args) {

		String source = "queywquehqwieqw21318298080";
		String password = "123456";
		try {
			byte[] datas = RSAUtils.encryptByPublicKey(password.getBytes(), RSAUtils.PUBLICKEY, "js");
			String passwordsign = Base64.encodeBase64String(datas);
			System.out.println(passwordsign);
			byte[] signs = RSAUtils.encryptByPublicKey((passwordsign+source).getBytes(), RSAUtils.PUBLICKEY, "js");
			String sign = Base64.encodeBase64String(signs);
			System.out.println(sign);
			byte[] datas1 = RSAUtils.decryptByPrivateKey(Base64.decodeBase64(sign), RSAUtils.PRIVATEKEY);
			System.out.println(new String(datas1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}









