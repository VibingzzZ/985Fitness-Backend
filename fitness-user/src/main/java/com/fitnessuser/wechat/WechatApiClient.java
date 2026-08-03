package com.fitnessuser.wechat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fitnessuser.exception.UserBusinessException;
import java.time.Instant;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Component
public class WechatApiClient implements WechatGateway {
    private final RestClient restClient;
    private final WechatProperties properties;
    private volatile CachedAccessToken cachedAccessToken;

    public WechatApiClient(RestClient.Builder builder, WechatProperties properties) {
        this.properties = properties;
        this.restClient = builder.baseUrl(properties.getBaseUrl()).build();
    }

    @Override
    public WechatSession exchangeLoginCode(String code) {
        validateConfiguration();
        CodeSessionResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/sns/jscode2session")
                        .queryParam("appid", properties.getAppId())
                        .queryParam("secret", properties.getSecret())
                        .queryParam("js_code", code)
                        .queryParam("grant_type", "authorization_code")
                        .build())
                .retrieve()
                .body(CodeSessionResponse.class);
        if (response == null || !StringUtils.hasText(response.openid())) {
            throw wechatError(response == null ? null : response.errmsg());
        }
        return new WechatSession(response.openid(), response.unionid());
    }

    @Override
    public String exchangePhoneCode(String code) {
        PhoneResponse response = restClient.post()
                .uri("/wxa/business/getuserphonenumber?access_token={token}", accessToken())
                .body(Map.of("code", code))
                .retrieve()
                .body(PhoneResponse.class);
        if (response == null
                || response.errcode() != 0
                || response.phoneInfo() == null
                || !StringUtils.hasText(response.phoneInfo().purePhoneNumber())) {
            throw wechatError(response == null ? null : response.errmsg());
        }
        return response.phoneInfo().purePhoneNumber();
    }

    private synchronized String accessToken() {
        validateConfiguration();
        if (cachedAccessToken != null && cachedAccessToken.expiresAt().isAfter(Instant.now())) {
            return cachedAccessToken.value();
        }
        AccessTokenResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder.path("/cgi-bin/token")
                        .queryParam("grant_type", "client_credential")
                        .queryParam("appid", properties.getAppId())
                        .queryParam("secret", properties.getSecret())
                        .build())
                .retrieve()
                .body(AccessTokenResponse.class);
        if (response == null || !StringUtils.hasText(response.accessToken())) {
            throw wechatError(response == null ? null : response.errmsg());
        }
        long cacheSeconds = Math.max(60, response.expiresIn() - 300L);
        cachedAccessToken = new CachedAccessToken(
                response.accessToken(), Instant.now().plusSeconds(cacheSeconds));
        return cachedAccessToken.value();
    }

    private void validateConfiguration() {
        if (!StringUtils.hasText(properties.getAppId())
                || !StringUtils.hasText(properties.getSecret())) {
            throw new UserBusinessException("微信小程序AppID或Secret未配置");
        }
    }

    private UserBusinessException wechatError(String detail) {
        return new UserBusinessException(
                StringUtils.hasText(detail) ? "微信接口调用失败: " + detail : "微信接口调用失败");
    }

    private record CachedAccessToken(String value, Instant expiresAt) {
    }

    private record CodeSessionResponse(
            String openid, String unionid, Integer errcode, String errmsg) {
    }

    private record AccessTokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("expires_in") Integer expiresIn,
            Integer errcode,
            String errmsg) {
    }

    private record PhoneResponse(
            Integer errcode,
            String errmsg,
            @JsonProperty("phone_info") PhoneInfo phoneInfo) {
    }

    private record PhoneInfo(
            @JsonProperty("purePhoneNumber") String purePhoneNumber) {
    }
}
