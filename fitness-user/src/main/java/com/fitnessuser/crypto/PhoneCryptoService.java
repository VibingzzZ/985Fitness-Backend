package com.fitnessuser.crypto;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PhoneCryptoService {
    private static final String PREFIX = "v1:";
    private static final int IV_LENGTH = 12;
    private static final int GCM_TAG_BITS = 128;

    private final SecretKeySpec encryptionKey;
    private final SecretKeySpec hashKey;
    private final SecureRandom secureRandom = new SecureRandom();

    public PhoneCryptoService(UserDataSecurityProperties properties) {
        byte[] masterKey = decodeMasterKey(properties.getPhoneEncryptionKey());
        this.encryptionKey = new SecretKeySpec(derive(masterKey, "phone-encryption"), "AES");
        this.hashKey = new SecretKeySpec(derive(masterKey, "phone-hash"), "HmacSHA256");
    }

    public String encrypt(String phone) {
        if (!StringUtils.hasText(phone)) {
            return phone;
        }
        try {
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] encrypted = cipher.doFinal(normalize(phone).getBytes(StandardCharsets.UTF_8));
            byte[] payload = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, payload, 0, iv.length);
            System.arraycopy(encrypted, 0, payload, iv.length, encrypted.length);
            return PREFIX + Base64.getUrlEncoder().withoutPadding().encodeToString(payload);
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("手机号加密失败", exception);
        }
    }

    public String decrypt(String encryptedPhone) {
        if (!StringUtils.hasText(encryptedPhone) || !encryptedPhone.startsWith(PREFIX)) {
            return encryptedPhone;
        }
        try {
            byte[] payload = Base64.getUrlDecoder().decode(encryptedPhone.substring(PREFIX.length()));
            if (payload.length <= IV_LENGTH) {
                throw new IllegalArgumentException("手机号密文格式错误");
            }
            byte[] iv = new byte[IV_LENGTH];
            byte[] encrypted = new byte[payload.length - IV_LENGTH];
            System.arraycopy(payload, 0, iv, 0, IV_LENGTH);
            System.arraycopy(payload, IV_LENGTH, encrypted, 0, encrypted.length);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, encryptionKey, new GCMParameterSpec(GCM_TAG_BITS, iv));
            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException exception) {
            throw new IllegalStateException("手机号解密失败", exception);
        }
    }

    public String hash(String phone) {
        if (!StringUtils.hasText(phone)) {
            return null;
        }
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(hashKey);
            return toHex(mac.doFinal(normalize(phone).getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("手机号哈希失败", exception);
        }
    }

    public boolean isEncrypted(String phone) {
        return StringUtils.hasText(phone) && phone.startsWith(PREFIX);
    }

    private static String normalize(String phone) {
        return phone.trim();
    }

    private static byte[] decodeMasterKey(String encodedKey) {
        if (!StringUtils.hasText(encodedKey)) {
            throw new IllegalStateException("手机号加密密钥未配置，请设置 USER_PHONE_ENCRYPTION_KEY");
        }
        try {
            byte[] key = Base64.getDecoder().decode(encodedKey);
            if (key.length != 32) {
                throw new IllegalStateException("手机号加密密钥必须是32字节Base64值");
            }
            return key;
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("手机号加密密钥不是有效Base64值", exception);
        }
    }

    private static byte[] derive(byte[] masterKey, String purpose) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(masterKey);
            return digest.digest(purpose.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("手机号密钥派生失败", exception);
        }
    }

    private static String toHex(byte[] value) {
        StringBuilder result = new StringBuilder(value.length * 2);
        for (byte item : value) {
            result.append(String.format("%02x", item));
        }
        return result.toString();
    }
}
