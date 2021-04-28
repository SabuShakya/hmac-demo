package com.sabu.hmacdemo.constants;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/21
 */
public class HmacConstants {

    public static final String API_KEY = "API_KEY";
    public static final String API_SECRET = "API_SECRET";
    public static final String HMAC_ALGORITHM = "HmacSHA512";
    public static String AUTHORIZATION_HEADER = "X-Authorization";
    public static String AUTHORIZATION_TIMESTAMP = "X-Authorization-Timestamp";
    public static String PAYLOAD_HASHING_ALGORITHM = "SHA-256";
    public static long TIME_STAMP_TOLERANCE = 900L;
}
