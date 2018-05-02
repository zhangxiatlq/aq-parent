package com.aq.facade.entity.system;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 机构宣言表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@Table(name = "aq_declaration")
public class Declaration implements Serializable {
    private static final long serialVersionUID = 8894398128151388024L;

    @Id
    private Integer id;

    /**
     * 宣言内容
     */
    @Column(name = "content")
    private String content;

}