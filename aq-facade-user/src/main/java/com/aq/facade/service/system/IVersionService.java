package com.aq.facade.service.system;

import com.aq.util.result.Result;

/**
 * 版本接口
 *
 * @author 熊克文
 * @createDate 2018\2\26 0026
 **/
public interface IVersionService {

    /**
     * 得到最新的版本Code
     *
     * @return String
     */
    Result getRecentVersionCode();
}
