package com.sabu.hmacdemo.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/27
 */
public class SHAUtil {
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static final String OUTPUT_FORMAT = "%-20s:%s";

    public static byte[] digest(byte[] input, String algorithm) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        byte[] result = md.digest(input);
        return result;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

//    public static void main(String[] args) {
//
//        //String algorithm = "SHA-256"; // if you perfer SHA-2
//        String algorithm = "SHA3-256";
//
//        String pText = "Hello World";
//        System.out.println(String.format(OUTPUT_FORMAT, "Input (string)", pText));
//        System.out.println(String.format(OUTPUT_FORMAT, "Input (length)", pText.length()));
//
//        byte[] shaInBytes = digest(pText.getBytes(UTF_8), algorithm);
//        System.out.println(String.format(OUTPUT_FORMAT, algorithm + " (hex) ", bytesToHex(shaInBytes)));
//        // fixed length, 32 bytes, 256 bits.
//        System.out.println(String.format(OUTPUT_FORMAT, algorithm + " (length)", shaInBytes.length));
//
//    }

}
