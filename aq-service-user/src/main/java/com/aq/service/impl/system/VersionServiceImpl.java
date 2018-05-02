package com.aq.service.impl.system;

import com.alibaba.dubbo.config.annotation.Service;
import com.aq.facade.entity.system.Version;
import com.aq.facade.service.system.IVersionService;
import com.aq.mapper.system.VersionMapper;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 版本接口实现类
 *
 * @author 熊克文
 * @create 2018/2/9
 **/

@Slf4j
@Service(version = "1.0.0")
public class VersionServiceImpl implements IVersionService {

    @Autowired
    private VersionMapper versionMapper;


    @Override
    public Result getRecentVersionCode() {
        Version version = this.versionMapper.selectAll().get(0);
        if (version == null) {
            version = new Version();
        }
        return ResultUtil.getResult(RespCode.Code.SUCCESS, version.getVersionCode());
    }
}