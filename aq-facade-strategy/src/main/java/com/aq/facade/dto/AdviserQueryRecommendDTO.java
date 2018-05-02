package com.aq.facade.dto;

import com.aq.core.base.BaseValidate;
import lombok.Data;

import java.io.Serializable;


/**
 * 查询被推荐人的投顾推荐 DTO
 *
 * @author 郑朋
 * @create 2018/3/8
 */
@Data
public class AdviserQueryRecommendDTO extends BaseValidate implements Serializable{

    private static final long serialVersionUID = 1986487094021745221L;


    /**
     * 投顾id
     */
    private Integer adviserId;
    /**
     * 推荐人id
     */
    private Integer recommendedId;

    /**
     * 被推荐人类型 {@link com.aq.core.constant.RoleCodeEnum}
     */
    private Byte beRecommendedRoleType;

    /**
     * 被推荐id
     */
    private Integer beRecommendedId;
}
