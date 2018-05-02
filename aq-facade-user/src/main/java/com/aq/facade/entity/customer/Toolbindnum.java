package com.aq.facade.entity.customer;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
/**
 * 
 * @ClassName: Toolbindnum
 * @Description: 客户工具推荐绑定设置
 * @author: lijie
 * @date: 2018年2月10日 下午3:18:16
 */
@Data
@Table(name="aq_toolbindnum")
public class Toolbindnum implements Serializable {
    /**
	 * @fieldName: serialVersionUID
	 * @fieldType: long
	 * @Description: TODO
	 */
	private static final long serialVersionUID = 3363611836616451842L;
	/**
     * 主键ID
     */
	@Id
    private Integer id;
    /**
     * 工具类别编码
     */
	@Column(name="toolCode")
    private String toolCode;
    /**
     * 客户ID
     */
	@Column(name="customerId")
    private Integer customerId;
    /**
     * 推荐数量
     */
	@Column(name="num")
    private Integer num;
    /**
     * 创建时间
     */
	@Column(name="createTime")
    private Date createTime;
    /**
     * 创建人
     */
	@Column(name="createrId")
    private Integer createrId;
    /**
     * 修改时间
     */
	@Column(name="updateTime")
    private Date updateTime;
    /**
     * 修改人
     */
	@Column(name="updaterId")
    private Integer updaterId;
}