package com.aq.service.impl.customer;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.facade.dto.customer.DeleteImportDTO;
import com.aq.facade.exception.permission.UserBizException;
import com.aq.facade.service.customer.IFailureCustomerService;
import com.aq.facade.vo.customer.ImportFailureRecordVO;
import com.aq.mapper.customer.ImportFailureRecordMapper;
import com.aq.util.page.PageBean;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 导入失败客户记录service.impl
 *
 * @author 郑朋
 * @create 2018/2/9 0009
 **/
@Service(version = "1.0.0")
@Slf4j
public class FailureCustomerServiceImpl implements IFailureCustomerService {


    @Autowired
    private ImportFailureRecordMapper importFailureRecordMapper;

    @Override
    public Result getFailureCustomerByPage(PageBean pageBean, Integer managerId) {
        log.info("导入失败的客户记录列表入参：pageBean={},managerId={}", JSON.toJSONString(pageBean), managerId);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            PageHelper.startPage(pageBean.getPageNum(), pageBean.getPageSize());
            List<ImportFailureRecordVO> list = importFailureRecordMapper.selectRecordByList(managerId);
            PageInfo<ImportFailureRecordVO> pageInfo = new PageInfo<>(list);
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, pageInfo.getList(), pageInfo.getTotal());
        } catch (Exception e) {
            log.error("导入失败的客户记录列表异常：e={}", e);
        }
        log.info("导入失败的客户记录列表返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @Override
    public Result getFailureCustomerByList(Integer managerId) {
        log.info("导入失败的客户记录列表入参：managerId={}", managerId);
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, importFailureRecordMapper.selectRecordByList(managerId));
        } catch (Exception e) {
            log.error("导入失败的客户记录列表异常：e={}", e);
        }
        log.info("导入失败的客户记录列表返回值：result={}", JSON.toJSONString(result));
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result deleteFailureCustomer(DeleteImportDTO deleteImportDTO) {
        log.info("批量删除导入失败的客户记录入参：deleteImportDTO={}", JSON.toJSONString(deleteImportDTO));
        Result result;
        try {
            //参数校验
            String message = deleteImportDTO.validateForm();
            if (StringUtils.isNotBlank(message)) {
                result = ResultUtil.getResult(RespCode.Code.REQUEST_DATA_ERROR);
                result.setMessage(message);
                return result;
            }
            for (Integer id : deleteImportDTO.getList()) {
                importFailureRecordMapper.deleteByPrimaryKey(id);
            }
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            log.info("批量删除导入失败的客户记录返回值：result={}", JSON.toJSONString(result));
            return result;
        } catch (Exception e) {
            log.error("批量删除导入失败的客户记录异常：e={}", e);
            throw new UserBizException(RespCode.Code.INTERNAL_SERVER_ERROR);
        }
    }
}
