package com.aq.facade.service.manager;

import com.aq.facade.dto.manage.BindBankDTO;
import com.aq.facade.dto.manage.UpdateManagerDTO;
import com.aq.util.result.Result;

/**
 * 客户经理信息service
 *
 * @author 郑朋
 * @create 2018/2/8 0008
 **/
public interface IManagerInfoService {

    /**
     * 通过客户经理id查询客户经理基本信息(包含银行卡、实名认证）
     *
     * @param managerId
     * @return com.aq.util.result.Result {@link com.aq.facade.vo.manage.ManagerBaseInfoVO}
     * @author 郑朋
     * @create 2018/2/10 0010
     */
    Result getManagerInfo(Integer managerId);


    /**
     * 绑定银行卡
     *
     * @param bindBankDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/23 0023
     */
    Result bindBankForManager(BindBankDTO bindBankDTO);

    /**
     * 修改客户经理信息（可自行扩展）
     *
     * @param updateManagerDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/24 0024
     */
    Result modifyManager(UpdateManagerDTO  updateManagerDTO);

}
