package com.apply.aotoconfig.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mf.huanxin")
@Data
public class HuanXinProperties {

    private String appkey;
    private String clientId;
    private String clientSecret;

}
