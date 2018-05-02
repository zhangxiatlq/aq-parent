package com.aq.util.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码 工具
 *
 * @author 郑朋
 * @create 2018/2/25 0025
 **/
@Slf4j
public class QRCodeUtil {
    //二维码颜色
    private static final int BLACK = 0xFF000000;
    //二维码颜色
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * <span style="font-size:18px;font-weight:blod;">ZXing 方式生成二维码</span>
     *
     * @param text     <a href="javascript:void();">二维码内容</a>
     * @param width    二维码宽
     * @param height   二维码高
     * @param request
     * @param response
     */
    public static void zxingCodeCreate(HttpServletRequest request, HttpServletResponse response, String text,
                                       int width, int height) {
        Map<EncodeHintType, String> his = new HashMap<>(16);
        log.info("二维码生成text = {}", text);
        // 设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //设置编码字符集
        his.put(EncodeHintType.CHARACTER_SET, "utf-8");
        OutputStream out = null;
        try {
            //1、生成二维码
            BitMatrix encode = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, his);

            //2、获取二维码宽高
            int codeWidth = encode.getWidth();
            int codeHeight = encode.getHeight();

            //3、将二维码放入缓冲流
            BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < codeWidth; i++) {
                for (int j = 0; j < codeHeight; j++) {
                    //4、循环将二维码内容定入图片
                    image.setRGB(i, j, encode.get(i, j) ? BLACK : WHITE);
                }
            }
            try {
                out = response.getOutputStream();
                ImageIO.write(image, "jpg", out);
            } catch (IOException ex) {
                log.error("生成验证码图片出错", ex);
            } finally {
                if (null != out) {
                    out.flush();
                    out.close();
                }
            }
        } catch (WriterException e) {
            log.error("二维码生成失败, e={}", e);
        } catch (IOException e) {
            log.error("关闭流异常, e={}", e);
        }
    }


    public static void main(String[] args) throws Exception {
        /**
         *    QRcode 二维码生成测试
         *    QRCodeUtil.QRCodeCreate("http://blog.csdn.net/u014266877", "E://qrcode.jpg", 15, "E://icon.png");
         */
        /**
         *     QRcode 二维码解析测试
         *    String qrcodeAnalyze = QRCodeUtil.QRCodeAnalyze("E://qrcode.jpg");
         */
        /**
         * ZXingCode 二维码生成测试
         * QRCodeUtil.zxingCodeCreate("http://blog.csdn.net/u014266877", 300, 300, "E://zxingcode.jpg", "jpg");
         */
        /**
         * ZxingCode 二维码解析
         *    String zxingAnalyze =  QRCodeUtil.zxingCodeAnalyze("E://zxingcode.jpg").toString();
         */
        //QRCodeUtil.zxingCodeCreate("http://blog.csdn.net/u014266877", 300, 300, "E://zxingcode.jpg", "jpg");
        System.out.println("success");

    }
}
