package com.aq.facade.vo;

import com.aq.core.base.BaseValidate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 后台
 * @date 2018/1/20
 * @copyright by xkw
 */
@Data
public class StrategysVO extends BaseValidate {

    /**
     * 策略id
     */
    private Integer id;

    /**
     * 策略名称
     */
    @NotBlank(message = "策略名称不能为空")
    private String strategyName;

    /**
     * 策略文件
     */
    private String fileName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    /**
     * 策略价格
     */
    @NotNull(message = "策略价格不能为空")
    private BigDecimal price;

    /**
     * 策略状态 0位系统策略 1为用户自定义策略 2为用户引导式策略，3待定，4客户经理策略
     * {@link com.aq.core.constant.BoutiqueTypeEnum}
     */
    private Byte status;

    /**
     * 发布人
     */
    private String publisherName;

    /**
     * 发布人头像
     */
    private String publisherPhoto;

    /**
     * 创建人
     */
    private Integer createId;
}
