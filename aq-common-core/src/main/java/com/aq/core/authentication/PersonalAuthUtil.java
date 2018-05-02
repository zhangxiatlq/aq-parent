package com.aq.core.authentication;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aq.core.authentication.dto.PersonalAuthDTO;
import com.aq.core.authentication.enums.bank.BankAuthCodeEnum;
import com.aq.core.authentication.util.HttpUtils;
import com.aq.util.http.HttpResult;
import com.aq.util.result.RespCode;
import com.aq.util.result.Result;
import com.aq.util.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * ali 银行卡二、三、四元素（要素）实名认证（验证）
 *
 * @author 郑朋
 * @create 2018/2/23
 */
@Slf4j
public class PersonalAuthUtil {

    public static final String AppCode = "46ce424fb940445990774239e4ae5263";
    /**
     * 银行卡认证请求地址
     */
    public static final String BankAuthUrl = "https://aliyun-bankcard-verify.apistore.cn";

    /**
     * 银行卡api路径
     */
    public static final String BankAuthPath = "/bank";

    /**
     * 银行卡、实名认证
     *
     * @param bad
     * @return
     * @author Mr.Chang
     */
    public static Result personalAuthentication(PersonalAuthDTO bad) {
        log.info("认证请求入参：" + JSON.toJSONString(bad));
        Result result = ResultUtil.getResult(RespCode.Code.FAIL);
        //组装请求头信息
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + AppCode);
        //请求参数组装
        Map<String, String> params = new HashMap<>();
        params.put("bankcard", bad.getBankCard());
        params.put("cardNo", bad.getCardNo());
        params.put("Mobile", bad.getMobile());
        params.put("realName", bad.getRealName());
        try {
            /**
             * {
             "error_code": 0,
             "reason": "认证通过",
             "result": {
             "bankcard": "62220210",
             "information": {
             "abbreviation": "ICBC",
             "bankimage": "http://auth.apis.la/bank/1_ICBC.png",
             "bankname": "工商银行",
             "bankurl": "http://www.icbc.com.cn/",
             "cardname": "E时代卡",
             "cardtype": "银联借记卡",
             "enbankname": "Industrial and Commercial Bank of China",
             "isLuhn": true,
             "iscreditcard": 1,
             "servicephone": "95588"
             }
             },
             "ordersign": "2017061218205316653545354"
             }
             */
            HttpResult httpResult = HttpUtils.doGet(BankAuthUrl, BankAuthPath, HttpMethod.GET.toString(), headers, params);

            String content = httpResult.getResponseContent();
            JSONObject obj = JSON.parseObject(content);
            log.error("认证请求返回值：content={}", obj.toJSONString());
            //返回状态码
            Integer code = (Integer) obj.get("error_code");
            String reason = (String) obj.get("reason");
            result.setMessage(reason);
            if (httpResult.getStatusCode() == HttpStatus.OK.value() && BankAuthCodeEnum.SUCCESS.getCode().equals(code)) {
                result = ResultUtil.getResult(RespCode.Code.SUCCESS, obj.get("result"));
            }
        } catch (Exception e) {
            log.error("认证异常：e={}", e);
        }
        log.info("认证返回结果：result={}", JSON.toJSONString(result));
        return result;
    }

    public static void main(String[] args) {
        PersonalAuthDTO personalAuthDTO = new PersonalAuthDTO();
        personalAuthDTO.setBankCard("6217731203082876");
        personalAuthDTO.setCardNo("500113199212102831");
        personalAuthDTO.setRealName("熊克文");
        Result result  = personalAuthentication(personalAuthDTO);
        System.out.println(JSON.toJSONString(result));
    }
}
