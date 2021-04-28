package com.sabu.hmacdemo.util;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/27
 */
public class HmacKeyGenerator {
    public static String generateNonce() {
        SecureRandom secureRandom = new SecureRandom();
        UUID uuid = UUID.randomUUID();
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < 36; i++) {
//            stringBuilder.append(secureRandom.nextInt(10));
//        }
//        String randomNumber = stringBuilder.toString();
        return uuid.toString();
    }
}
