package com.aq.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 投顾后台列表DTO
 *
 * @author 郑朋
 * @create 2018/4/13
 **/
@Data
public class AdviserPageDTO implements Serializable {
    private static final long serialVersionUID = 1284572036041571779L;

    private String adviserName;

    private String createTimeStart;

    private String createTimeEnd;
}
