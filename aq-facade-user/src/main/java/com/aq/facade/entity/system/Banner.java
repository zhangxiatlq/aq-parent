package com.aq.facade.entity.system;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 轮播图片表
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Data
@Table(name = "aq_banner")
public class Banner implements Serializable {
    private static final long serialVersionUID = -2214809149892407259L;

    @Id
    private Long id;

    /**
     * 图片地址
     */
    @Column(name = "url")
    private String url;
}