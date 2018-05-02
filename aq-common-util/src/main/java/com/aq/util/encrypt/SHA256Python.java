package com.aq.util.encrypt;

/* Example implementation of password hasher similar on Django's PasswordHasher
 * Requires Java8 (but should be easy to port to older JREs)
 * Currently it would work only for pbkdf2_sha256 algorithm
 */

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;


/**
 * @version 1.0
 * @项目：aq-facade-core
 * @描述：Python版的sha256加密
 * @作者： 张霞
 * @创建时间： 10:49 2018/1/22
 * @Copyright @2017 by zhangxia
 */
public class SHA256Python {
    public final static Integer DEFAULT_ITERATIONS = 36000;
    public final static String algorithm = "pbkdf2_sha256";
    public SHA256Python() {}

    public static String getEncodedHash(String password, String salt, int iterations) {
        // Returns only the last part of whole encoded password
        SecretKeyFactory keyFactory = null;
        try {
            keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Could NOT retrieve PBKDF2WithHmacSHA256 algorithm");
            System.exit(1);
        }
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(Charset.forName("UTF-8")), iterations, 256);
        SecretKey secret = null;
        try {
            secret = keyFactory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            System.out.println("Could NOT generate secret key");
            e.printStackTrace();
        }

        byte[] rawHash = secret.getEncoded();
        byte[] hashBase64 = Base64.getEncoder().encode(rawHash);

        return new String(hashBase64);
    }
    /**
     * make salt
     * @return String
     */
    private static String getsalt(){
        int length = 12;
        Random rand = new Random();
        char[] rs = new char[length];
        for(int i = 0; i < length; i++){
            int t = rand.nextInt(3);
            if (t == 0) {
                rs[i] = (char)(rand.nextInt(10)+48);
            } else if (t == 1) {
                rs[i] = (char)(rand.nextInt(26)+65);
            } else {
                rs[i] = (char)(rand.nextInt(26)+97);
            }
        }
        return new String(rs);
    }
    /**
     * rand salt
     * iterations is default 20000
     * @param password
     * @return
     */
    public synchronized static String encode(String password) {
        return encode(password, getsalt());
    }
    /**
     * rand salt
     * @param password
     * @return
     */
    public static String encode(String password,int iterations) {
        return encode(password, getsalt(),iterations);
    }
    /**
     * iterations is default 20000
     * @param password
     * @param salt
     * @return 生成动态默认不变化的字符串密码
     */
    public static String encode(String password, String salt) {
        return encode(password, salt, DEFAULT_ITERATIONS);
    }
    /**
     *
     * @param password
     * @param salt
     * @param iterations
     * @return
     */
    public static String encode(String password, String salt, int iterations) {
        // returns hashed password, along with algorithm, number of iterations and salt
        String hash = getEncodedHash(password, salt, iterations);
        return String.format("%s$%d$%s$%s", algorithm, iterations, salt, hash);
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        // hashedPassword consist of: ALGORITHM, ITERATIONS_NUMBER, SALT and
        // HASH; parts are joined with dollar character ("$")
        String[] parts = hashedPassword.split("\\$");
        if (parts.length != 4) {
            // wrong hash format
            return false;
        }
        Integer iterations = Integer.parseInt(parts[1]);
        String salt = parts[2];
        String hash = encode(password, salt, iterations);

        return hash.equals(hashedPassword);
    }


