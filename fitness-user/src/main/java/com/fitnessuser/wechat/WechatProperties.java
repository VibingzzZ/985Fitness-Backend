package com.fitnessuser.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wechat.mini-program")
public class WechatProperties {
    private String appId;
    private String secret;
    private String baseUrl = "https://api.weixin.qq.com";
}
