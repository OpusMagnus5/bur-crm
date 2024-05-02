package pl.bodzioch.damian.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.bodzioch.damian.exception.AppException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
@Slf4j
public class CipherComponent {

    private final SecretKey secretKey;

    public CipherComponent() throws GeneralSecurityException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        this.secretKey = keyGenerator.generateKey();
    }

    public String encryptMessage(String text) {
        try {
            byte[] iv = generateIv();
            GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmSpec);
            byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            byte[] outputBytes = attachIvAndGetOutput(iv, encryptedBytes);
            return Base64.getUrlEncoder().encodeToString(outputBytes);
        } catch (GeneralSecurityException ex) {
            log.error("Cipher exception during encrypt message: {}", text, ex);
            throw AppException.getGeneralError(ex);
        }
    }

    public String decryptMessage(String encryptedText) {
        try {
            byte[] encryptedBytes = Base64.getUrlDecoder().decode(encryptedText);
            byte[] iv = extractIv(encryptedBytes);
            byte[] ciphertext = extractEncodedTextBytes(encryptedBytes, iv);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
            byte[] decryptedBytes = cipher.doFinal(ciphertext);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException ex) {
            log.error("Cipher exception during decrypt message: {}", encryptedText, ex);
            throw AppException.getGeneralError(ex);
        }
    }

    private byte[] attachIvAndGetOutput(byte[] iv, byte[] encryptedBytes) {
        byte[] outputBytes = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, outputBytes, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, outputBytes, iv.length, encryptedBytes.length);
        return outputBytes;
    }

    private byte[] generateIv() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);
        return iv;
    }

    private byte[] extractIv(byte[] encryptedBytes) {
        byte[] iv = new byte[16];
        System.arraycopy(encryptedBytes, 0, iv, 0, iv.length);
        return iv;
    }

    private byte[] extractEncodedTextBytes(byte[] encryptedBytes, byte[] iv) {
        byte[] ciphertext = new byte[encryptedBytes.length - iv.length];
        System.arraycopy(encryptedBytes, iv.length, ciphertext, 0, ciphertext.length);
        return ciphertext;
    }
}