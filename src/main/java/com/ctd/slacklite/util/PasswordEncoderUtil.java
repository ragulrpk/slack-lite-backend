package com.ctd.slacklite.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class PasswordEncoderUtil {

    private static final String HASH_KEY = "slacklite";
    private static final String ALGORITHM = "AES";

    // ---------------- ENCRYPT ----------------
    public static String encryptPassword(String rawPassword) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());

            byte[] encryptedBytes = cipher.doFinal(
                    rawPassword.getBytes(StandardCharsets.UTF_8)
            );

            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new RuntimeException("Password encryption failed", e);
        }
    }

    // ---------------- DECRYPT ----------------
    public static String decryptPassword(String encryptedPassword) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey());

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Password decryption failed", e);
        }
    }

    // ---------------- MATCH ----------------
    public static boolean matches(String rawPassword, String encryptedPassword) {
        String decryptedPassword = decryptPassword(encryptedPassword);
        return rawPassword.equals(decryptedPassword);
    }

    // ---------------- SECRET KEY ----------------
    private static SecretKeySpec getSecretKey() {
        // AES requires exactly 16 bytes key
        byte[] keyBytes = new byte[16];
        byte[] hashKeyBytes = HASH_KEY.getBytes(StandardCharsets.UTF_8);

        System.arraycopy(hashKeyBytes, 0, keyBytes, 0, Math.min(hashKeyBytes.length, 16));

        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    // ---------------- MAIN (TEST ONLY) ----------------
    public static void main(String[] args) {

        String rawPassword = "12345";

        String encrypted = encryptPassword(rawPassword);
        String decrypted = decryptPassword(encrypted);
        boolean matches = matches(rawPassword, encrypted);

        System.out.println("Raw Password      : " + rawPassword);
        System.out.println("Encrypted Password: " + encrypted);
        System.out.println("Decrypted Password: " + decrypted);
        System.out.println("Password Match?   : " + matches);
    }
}