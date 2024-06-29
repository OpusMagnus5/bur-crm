package pl.bodzioch.damian.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.configuration.security.SecurityConstants;
import pl.bodzioch.damian.exception.AppException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

@Component
@Slf4j
public class CipherComponent {

    private final SecretKey secretKey;
    private final byte[] iv;

    public CipherComponent() throws GeneralSecurityException {
        this.secretKey = generateSecretKey();
        this.iv = generateIv();
    }

    private SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        return keyGenerator.generateKey();
    }

    private byte[] generateIv() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        return iv;
    }

    public String encryptMessage(String text) {
        try {
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, this.iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
            byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            byte[] outputBytes = attachIvAndGetOutput(this.iv, encryptedBytes);
            return Base64.getUrlEncoder().encodeToString(outputBytes);
        } catch (GeneralSecurityException ex) {
            log.error("Cipher exception during encrypt message: {}", text, ex);
            throw AppException.getGeneralError(ex);
        }
    }

    public String decryptMessage(String encryptedText) {
        try {
            byte[] encryptedBytes = Base64.getUrlDecoder().decode(encryptedText);
            byte[] ciphertext = extractEncodedTextBytes(encryptedBytes, this.iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, this.iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            byte[] decryptedBytes = cipher.doFinal(ciphertext);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException ex) {
            log.error("Cipher exception during decrypt message: {}", encryptedText, ex);
            throw AppException.getGeneralError(ex);
        }
    }

    public Long getPrincipalId() {
        Jwt token = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String principalId = token.getClaim(SecurityConstants.PRINCIPAL_ID);
        return getDecryptedId(principalId);
    }

    public Optional<Long> getPrincipalIdIfExists() {
        try {
            Jwt token = getToken();
            String principalId = token.getClaim(SecurityConstants.PRINCIPAL_ID);
            return Optional.of(getDecryptedId(principalId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Long getDecryptedId(String id) {
        return Optional.ofNullable(id)
                .filter(StringUtils::isNotBlank)
                .map(this::decryptMessage)
                .map(Long::parseLong)
                .orElse(null);
    }

    public Optional<String> getSessionId() {
        try {
            Jwt token = getToken();
            String sessionId = token.getClaim(SecurityConstants.SESSION_ID);
            return Optional.of(sessionId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Jwt getToken() {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private byte[] attachIvAndGetOutput(byte[] iv, byte[] encryptedBytes) {
        byte[] outputBytes = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, outputBytes, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, outputBytes, iv.length, encryptedBytes.length);
        return outputBytes;
    }

    private byte[] extractEncodedTextBytes(byte[] encryptedBytes, byte[] iv) {
        byte[] ciphertext = new byte[encryptedBytes.length - iv.length];
        System.arraycopy(encryptedBytes, iv.length, ciphertext, 0, ciphertext.length);
        return ciphertext;
    }
}
