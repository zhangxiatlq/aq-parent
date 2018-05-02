/**
 *
 */
package com.aq.core.base;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @项目：phshopping-common-core
 * @描述：实体base类
 * @作者： Mr.chang
 * @创建时间：2017年3月8日
 * @Copyright @2017 by Mr.chang
 * @author Mr.Chang
 */
@Data
public class BaseEntity implements Serializable {

    public final static String[] BASE_FIELD_STRINGS = new String[]{"id", "createrId", "createTime", "updaterId", "updateTime"};
    /**
     *
     */
    private static final long serialVersionUID = -5300113985007593228L;

    @Column(name = "createTime")
    private Date createTime;

    @Id
    private Integer id;

    @Column(name = "updateTime")
    private Date updateTime;

    @Column(name = "createrId")
    private Integer createrId;

    @Column(name = "updaterId")
    private Integer updaterId;

    /**
     * 实体验证
     * @return
     * @author Mr.Chang
     */
    public List<String> validateForm() {
        List<String> errorList = new ArrayList<>(0);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<BaseEntity>> constraintViolation = validator.validate(this);
        if (constraintViolation.size() > 0){
            errorList = new ArrayList<String>(constraintViolation.size());
        }
        for (ConstraintViolation<BaseEntity> violation : constraintViolation) {
            errorList.add(violation.getMessage());
        }
        return errorList;
    }
}