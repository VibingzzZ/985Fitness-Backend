package com.fitnessuser.wechat;

public interface WechatGateway {
    WechatSession exchangeLoginCode(String code);

    String exchangePhoneCode(String code);
}
