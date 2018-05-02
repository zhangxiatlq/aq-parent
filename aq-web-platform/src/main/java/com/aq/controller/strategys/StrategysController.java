package com.aq.controller.strategys;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.aq.base.BaseController;
import com.aq.core.constant.CommonConstants;
import com.aq.core.constant.RoleCodeEnum;
import com.aq.facade.dto.StrategysDTO;
import com.aq.facade.dto.StrategysOperateDTO;
import com.aq.facade.dto.StrategysPushQueryDTO;
import com.aq.facade.dto.StrategysQueryDTO;
import com.aq.facade.service.IStrategyService;
import com.aq.facade.vo.StrategysPushQueryVO;
import com.aq.facade.vo.StrategysSelfSupportQueryVO;
import com.aq.facade.vo.permission.SessionUserVO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 策略后台接入层
 * @date 2018/1/22
 * @copyright by xkw
 */

@Slf4j
@Controller
@RequestMapping(value = "web/strategys")
public class StrategysController extends BaseController {
    @Reference(version = "1.0.0", check = false)
    private IStrategyService iStrategysService;

    /**
     * 自营策略页面
     *
     * @author 熊克文
     */
    @RequestMapping(value = "/self/page", method = RequestMethod.GET)
    public String toStrategysSelfSupportList() {
        return "strategys/selfSupportList";
    }

    /**
     * 自营策略推送页面
     *
     * @author 熊克文
     */
    @RequestMapping(value = "/pushStrategy", method = RequestMethod.GET)
    public String pushStrategy(Integer strategysId, Model model) {
        model.addAttribute("strategysId", strategysId);
        model.addAttribute("roleType", this.iStrategysService.strategysPushRoleType(strategysId).getData());
        return "strategys/pushStrategy";
    }


    /**
     * 自营策略分页查询
     *
     * @param pageBean 分页条件DTO
     * @param dto      查询条件DTO
     * @return {@link StrategysSelfSupportQueryVO}
     */
    @RequestMapping(value = "/pageStrategysSelfSupportQueryVO", method = RequestMethod.GET)
    @ResponseBody
    Result pageStrategysSelfSupportQueryVO(PageBean pageBean, StrategysQueryDTO dto) {
        Result result = this.iStrategysService.pageStrategysSelfSupportQueryVO(pageBean, dto);
        log.info("自营策略分页查询，result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 策略推送对象分页查询
     *
     * @param pageBean 分页条件DTO
     * @param dto      查询条件DTO
     * @return {@link StrategysPushQueryVO}
     */
    @RequestMapping(value = "/pageStrategysPushVO", method = RequestMethod.GET)
    @ResponseBody
    Result pageStrategysPushVO(PageBean pageBean, StrategysPushQueryDTO dto) {
        Result result = this.iStrategysService.pageStrategysPushVO(pageBean, dto);
        log.info("策略推送对象分页查询，result={}", JSON.toJSONString(result));
        return result;
    }

    /**
     * 编辑策略
     *
     * @param dto 编辑策略
     * @return {@link com.aq.util.result.Result}
     */
    @RequestMapping(value = "/editStrategys", method = RequestMethod.POST)
    @ResponseBody
    Result editStrategys(StrategysDTO dto) {
        return this.iStrategysService.editStrategys(dto);
    }

    /**
     * 审核策略
     *
     * @param dto 审核策略dto
     * @return {@link com.aq.util.result.Result}
     */
    @RequestMapping(value = "/auditStrategys", method = RequestMethod.POST)
    @ResponseBody
    Result auditStrategys(StrategysOperateDTO dto) {
        return this.iStrategysService.auditStrategys(dto);
    }

    /**
     * 查询单个策略
     *
     * @param id 主键id
     * @return {@link com.aq.util.result.Result}
     */
    @RequestMapping(value = "/getStrategys", method = RequestMethod.GET)
    @ResponseBody
    Result getStrategys(Integer id) {
        return this.iStrategysService.getStrategys(id);
    }

    /**
     * 添加策略
     *
     * @param dto 添加策略dto
     * @return {@link com.aq.util.result.Result}
     */
    @RequestMapping(value = "/addStrategys", method = RequestMethod.POST)
    @ResponseBody
    Result addStrategys(MultipartFile apk, StrategysDTO dto) {
        SessionUserVO userVO = getLoginUser();

        File dir = new File(CommonConstants.UPLOAD_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String fileName = System.currentTimeMillis() + "." + apk.getOriginalFilename().split("\\.")[1];
        String path = CommonConstants.UPLOAD_PATH + File.separator + fileName;

        File tempFile;
        try {
            tempFile = new File(path);

            apk.transferTo(tempFile);
            dto.setFileName(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("上传策略文件错误");
        }

        dto.setPublisherName(userVO.getUserName());
        dto.setPublisherPhoto("默认头像");
        dto.setCreateId(userVO.getId());

        return this.iStrategysService.addStrategys(dto);
    }

    /**
     * 删除策略
     *
     * @param id 策略id
     * @return {@link com.aq.util.result.Result}
     */
    @RequestMapping(value = "/delStrategys", method = RequestMethod.POST)
    @ResponseBody
    Result delStrategys(Integer id) {
        return this.iStrategysService.delStrategys(id);
    }

    /**
     * 推送客户或者客户经理策略接口
     *
     * @param strategysId   策略id
     * @param roleType      {@link com.aq.core.constant.UserTypeEnum}
     * @param userIds 推送人ids ','隔开
     * @param allChecked 是否全选
     * @return Result
     */
    @RequestMapping(value = "/pushStrategys", method = RequestMethod.POST)
    @ResponseBody
    Result pushStrategys(Integer strategysId, Byte roleType, String userIds, Boolean allChecked) {
        return this.iStrategysService.pushStrategys(strategysId, RoleCodeEnum.getRoleEnumByCode(roleType), userIds.split(","), allChecked);
    }

    /**
     * 文件下载
     *
     * @param fileName 下载路径
     */
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
}
