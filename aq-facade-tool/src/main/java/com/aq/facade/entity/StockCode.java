package com.aq.facade.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @项目：aq-parent
 * @描述：
 * @创建时间：2018/1/20
 * @Copyright @2017 by Mr.chang
 * @author Mr.chang
 */
@Data
@Table(name = "all_code")
public class StockCode implements Serializable{

	@Id
    private Integer id;

    /**
     * 股票编码
     */
    @Column(name = "code")
    private String code;

    /**
     * 股票名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 行业
     */
    @Column(name = "hangye")
    private String hangye;

}
