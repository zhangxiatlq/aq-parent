package com.aq.mapper.system;

import com.aq.core.base.BaseMapper;
import com.aq.facade.entity.system.Banner;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 轮播图片 mapper
 *
 * @author zhengpeng
 * @date 2018-02-07
 */
@Repository
public interface BannerMapper extends BaseMapper<Banner> {

    /**
     * 获取banner 图
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author 郑朋
     * @create 2018/2/9 0009
     */
    List<String> selectAllBanner();
}