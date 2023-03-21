package shop.mtcoding.miniproject2.util;

import java.security.MessageDigest;
import java.security.SecureRandom;

import org.springframework.http.HttpStatus;

import shop.mtcoding.miniproject2.handler.ex.CustomException;

public class EncryptionUtils {
    public static String getSalt() {
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[20];
        sr.nextBytes(salt);
        StringBuffer sb = new StringBuffer();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static String encrypt(String pwd, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] pwdSalt = ((pwd + salt).getBytes());
            md.reset();
            byte[] digested = md.digest(pwdSalt);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < digested.length; j++) {
                sb.append(Integer.toString((digested[j] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
