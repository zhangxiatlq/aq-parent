package com.aq.controller.pc;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aq.core.constant.CommonConstants;
import com.aq.facade.service.IStrategyService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 文件接入
 * @date 2018/1/27
 * @copyright by xkw
 */

@Slf4j
@RestController
@RequestMapping("api/file")
@Api(value = "文件接口", description = "文件接口")
public class FileController {

    @Reference(version = "1.0.0")
    private IStrategyService iBoutiqueStrategyService;

    @ApiOperation(value = "文件下载", notes = "[熊克文]文件下载")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "fileName", value = "文件名称", paramType = "query", required = true),
    })
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletResponse res, String fileName) throws IOException {
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("utf-8"), "iso-8859-1"));
        BufferedInputStream bis = null;
        OutputStream os;
        try {
            os = res.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(new File(CommonConstants.UPLOAD_PATH + File.separator + fileName)));
            byte[] buff = new byte[bis.available()];
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @ApiOperation(value = "文件上传", notes = "[熊克文]文件上传")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "操作成功 返回文件名称")
    })
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = {"application/json"})
    public String upload(@RequestParam("file") MultipartFile file) {
        File dir = new File(CommonConstants.UPLOAD_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String fileName = System.currentTimeMillis() + "." + file.getOriginalFilename().split("\\.")[1];
        String path = CommonConstants.UPLOAD_PATH + File.separator + fileName;

        File tempFile;
        try {
            tempFile = new File(path);
            file.transferTo(tempFile);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传策略文件错误");
        }

        return null;
    }
}
