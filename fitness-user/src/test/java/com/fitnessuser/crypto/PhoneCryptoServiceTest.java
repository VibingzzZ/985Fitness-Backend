package com.fitnessuser.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Base64;
import org.junit.jupiter.api.Test;

class PhoneCryptoServiceTest {

    @Test
    void shouldEncryptWithRandomIvAndDecryptPhone() {
        PhoneCryptoService service = serviceWithKey();

        String first = service.encrypt("13800000000");
        String second = service.encrypt("13800000000");

        assertThat(first).startsWith("v1:").isNotEqualTo(second);
        assertThat(service.decrypt(first)).isEqualTo("13800000000");
        assertThat(service.decrypt(second)).isEqualTo("13800000000");
    }

    @Test
    void shouldCreateStableLookupHash() {
        PhoneCryptoService service = serviceWithKey();

        assertThat(service.hash("13800000000"))
                .isEqualTo(service.hash("13800000000"))
                .hasSize(64);
    }

    @Test
    void shouldRejectInvalidKeyLength() {
        UserDataSecurityProperties properties = new UserDataSecurityProperties();
        properties.setPhoneEncryptionKey(Base64.getEncoder().encodeToString(new byte[16]));

        assertThatThrownBy(() -> new PhoneCryptoService(properties))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("手机号加密密钥必须是32字节Base64值");
    }

    private PhoneCryptoService serviceWithKey() {
        UserDataSecurityProperties properties = new UserDataSecurityProperties();
        byte[] key = new byte[32];
        for (int index = 0; index < key.length; index++) {
            key[index] = (byte) index;
        }
        properties.setPhoneEncryptionKey(Base64.getEncoder().encodeToString(key));
        return new PhoneCryptoService(properties);
    }
}
