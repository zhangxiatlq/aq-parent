package com.aq.util.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author zhang
 * @oss上传工具类
 */
@Component
public class OSSClientUtil {

    public static Log log = LogFactory.getLog(OSSClientUtil.class);

    private static OSSClient client = null;
    private static final String ACCESS_ID = "LTAIrcq4jRb5EVzG";
    private static final String ACCESS_KEY = "LxRfVPeoDsQNeLCqQwDuC4E8gMcSLD";
    /**
     * oss外网正式上传地址
     */
    private static final String OSS_ENDPOINT = "http://oss-cn-shenzhen.aliyuncs.com";
    //oss内网上传地址
    //private static final String OSS_ENDPOINT = "http://oss-cn-shenzhen-internal.aliyuncs.com";
    /**
     * 正式基础访问路径
     */
    public static String BASE_URL = "https://aq-images.oss-cn-shenzhen.aliyuncs.com/";
    /**
     * 正式bucket名
     */
    public static String BUCKET_NAME = "aq-images";

    /**
     * 地区
     */
    private static final String REGION_ID = "cn-shenzhen";
    /**
     * 角色key
     */
    private static final String ROLE_ACCESS_ID = "LTAI2sRrPNkW5fvw";
    /**
     * 角色秘钥
     */
    private static final String ROLE_ACCESS_KEY = "MTSLPU6BeU5FTqFyULe3loa4UFpchc";
    /**
     * 角色
     */
    private static final String ROLE_ARN = "acs:ram::1032008405331574:role/vulcan-oss";


    /**
     * @param url(模块名+YYYYMMDD+图片名)
     * @param file
     * @throws FileNotFoundException
     * @上传文件
     * @author Mr.Chang
     * @since 2017年3月26日
     */
    public String putObject(String url, MultipartFile file) {
        if (client == null) {
            client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
            // 设置bucket的访问权限，public-read-write权限
            client.setBucketAcl(BUCKET_NAME, CannedAccessControlList.PublicRead);
        }
        return up(client, url, file);

    }

    /**
     * 输入流上传文件
     *
     * @param client
     * @param url
     * @param in
     * @return
     * @author Mr.Chang
     */
    public String up(OSSClient client, String url, InputStream in) {
        try {
            if (client == null) {
                client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
                // 设置bucket的访问权限，public-read-write权限
                client.setBucketAcl(BUCKET_NAME, CannedAccessControlList.PublicRead);
            }

            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();

            // 必须设置ContentLength
            meta.setContentLength(in.available());

            // 上传Object.
            client.putObject(BUCKET_NAME, url, in, meta);
            // 打印ETag
            return BASE_URL + url;
        } catch (Exception e) {
            log.error("系统错误:上传OSS图片错误" + e);
            return null;
        }
    }

    /**
     * multipartFile上传文件
     *
     * @param client
     * @param url
     * @param file
     * @return
     * @author Mr.Chang
     */
    public String up(OSSClient client, String url, MultipartFile file) {
        try {
            // 初始化OSSClient
            if (client == null) {
                client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
                // 设置bucket的访问权限，public-read-write权限
                client.setBucketAcl(BUCKET_NAME, CannedAccessControlList.PublicRead);
            }
            // 获取指定文件的输入流
            InputStream content = file.getInputStream();

            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();

            // 必须设置ContentLength
            meta.setContentLength(file.getSize());

            // 上传Object.
            client.putObject(BUCKET_NAME, url, content, meta);
            // 打印ETag
            return BASE_URL + url;
        } catch (Exception e) {
            log.error("系统错误:上传OSS图片错误" + e);
            return null;
        }
    }

    /**
     * @param key
     * @删除oss的图片
     * @author Mr.Chang
     * @since 2017年3月26日
     */
    public static void deleteObject(String key) {
        if (client == null) {
            client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
            // 设置bucket的访问权限，public-read-write权限
            client.setBucketAcl(BUCKET_NAME, CannedAccessControlList.PublicReadWrite);
        }
        client.deleteObject(BUCKET_NAME, key);
    }

    /**
     * @描述：获取STSToken临时认证
     * @param: null
     * @return:
     * @author： Mr.Shu
     * @创建时间：2017/10/31 10:15
     */
    public static AssumeRoleResponse getSTSToken() {
        try {
            // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
            IClientProfile profile = DefaultProfile.getProfile(REGION_ID, ROLE_ACCESS_ID, ROLE_ACCESS_KEY);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            // 创建一个 AssumeRoleRequest 并设置请求参数
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setProtocol(ProtocolType.HTTPS);
            long time = 3600;
            request.setDurationSeconds(time);
            request.setRoleArn(ROLE_ARN);
            request.setRoleSessionName("vul-OSS");
            // 发起请求，并得到response
            final AssumeRoleResponse response = client.getAcsResponse(request);
            return response;
        } catch (ClientException e) {
            log.error("获取STSToken错误" + e);
        }
        return null;
    }

    public static void main(String[] args) {
	/*	String str="http://image.jsbn.com/WebImage/cq/jpg/20150821/44980352111163502374/20150821191413486574_800x1200.jpg";
		str.replace(str.substring(0, str.lastIndexOf("/")), "http://img2.jsbn.com/venus/pringles/20160307");
		System.out.println(str);*/
		/*OSSClientUtil ot=new OSSClientUtil();
		File f=new File("G:\\download\\test.jpg");
		InputStream is = null;
		try {
			is = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		ot.up(client, "agent/test.jpg",is);*/

    }
}
