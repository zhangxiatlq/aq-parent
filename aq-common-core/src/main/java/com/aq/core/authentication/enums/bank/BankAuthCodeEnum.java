package com.aq.core.authentication.enums.bank;

import lombok.Getter;


/**
 * ali 银行卡认证返回状态
 *
 * @author 郑朋
 * @create 2018/2/23 0023
 */
@Getter
public enum BankAuthCodeEnum {

    SUCCESS(0,"认证通过"),
    PARAM_ERROR(80001,"缺少参数,或参数不正确"),
    MOBILE_ERROR(80002,"手机号码不合法"),
    NAME_ERROR(80003,"姓名格式不正确"),
    ID_CARD_NUMBER_ERROR(80004,"身份证号码错误"),
    NOT_SUPPORT_CREDIT_CARD(80005," 不支持信用卡验证(当传递 cardtype=DC)有效"),
    NOT_SUPPORT_DEBIT_CARD(80006,"不支持借记卡验证(当传递 cardtype=DD)有效"),
    BANK_CARD_FORMART_ERROR(80007,"银行卡格式错误"),
    PARAM_INVALID(80009,"姓名/身份证号码/手机号至少需要填写一项"),
    TRADE_ERROR_TRY(80010,"交易失败,请稍后重试"),
    NOT_SUPPORT_BANK_CARD_TYPE(80011,"不支持此类银行卡校验"),
    TRADE_ERROR(80012,"交易失败,请联系发卡"),
    CERTIFICATE_TYPE_ERROR(80013,"证件类型不正确"),
    NOT_CERTIFICATE_NUMBER(80014,"您选择了证件类型,但是未传递证件号"),
    INVALID_BANK_CARD_NUMBER(90020,"无效卡号"),
    BANK_CARD_CONFISCATE(90021,"此卡被没收,请于发卡方联系"),
    CARDHOLDER_AUTH_ERROR(90022,"持卡人认证失败"),
    CARD_NOT_INIT(90024,"该卡未初始化或睡眠卡"),
    CHEAT_CARD(90025,"作弊卡,吞卡"),
    NOT_SUPPORT_TRADE(90026,"发卡方不支持的交易"),
    LOSS_CARD(90027,"此卡已经挂失"),
    CARD_CONFISCATE(90028,"此卡被没收"),
    EXPIRED_CARD(90029,"该卡已过期"),
    LIMITED_CARD(90031,"受限制的卡"),
    PASS_PASSWORD_ERROR_NUM(90032,"密码错误次数超限"),
    INVALID_ID_CARD(90033,"无效身份证号码"),
    NOT_SUPPORT_BANK_CARD(90034,"不支持的银行卡"),
    NOT_OPEN_NO_CARD_PAY(90036,"未开通无卡支付"),
    NOT_OPEN_AUTH_PAY(90039,"用户未开通认证支付"),
    NAME_NOT_MATCH_ID_CARD(90084,"姓名与身份证号不匹配"),
    MOBILE_NOT_MATCH_BANK_CARD(90085,"手机号码与卡号不匹配"),
    NAME_NOT_MATCH_BANK_CARD(90086,"姓名与卡号不匹配"),
    AUTH_FAIL(90099,"认证不通过");

    BankAuthCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;
    private String msg;
}