//    public static void main(String[] args) {
//        runTests();
//    }


    // Following examples can be generated at any Django project:
    //
    //  >>> from django.contrib.auth.hashers import make_password
    //  >>> make_password('mystery', hasher='pbkdf2_sha256')  # salt would be randomly generated
    //  'pbkdf2_sha256$10000$HqxvKtloKLwx$HdmdWrgv5NEuaM4S6uMvj8/s+5Yj+I/d1ay6zQyHxdg='
    //  >>> make_password('mystery', salt='mysalt', hasher='pbkdf2_sha256')
    //  'pbkdf2_sha256$10000$mysalt$KjUU5KrwyUbKTGYkHqBo1IwUbFBzKXrGQgwA1p2AuY0='
    //
    //
    // mystery
    // pbkdf2_sha256$10000$qx1ec0f4lu4l$3G81rAm/4ng0tCCPTrx2aWohq7ztDBfFYczGNoUtiKQ=
    //
    // s3cr3t
    // pbkdf2_sha256$10000$BjDHOELBk7fR$xkh1Xf6ooTqwkflS3rAiz5Z4qOV1Jd5Lwd8P+xGtW+I=
    //
    // puzzle
    // pbkdf2_sha256$10000$IFYFG7hiiKYP$rf8vHYFD7K4q2N3DQYfgvkiqpFPGCTYn6ZoenLE3jLc=
    //
    // riddle
    // pbkdf2_sha256$10000$A0S5o3pNIEq4$Rk2sxXr8bonIDOGj6SU4H/xpjKHhHAKpFXfmNZ0dnEY=
    private static void runTests() {
        System.out.println(SHA256Python.encode("123456", 36000));//每次生成不一样的sha256
        System.out.println("===========================");
        System.out.println("= Testing password hasher =");
        System.out.println("===========================");
        System.out.println();
        String result = SHA256Python.encode("123456", "14TJHLYmGxXo", 36000);//每次生成一样的sha256
        System.out.println(result);
//        String result1 = (new SHA256Python()).encode("123456");//每次生成一样的sha256
        String result1 = SHA256Python.encode("asdfghjkl123");//每次生成一样的sha256
        System.out.println(result1);
//        System.out.println();
        passwordShouldMatch("123456", "pbkdf2_sha256$36000$lWoy49aH3dR1$czOoWJSJ0Hbym+VISnkAg3BADLqHdbuN4o7nrXk/WpE=");
        passwordShouldMatch("123456", "pbkdf2_sha256$36000$QH0cl8o7eUPu$fDgchVr4E+vY+A3URVfTJkNFD5Vx+pdF4rOzPBJNh9M=");
        passwordShouldMatch("asdfghjkl123", "pbkdf2_sha256$36000$K0R5G5jBoJ5N$BEFxfBEGNIXHMh2nUvBvUVOU6ru89ovgLgajYnavZV4=");
//        passwordShouldMatch("mystery", "pbkdf2_sha256$10000$qx1ec0f4lu4l$3G81rAm/4ng0tCCPTrx2aWohq7ztDBfFYczGNoUtiKQ=");
//        passwordShouldMatch("mystery", "pbkdf2_sha256$10000$mysalt$KjUU5KrwyUbKTGYkHqBo1IwUbFBzKXrGQgwA1p2AuY0=");  // custom salt
//        passwordShouldMatch("s3cr3t", "pbkdf2_sha256$10000$BjDHOELBk7fR$xkh1Xf6ooTqwkflS3rAiz5Z4qOV1Jd5Lwd8P+xGtW+I=");
//        passwordShouldMatch("puzzle", "pbkdf2_sha256$10000$IFYFG7hiiKYP$rf8vHYFD7K4q2N3DQYfgvkiqpFPGCTYn6ZoenLE3jLc=");
//        passwordShouldMatch("riddle", "pbkdf2_sha256$10000$A0S5o3pNIEq4$Rk2sxXr8bonIDOGj6SU4H/xpjKHhHAKpFXfmNZ0dnEY=");
    }

    private static void passwordShouldMatch(String password, String expectedHash) {
        if (checkPassword(password, expectedHash)) {
            System.out.println(" => OK");
        } else {
            String[] parts = expectedHash.split("\\$");
            if (parts.length != 4) {
                System.out.printf(" => Wrong hash provided: '%s'\n", expectedHash);
                return;
            }
            String salt = parts[2];
            String resultHash = SHA256Python.encode(password, salt);
            String msg = " => Wrong! Password '%s' hash expected to be '%s' but is '%s'\n";
            System.out.printf(msg, password, expectedHash, resultHash);
        }
    }
}
