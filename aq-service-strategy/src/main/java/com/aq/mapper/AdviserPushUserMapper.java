package com.aq.mapper;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.AdviserPush;
import com.aq.facade.entity.AdviserPushUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 投顾推送用户表mapper
 *
 * @author 郑朋
 * @create 2018/3/2
 */
@Repository
public interface AdviserPushUserMapper extends BaseMapper<AdviserPushUser> {
    /**
     * 删除推送记录
     *
     * @param list
     * @return int
     * @author 郑朋
     * @create 2018/3/7 0007
     */
    int deleteAdviserPushUser(List<AdviserPush> list);
}