package com.aq.core.wechat.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 微信推送模板dto
 * @date 2018/1/22
 * @copyright by xkw
 */
@Data
public class WechatPushTemplateDTO {

    /**
     * 接收者openid
     */
    @ApiModelProperty(value = "接收者openid")
    private String touser;

    /**
     * 模板枚举
     * {@link com.aq.core.wechat.constant.WechatTemplateEnum}
     */
    @ApiModelProperty(value = "模板id", required = true)
    private String template_id;

    /**
     * 模板跳转链接
     */
    @ApiModelProperty(value = "模板跳转链接", required = true)
    private String url;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    @ApiModelProperty(value = "跳小程序所需数据 不需跳小程序可不用传该数据")
    private Miniprogram miniprogram;

    /**
     * 模板数据 参照微信文档 https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433751277  结合模板使用
     */
    @ApiModelProperty(hidden = true)
    private Map<String,Data> data;

    @lombok.Data
    public static class Miniprogram {

        private Miniprogram() {

        }

        /**
         * 所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号绑定关联关系）
         */
        @ApiModelProperty(value = "小程序appid")
        private String appid;
        /**
         * 所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar）
         */
        @ApiModelProperty(value = "跳转到小程序的具体页面路径")
        private String pagepath;

        public static Miniprogram builder() {
            return new Miniprogram();
        }

        public Miniprogram appid(String appid) {
            this.setAppid(appid);
            return this;
        }

        public Miniprogram pagepath(String pagepath) {
            this.setPagepath(pagepath);
            return this;
        }
    }

    @lombok.Data
    public static class Data {

        private Data() {

        }

        /**
         * 模板内容字体颜色，不填默认为黑色
         */
        @ApiModelProperty(value = "模板数据颜色 默认黑色", hidden = true)
        private String color;
        /**
         * 模板内容值
         */
        @ApiModelProperty(value = "模板数据值", hidden = true)
        private String value;


        public static Data builder() {
            return new Data();
        }

        public Data color(String color) {
            this.setColor(color);
            return this;
        }

        public Data value(String value) {
            this.setValue(value);
            return this;
        }
    }
}
