package com.aq.facade.entity.customer;

import com.aq.core.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 客户分组表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@Table(name = "aq_group")
public class Group extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 3172671916248070376L;


    /**
     * 分组名称
     */
    @Column(name = "groupName")
    private String groupName;

    /**
     * 是否默认 1-是 2-否
     * {@link com.aq.core.constant.IsDeleteEnum}
     */
    @Column(name = "isDefault")
    private Byte isDefault;

    /**
     * 是否删除 1-是 2-否
     */
    @Column(name = "isDelete")
    private Byte isDelete;
}