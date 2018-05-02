package com.aq.core.wechat.constant;

import lombok.Getter;

/**
 * @author 熊克文
 * @version 1.1
 * @describe 微信模板枚举
 * @date 2018/1/22
 * @copyright by xkw
 */
public enum WechatTemplateEnum {

    ONE("RHcQSYDc0Gn5phLDWTbSl8OFRMsqLsOszjmmLJtuz5I",
            "股票建仓提醒",
            "{{first.DATA}}" +
                    "股票代码：{{keyword1.DATA}}" +
                    "股票名称：{{keyword2.DATA}}" +
                    "股票价格：{{keyword3.DATA}}" +
                    "建仓时间：{{keyword4.DATA}}" +
                    "创建类型：{{keyword5.DATA}}" +
                    "{{remark.DATA}}",
            "温馨提示您关注的股票已建仓：" +
                    "股票代码：002300" +
                    "股票名称：太阳电缆" +
                    "股票价格：13.60" +
                    "建仓时间：2014年7月21日 18:36" +
                    "创建类型：模拟操作" +
                    "信息仅供参考，请投资者独立作出决策。股市有风险，投资须谨慎。"),
    TWO("TeGDsqrzmgss68WofTZUYk9aIbMVbbDQTN6ZjxI26VU",
            "组合操作提醒",
            "{{first.DATA}}" +
                    "操作类型：{{keyword1.DATA}}" +
                    "股票代码：{{keyword2.DATA}}" +
                    "股票名称：{{keyword3.DATA}}" +
                    "操作价格：{{keyword4.DATA}}" +
                    "操作仓位：{{keyword5.DATA}}" +
                    "{{remark.DATA}}",
            "价值量化1号" +
                    "操作类型：买入" +
                    "股票代码：000001" +
                    "股票名称：平安银行" +
                    "操作价格：8.1" +
                    "操作仓位：10%" +
                    "操作理由：平安银行净利润增长平稳，ROE10%，符合增长预期"),
    THREE("c2HDqbUSFTPKESJz-J5bLXiWmDRBWGiHDIQ89oog_k4",
            "模拟股票交易通知",
            "{{first.DATA}}" +
                    "交易操作：{{keyword1.DATA}}" +
                    "交易价格：{{keyword2.DATA}}" +
                    "交易数量：{{keyword3.DATA}}" +
                    "交易占比：{{keyword4.DATA}}" +
                    "交易时间：{{keyword5.DATA}}" +
                    "{{remark.DATA}}", "尊敬的用户，您在虚拟交易中的订单交易成功" +
            "交易操作：买入" +
            "交易价格：16.5" +
            "交易数量：100" +
            "交易占比：1%" +
            "交易时间：2015-10-20" +
            "点击这里查看交易详情&gt;&gt;"),
    FOUR("cgnRz3fBCoURhCoy0UtQA7aSN5IJZiJmyT0NUpDiA3o",
            "购买成功通知",
            "您好，您已购买成功。" +
                    "商品信息：{{name.DATA}}" +
                    "{{remark.DATA}}",
            "您好，您已购买成功。" +
            "商品信息：微信影城影票" +
            "有效期：永久有效" +
                    "券号为QQ5024813399，密码为123456890"),
    FIVE("ZGi55O2zAMALxahDo00rQDql6KWTJbs733th7TVOxjU", "调仓提醒",
            "{{first.DATA}}" +
                    "调入标的：{{keyword1.DATA}}" +
                    "调出标的：{{keyword2.DATA}}" +
                    "调仓时间：{{keyword3.DATA}}" +
                    "{{remark.DATA}}",
            "策略调仓提醒" +
                    "调入标的：平安银行(000001),万科A(000002)" +
                    "调出标的：平安银行(000001),万科A(000002)" +
                    "调仓时间：2018-02-02" +
                    "信息来源您定制的策略，请您充分认识投资风险，谨慎投资。如有疑问，请与客服联系。【AQ量化家】");


    WechatTemplateEnum(String templateId, String name, String code, String example) {
        this.templateId = templateId;
        this.name = name;
        this.code = code;
        this.example = example;
    }

    /**
     * 模板id
     */
    @Getter
    private String templateId;
    /**
     * 模板名字
     */
    @Getter
    private String name;
    /**
     * 模板代码
     */
    @Getter
    private String code;
    /**
     * 模板示例
     */
    @Getter
    private String example;
}
