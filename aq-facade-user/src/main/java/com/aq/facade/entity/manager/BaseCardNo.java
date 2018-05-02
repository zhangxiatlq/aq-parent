package com.aq.facade.entity.manager;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 身份证信息
 *
 * @author 郑朋
 * @create 2018/2/23 0023
 **/
@Data
@Table(name = "aq_base_cardno")
public class BaseCardNo implements Serializable {
    private static final long serialVersionUID = -1193004132342106312L;
    /**
     * 主键id
     */
    @Id
    private Integer id;

    /**
     * 真实姓名
     */
    @Column(name = "realName")
    private String realName;

    /**
     * 身份证号
     */
    @Column(name = "cardNo")
    private String cardNo;

}
