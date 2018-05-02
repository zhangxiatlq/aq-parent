package com.aq.facade.service.account;

import com.aq.facade.dto.account.AccountFlowerDTO;
import com.aq.facade.dto.account.DrawBalanceDTO;
import com.aq.facade.entity.account.AddAccountFlowerDTO;
import com.aq.util.page.PageBean;
import com.aq.util.result.Result;

/**
 * 账户流水 service
 *
 * @author 郑朋
 * @create 2018/2/12 0012
 **/
public interface IAccountFlowerService {

    /**
     * 获取账户流水
     *
     * @param pageBean
     * @param accountFlowerDTO
     * @return com.aq.util.result.Result {@link com.aq.facade.vo.account.ManagerFlowerVO}
     * @author 郑朋
     * @create 2018/2/12 0012
     */
    Result getAccountFlower(PageBean pageBean, AccountFlowerDTO accountFlowerDTO);

    /**
     * 获取账户流水
     *
     * @param pageBean
     * @param accountFlowerDTO
     * @return com.aq.util.result.Result {@link com.aq.facade.vo.account.AccountFlowerVO}
     * @author 郑朋
     * @create 2018/2/12 0012
     */
    Result getAccountFlowerList(PageBean pageBean, AccountFlowerDTO accountFlowerDTO);
    /**
     * @author: zhangxia
     * @desc: 获取所有账户流水
     * @params: [pageBean, accountFlowerDTO]
     * @methodName:getAccountFlowerList
     * @date: 2018/3/26 0026 下午 16:43
     * @return: com.aq.util.result.Result
     * @version:2.1.2
     */
    Result getAllAccountFlowerList(PageBean pageBean, AccountFlowerDTO accountFlowerDTO);



    /**
     * 提现申请
     *
     * @param drawBalanceDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/12 0012
     */
    Result drawBalance(DrawBalanceDTO drawBalanceDTO);

    /**
     * 余额信息
     *
     * @param managerId
     * @return com.aq.util.result.Result {@link com.aq.facade.vo.account.DrawCashVO}
     * @author 郑朋
     * @create 2018/2/12 0012
     */
    Result getBalanceInfo(Integer managerId);

    /**
     * 新增账户流水
     *
     * @param addAccountFlowerDTO
     * @return com.aq.util.result.Result
     * @author 郑朋
     * @create 2018/2/12 0012
     */
    Result addAccountFlower(AddAccountFlowerDTO addAccountFlowerDTO);
}
