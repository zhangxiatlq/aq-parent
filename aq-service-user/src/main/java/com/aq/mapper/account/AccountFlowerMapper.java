package com.aq.mapper.account;

import com.aq.core.base.BaseMapper;
import com.aq.facade.dto.account.AccountFlowerDTO;
import com.aq.facade.entity.account.AccountFlow;
import com.aq.facade.vo.account.AccountFlowerVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 账户流水 mapper
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Repository
public interface AccountFlowerMapper extends BaseMapper<AccountFlow> {

    /**
     * 通过客户经理id查询账户流水
     *
     * @param accountFlowerDTO
     * @return java.util.List<com.aq.facade.vo.account.AccountFlowerVO>
     * @author 郑朋
     * @create 2018/2/12 0012
     */
    List<AccountFlowerVO> selectFlower(AccountFlowerDTO accountFlowerDTO);

    /**
     * @author: zhangxia
     * @desc: 查询所有账户流水（包括：客户经理，客户，管理员）
     * @params: [dto]
     * @methodName:allSelectFlower
     * @date: 2018/3/26 0026 下午 14:57
     * @return: java.util.List<com.aq.facade.vo.account.AccountFlowerVO>
     * @version:2.1.2
     */
    List<AccountFlowerVO> allSelectFlower(AccountFlowerDTO dto);
}