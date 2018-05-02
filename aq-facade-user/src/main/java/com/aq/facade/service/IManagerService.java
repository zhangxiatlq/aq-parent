package com.aq.facade.service;

import com.aq.facade.dto.AddManagerDTO;
import com.aq.facade.dto.UpdateManagerDTO;
import com.aq.facade.dto.manage.SelectManagerDTO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * @version 1.0
 * @项目：aq-facade-user
 * @描述：客户经理接口
 * @作者： 张霞
 * @创建时间： 16:37 2018/1/19
 * @Copyright @2017 by zhangxia
 */
public interface IManagerService {

    /**
     * @author: 张霞
     * @description：翻页获取客户经理列表
     * @createDate: 21:19 2018/1/21
     * @param pageBean
     * @param selectManagerDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    Result getManagerListByPage(PageBean pageBean, SelectManagerDTO selectManagerDTO);

    /**
     * @author: 张霞
     * @description：后台添加客户经理
     * @createDate: 21:44 2018/1/21
     * @param addManagerDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    Result addManager(AddManagerDTO addManagerDTO);

    /**
     * @author: 张霞
     * @description：后台获取客户经理的详细信息
     * @createDate: 23:19 2018/1/21
     * @param selectManagerDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    Result getManagerDetail(SelectManagerDTO selectManagerDTO);

    /**
     * @author: 张霞
     * @description：编辑客户经理信息
     * @createDate: 9:53 2018/1/22
     * @param updateManagerDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    Result editManager(UpdateManagerDTO updateManagerDTO);

    /**
     * @author: 张霞
     * @description：获取所有客户经理
     * @createDate: 21:13 2018/1/22
     * @param
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    Result getAllManager();
    /**
     * @author: 张霞
     * @description：客户经理重置密码
     * @createDate: 17:24 2018/1/23
     * @param updateManagerDTO
     * @return: com.aq.util.result.Result
     * @version: 2.1
     */
    Result resetPWD(UpdateManagerDTO updateManagerDTO);
}
