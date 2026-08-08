package com.fitnessuser.crypto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.user-data")
public class UserDataSecurityProperties {
    private String phoneEncryptionKey;

    public String getPhoneEncryptionKey() {
        return phoneEncryptionKey;
    }

    public void setPhoneEncryptionKey(String phoneEncryptionKey) {
        this.phoneEncryptionKey = phoneEncryptionKey;
    }
}
