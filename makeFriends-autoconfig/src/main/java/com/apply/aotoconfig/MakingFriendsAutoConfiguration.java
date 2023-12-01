package com.apply.aotoconfig;

import com.apply.aotoconfig.properties.AipFaceProperties;
import com.apply.aotoconfig.properties.HuanXinProperties;
import com.apply.aotoconfig.properties.OssProperties;
import com.apply.aotoconfig.properties.SmsProperties;
import com.apply.aotoconfig.template.AipFaceTemplate;
import com.apply.aotoconfig.template.HuanXinTemplate;
import com.apply.aotoconfig.template.OssTemplate;
import com.apply.aotoconfig.template.SmsTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        SmsProperties.class,
        OssProperties.class,
        AipFaceProperties.class,
        HuanXinProperties.class
})
public class MakingFriendsAutoConfiguration {
    @Bean
    public SmsTemplate smsTemplate(SmsProperties smsProperties) {
        return new SmsTemplate(smsProperties);
    }

    @Bean
    public OssTemplate ossTemplate(OssProperties ossProperties) {
        return new OssTemplate(ossProperties);
    }

    @Bean
    public AipFaceTemplate aipFaceTemplate() {
        return new AipFaceTemplate();
    }

    @Bean
    public HuanXinTemplate huanXinTemplate(HuanXinProperties huanXinProperties) {
        return new HuanXinTemplate(huanXinProperties);
    }

}

