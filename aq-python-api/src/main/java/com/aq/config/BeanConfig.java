package com.aq.config;

import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.aq.core.config.properties.WeChatCoreProperties;
import com.aq.core.wechat.WeChatConfig;
import com.aq.core.wechat.pay.IWXPayDomain;
import com.aq.core.wechat.pay.WXPayConfig;
import com.aq.core.wechat.pay.WXPayConstants;
import com.aq.core.wechat.pay.WeChatPay;

/**
 * @ClassName: BeanConfig
 * @Description: bean相关配置
 * @author: lijie
 * @date: 2018年1月29日 下午2:08:23
 */

@Configuration
public class BeanConfig {

    /**
     * @param weChatProperties
     * @return
     * @throws Exception
     * @Title: weChatPay
     * @author: lijie
     * @Description: 微信支付配置
     * @return: WeChatPay
     */
    @Bean
    public WeChatPay weChatPay(WeChatCoreProperties weChatProperties) throws Exception {

        return new WeChatPay(new WXPayConfig() {

            @Override
            public String getAppID() {

                return WeChatConfig.APPID;
            }

            @Override
            public String getMchID() {
                return WeChatConfig.MCH_ID;
            }

            @Override
            public String getKey() {

                return WeChatConfig.API_KEY;
            }

            @Override
            public InputStream getCertStream() {
                // TODO:退款时需要扩展认证
                return null;
            }

            @Override
            public IWXPayDomain getWXPayDomain() {

                return new IWXPayDomain() {

                    @Override
                    public void report(String domain, long elapsedTimeMillis, Exception ex) {
                        // TODO:处理异常报告
                    }

                    @Override
                    public DomainInfo getDomain(WXPayConfig config) {

                        return new DomainInfo(WXPayConstants.DOMAIN_API, true);
                    }
                };
            }
        }, weChatProperties.getAsync());
    }
}
