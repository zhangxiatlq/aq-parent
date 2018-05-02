package com.aq.core.base.author;

/**
 * @author Mr.Shu
 * @packageName com.aq.core.base.vaildate
 * @description
 * @date 2017/12/8 10:33
 * @Copyright @2017 by Mr.Shu
 */
public interface IRuler {

    /**
     * 获取返回信息
     *
     * @return {@link String}
     * @author Mr.Shu
     * @date 2017/12/8 15:43
     */
    String getError();

    /**
     * 校验
     *
     * @param validatorPara {@link ValidatorCenter.ValidatorPara}
     * @return {@link Boolean}
     * @author Mr.Shu
     * @date 2017/12/8 15:43
     */
    Boolean authorize(ValidatorCenter.ValidatorPara validatorPara);

}
