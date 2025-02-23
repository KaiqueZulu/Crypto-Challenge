package com.matdev.security;

import javax.crypto.Cipher;
import java.security.PrivateKey;

public class RSADecrypt {
    public static String decrypt(byte[] encryptedMessage, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedMessage);

        return new String(decryptedBytes);
    }
}
