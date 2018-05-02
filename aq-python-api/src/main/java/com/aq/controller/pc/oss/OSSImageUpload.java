package com.aq.controller.pc.oss;

import com.aq.facade.vo.customer.QueryGroupVO;
import com.aq.util.oss.OSSClientUtil;
import com.aq.util.result.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * oss 图片上传
 *
 * @author 郑朋
 * @create 2018/2/22 0022
 **/

@RestController
@RequestMapping("/api/pc/images")
@Api(value = "图片上传接口", description = "图片上传接口")
@Slf4j
public class OSSImageUpload {

    @Autowired
    OSSClientUtil ossClientUtil;

    /**
     * 5M
     */
    private static final int MAX_POST_SIZE = 5 * 1024 * 1024;


    public static synchronized String getOrder() {
        return DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(6);
    }

    /**
     * 上传图片
     *
     * @param file
     * @param module
     * @return Result
     * @author 郑朋
     * @create 2018/2/22 0022
     */

    @ApiOperation(value = "上传图片", notes = "[郑朋]上传图片接口")
    @ApiImplicitParams({
            @ApiImplicitParam(dataType = "String", name = "module", value = "上传模块名称", paramType = "query", required = true),
            @ApiImplicitParam(dataType = "file", name = "file", value = "图片文件", paramType = "query", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(code = 10100, message = "请求参数有误"),
            @ApiResponse(code = 200, message = "操作成功", response = QueryGroupVO.class)
    })
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result springUploadFile(MultipartFile file, String module) {

        String fileName = null;

        //判断包名，如果为空返回
        if (module == null || "".equals(module)) {
            return new Result(false, "1", "请先自定义module");
        }

        try {
            if (file.isEmpty()) {
                return new Result(false, "1", "图片不存在");
            } else {
                //如果上传图片大于5M不予上传
                if (file.getSize() > MAX_POST_SIZE) {
                    return new Result(false, "1", "上传图片不能大于5M");
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String date = sdf.format(new Date());
                String url = module + "/" + date;
                String path;
                fileName = file.getOriginalFilename();
                boolean isFile = StringUtils.endsWithAny(StringUtils.lowerCase(fileName), new String[]{".png", ".jpg", ".jpeg", ".bmp", ".gif"});
                String sysFileName = getOrder() + "." + fileName.substring((fileName.lastIndexOf(".") + 1));
                if (isFile) {
                    path = url + "/" + sysFileName;
                    Object address = ossClientUtil.putObject(path, file);
                    return new Result(true, address, 0);
                } else {
                    return new Result(false, "1", "图片格式不正确");
                }
            }
        } catch (Exception e) {
            log.info("上传图片异常, e={}", e);
            return new Result(false, "1", "上传失败");
        }
    }
}
