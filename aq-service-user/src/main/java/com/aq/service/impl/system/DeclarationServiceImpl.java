package com.aq.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.system.IDeclarationService;
import com.aq.mapper.system.DeclarationMapper;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * declaration service.impl
 *
 * @author 郑朋
 * @create 2018/2/9 0009
 **/

@Slf4j
@Service(version = "1.0.0")
public class DeclarationServiceImpl implements IDeclarationService {

    @Autowired
    private DeclarationMapper declarationMapper;

    @Override
    public Result getDeclaration() {
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            String content = declarationMapper.selectAll().get(0).getContent();
            result = ResultUtil.getResult(RespCode.Code.SUCCESS);
            result.setData(content);
        } catch (Exception e) {
            log.error("获取机构宣言异常：e={}", e);
        }
        log.info("获取机构宣言返回值：result={}", JSON.toJSONString(result));
        return result;
    }
}
