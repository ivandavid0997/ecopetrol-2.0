package com.nttdata.ecopetrol.talento.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AesUtil {
    // Debe ser 16 bytes (128 bits). Configurable vía AES_SECRET_KEY / AES_INIT_VECTOR.
    // Spring rellena los campos estáticos mediante los setters @Value al iniciar el contexto.
    private static String SECRET_KEY = "1234567890abcdef";
    private static String INIT_VECTOR = "1A2B3C4D5E6F7G8H";

    @Value("${aes.secret-key:1234567890abcdef}")
    public void setSecretKey(String secretKey) {
        AesUtil.SECRET_KEY = secretKey;
    }

    @Value("${aes.init-vector:1A2B3C4D5E6F7G8H}")
    public void setInitVector(String initVector) {
        AesUtil.INIT_VECTOR = initVector;
    }

    public static String encrypt(String value) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); // Puedes usar GCM en Java 8+
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] encrypted = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encrypted) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes("UTF-8"));
        SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(original);
    }
}
