package com.matdev.security;

import javax.crypto.Cipher;
import java.security.PublicKey;
import java.util.Base64;

public class RSAEncrypt {
    public static String encrypt(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] encryptedByte = cipher.doFinal(message.getBytes());

        return Base64.getEncoder().encodeToString(encryptedByte);
    }
}
