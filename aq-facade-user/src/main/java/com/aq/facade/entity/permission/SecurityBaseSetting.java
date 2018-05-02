package com.aq.facade.entity.permission;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 安全设置基础项
 *
 * @author 郑朋
 * @create 2018/3/21
 */
@Table(name = "aq_security_base_setting")
@Data
public class SecurityBaseSetting implements Serializable {
    private static final long serialVersionUID = -601357723202545147L;

    @Id
    private Integer id;

    /**
     * 菜单名称
     */
    @Column(name = "name")
    private String name;
}
