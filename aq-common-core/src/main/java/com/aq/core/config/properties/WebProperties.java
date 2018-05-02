/**
 * @Title: WebProperties.java
 * @Package com.ph.shopping.common.core.config.properties
 * @Description: TODO(用一句话描述该文件做什么)
 * @author: 李杰
 * @date: 2017年6月29日 下午1:43:01
 * @version V1.0
 * @Copyright: 2017
 */
package com.aq.core.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName: WebProperties
 * @Description:web相关配置
 * @author: 李杰
 * @date: 2017年6月29日 下午1:43:01
 * @Copyright: 2017
 */
@PropertySource("classpath:web.properties")
@ConfigurationProperties(prefix = "web")
@Component
public class WebProperties {
    /**
     * 服务版本
     */
    private String serviceVersion;

    private String pythonWebChat;

    public String getPythonWebChat() {
        return pythonWebChat;
    }

    public void setPythonWebChat(String pythonWebChat) {
        this.pythonWebChat = pythonWebChat;
    }

    public String getServiceVersion() {
        return serviceVersion == null ? null : serviceVersion.trim();
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }
}
