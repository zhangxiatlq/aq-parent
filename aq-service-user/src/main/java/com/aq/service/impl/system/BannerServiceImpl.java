package com.aq.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.aq.facade.service.system.IBannerService;
import com.aq.mapper.system.BannerMapper;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * banner service.impl
 *
 * @author 郑朋
 * @create 2018/2/9 0009
 **/
@Slf4j
@Service(version = "1.0.0")
public class BannerServiceImpl implements IBannerService {

    @Autowired
    private BannerMapper bannerMapper;

    @Override
    public Result getBanner() {
        Result result = ResultUtil.getResult(RespCode.Code.FAIL, new ArrayList<>());
        try {
            result = ResultUtil.getResult(RespCode.Code.SUCCESS, bannerMapper.selectAllBanner());
        } catch (Exception e) {
            log.error("获取banner 图异常：e={}", e);
        }
        log.info("获取banner 图返回值：result={}", JSON.toJSONString(result));
        return result;
    }
}
