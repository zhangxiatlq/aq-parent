package com.aq.facade.contant;

import com.aq.core.constant.CacheKey;
import com.aq.core.constant.ToolCategoryEnum;
import com.aq.util.string.StringTools;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: ToolCacheKeyEnum
 * @Description: 工具缓存操作key
 * @author: lijie
 * @date: 2018年3月17日 下午3:21:53
 */
public enum ToolCacheKeyEnum {

    ADD {
        @Override
        public String getKey(ToolCategoryEnum type) {
            return KEY_MAP.get(StringTools.getKey(this.name(), type.getCode()));
        }

        @Override
        public String getKeyByToolType(ToolCategoryEnum type, String toolType) {
            return KEY_MAP.get(StringTools.getKey(this.name(), type.getCode(), toolType));
        }
    },
    DELETE {
        @Override
        public String getKey(ToolCategoryEnum type) {
            return KEY_MAP.get(StringTools.getKey(this.name(), type.getCode()));
        }

        @Override
        public String getKeyByToolType(ToolCategoryEnum type, String toolType) {
            return KEY_MAP.get(StringTools.getKey(this.name(), type.getCode(), toolType));
        }
    },

    UPDATE {
        @Override
        public String getKey(ToolCategoryEnum type) {
            return KEY_MAP.get(StringTools.getKey(this.name(), type.getCode()));
        }

        @Override
        public String getKeyByToolType(ToolCategoryEnum type, String toolType) {
            return KEY_MAP.get(StringTools.getKey(this.name(), type.getCode(), toolType));
        }
    },
    GET {
        @Override
        public String getKey(ToolCategoryEnum type) {
            return KEY_MAP.get(StringTools.getKey(this.name(), type.getCode()));
        }

        @Override
        public String getKeyByToolType(ToolCategoryEnum type, String toolType) {
            return KEY_MAP.get(StringTools.getKey(this.name(), type.getCode(), toolType));
        }
    };

    private static Map<String, String> KEY_MAP;

    static {
        KEY_MAP = new HashMap<>();
        // 新增
        KEY_MAP.put(StringTools.getKey(ADD.name(), ToolCategoryEnum.GRID.getCode()), CacheKey.GRID_TOOL_BASE.getKey());
        KEY_MAP.put(StringTools.getKey(ADD.name(), ToolCategoryEnum.SELLING.getCode()), CacheKey.SELLING_TOOL_BASE.getKey());
        KEY_MAP.put(StringTools.getKey(ADD.name(), ToolCategoryEnum.TREND.getCode()), CacheKey.TREND_TOOL_BASE.getKey());

        // 新增--专用、普通
        KEY_MAP.put(StringTools.getKey(ADD.name(), ToolCategoryEnum.GRID.getCode(), ToolTypeEnum.ROUTINE.getType().toString()), CacheKey.GRID_TOOL_BASE.getKey());
        KEY_MAP.put(StringTools.getKey(ADD.name(), ToolCategoryEnum.SELLING.getCode(), ToolTypeEnum.ROUTINE.getType().toString()), CacheKey.SELLING_TOOL_BASE.getKey());
        KEY_MAP.put(StringTools.getKey(ADD.name(), ToolCategoryEnum.TREND.getCode(), ToolTypeEnum.ROUTINE.getType().toString()), CacheKey.TREND_TOOL_BASE.getKey());
        KEY_MAP.put(StringTools.getKey(ADD.name(), ToolCategoryEnum.TREND.getCode(), ToolTypeEnum.SPECIAL_PURPOSE.getType().toString()), CacheKey.SPECIAL_TREND_TOOL_BASE.getKey());

        // 删除
        KEY_MAP.put(StringTools.getKey(DELETE.name(), ToolCategoryEnum.GRID.getCode()), CacheKey.GRID_TOOL_CALCULATE.getKey());
        KEY_MAP.put(StringTools.getKey(DELETE.name(), ToolCategoryEnum.SELLING.getCode()), CacheKey.SELLING_TOOL_CALCULATE.getKey());
        KEY_MAP.put(StringTools.getKey(DELETE.name(), ToolCategoryEnum.TREND.getCode()), CacheKey.TREND_TOOL_BASE.getKey());

        KEY_MAP.put(StringTools.getKey(DELETE.name(), ToolCategoryEnum.GRID.getCode(), ToolTypeEnum.ROUTINE.getType().toString()), CacheKey.GRID_TOOL_CALCULATE.getKey());
        KEY_MAP.put(StringTools.getKey(DELETE.name(), ToolCategoryEnum.SELLING.getCode(), ToolTypeEnum.ROUTINE.getType().toString()), CacheKey.SELLING_TOOL_CALCULATE.getKey());
        KEY_MAP.put(StringTools.getKey(DELETE.name(), ToolCategoryEnum.TREND.getCode(), ToolTypeEnum.ROUTINE.getType().toString()), CacheKey.TREND_TOOL_BASE.getKey());
        KEY_MAP.put(StringTools.getKey(DELETE.name(), ToolCategoryEnum.TREND.getCode(), ToolTypeEnum.SPECIAL_PURPOSE.getType().toString()), CacheKey.SPECIAL_TREND_TOOL_BASE.getKey());

        // 查询
        KEY_MAP.put(StringTools.getKey(GET.name(), ToolCategoryEnum.GRID.getCode()), CacheKey.GRID_TOOL_CALCULATE.getKey());
        KEY_MAP.put(StringTools.getKey(GET.name(), ToolCategoryEnum.SELLING.getCode()), CacheKey.SELLING_TOOL_CALCULATE.getKey());
        KEY_MAP.put(StringTools.getKey(GET.name(), ToolCategoryEnum.TREND.getCode()), CacheKey.TREND_TOOL_BASE.getKey());

        KEY_MAP.put(StringTools.getKey(GET.name(), ToolCategoryEnum.GRID.getCode(), ToolTypeEnum.ROUTINE.getType().toString()), CacheKey.GRID_TOOL_CALCULATE.getKey());
        KEY_MAP.put(StringTools.getKey(GET.name(), ToolCategoryEnum.SELLING.getCode(), ToolTypeEnum.ROUTINE.getType().toString()), CacheKey.SELLING_TOOL_CALCULATE.getKey());
        KEY_MAP.put(StringTools.getKey(GET.name(), ToolCategoryEnum.TREND.getCode(), ToolTypeEnum.ROUTINE.getType().toString()), CacheKey.TREND_TOOL_BASE.getKey());
        KEY_MAP.put(StringTools.getKey(GET.name(), ToolCategoryEnum.TREND.getCode(), ToolTypeEnum.SPECIAL_PURPOSE.getType().toString()), CacheKey.SPECIAL_TREND_TOOL_BASE.getKey());

    }

    public abstract String getKey(ToolCategoryEnum type);

    public abstract String getKeyByToolType(ToolCategoryEnum type, String toolType);

}
