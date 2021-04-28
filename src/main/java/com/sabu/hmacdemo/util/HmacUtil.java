package com.sabu.hmacdemo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sabu.hmacdemo.constants.HmacConstants;
import com.sabu.hmacdemo.constants.PatternConstants;
import com.sabu.hmacdemo.hmac.AuthHeader;
import com.sabu.hmacdemo.hmac.HmacSignatureBuilder;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

import static com.sabu.hmacdemo.util.HmacKeyGenerator.generateNonce;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/26
 */
public class HmacUtil {

    public static String generateHmacToken(byte[] payload,
                                           String requestMethod,
                                           String contentType,
                                           String url,
                                           long unixTimeStamp
    ) {
        final String nonce = generateNonce();
        String apiKey = "0SJZhK0ix6bks38pjC2UTyhxlDEN42jT";
        String apiSecret = "kAzSY88IoABo8dyYNfUsYdbE4fPKhpCw";

        final HmacSignatureBuilder signatureBuilder = new HmacSignatureBuilder()
                .requestMethod(requestMethod)
                .path(url)
                .contentType(contentType)
                .nonce(nonce)
                .timeStamp(unixTimeStamp)
                .payload(payload)
                .algorithm("HmacSHA512")
                .apiKey(apiKey)
                .apiSecret(apiSecret);

        final String signature = signatureBuilder
                .buildAsBase64String();

        System.out.println("Signature::::::"+ signature);

        String authToken = HmacConstants.HMAC_ALGORITHM +
                " " +
                apiKey +
                ":" +
                nonce +
                ":" +
                signature;

        return authToken;
    }

    public static byte[] getByteArrayFromObject(Object requestObject) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] byteArray = new byte[0];
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(requestObject);
            out.flush();
            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return byteArray;
    }

    public static <T> byte[] getPayloadInByteArray(T requestObj,String httpMethod) {
        byte[] contentAsByteArray;

        if (httpMethod.equalsIgnoreCase(HttpMethod.GET.name()))
            contentAsByteArray = "{}".getBytes();
        else {
            String body = null;
            try {
                body = new ObjectMapper().writeValueAsString(requestObj);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            contentAsByteArray = SHAUtil.digest(body.getBytes(StandardCharsets.UTF_8), HmacConstants.PAYLOAD_HASHING_ALGORITHM);
        }
        return contentAsByteArray;
    }

    public static AuthHeader validateAndGetAuthHeader(HttpServletRequest request) {

        final String authHeader = request.getHeader(HmacConstants.AUTHORIZATION_HEADER);
        final String requestTimeStamp = request.getHeader(HmacConstants.AUTHORIZATION_TIMESTAMP);

        validateNullOrEmptyHeaders(authHeader, requestTimeStamp);

        Matcher matcher = validateHeaderPattern(authHeader);

        validateTimeStamp(requestTimeStamp);

        final String algorithm = matcher.group(1);
        final String apiKey = matcher.group(2);
        final String nonce = matcher.group(3);
        final String receivedDigest = matcher.group(4);


        return new AuthHeader(
                apiKey,
                nonce,
                DatatypeConverter.parseBase64Binary(receivedDigest));
    }

    private static void validateTimeStamp(String requestTimeStamp) {
        Matcher matcher;
        matcher = PatternConstants.AUTHORIZATION_TIMESTAMP_PATTERN.matcher(requestTimeStamp);
        if (!matcher.matches())
            throw new RuntimeException("X-Authorization-Timestamp format is not correct.");

        checkIfTimeStampIsWithinTolerance(requestTimeStamp);
    }

    private static void checkIfTimeStampIsWithinTolerance(String requestTimeStamp) {
        long unixCurrent = System.currentTimeMillis() / 1000L;
        long unixTimestamp = Long.parseLong(requestTimeStamp);
        long tolerance = HmacConstants.TIME_STAMP_TOLERANCE;

        if (unixTimestamp > unixCurrent + tolerance)
            throw new RuntimeException("X-Authorization-Timestamp is too far in the future.");

        else if (unixTimestamp < unixCurrent - tolerance)
            throw new RuntimeException("X-Authorization-Timestamp is too far in the past.");
    }

    private static Matcher validateHeaderPattern(String authHeader) {
        Matcher matcher = PatternConstants.AUTHORIZATION_HEADER_PATTERN.matcher(authHeader);
        if (!matcher.matches())
            throw new RuntimeException("X-Authorization format is not correct."); // invalid authorization token
        return matcher;
    }

    private static void validateNullOrEmptyHeaders(String authHeader, String requestTimeStamp) {
        if (!((authHeader != null && authHeader.trim().length() > 0)
                && (requestTimeStamp != null && requestTimeStamp.trim().length() > 0))) {
            throw new RuntimeException("Authorization Header or Authorization Timestamp not found.");
        }
    }

    public static byte[] extractPayload(String httpMethod, CachedRequestWrapper requestWrapper) throws IOException {
        byte[] contentAsByteArray;

        if (httpMethod.equalsIgnoreCase(HttpMethod.GET.name()))
            contentAsByteArray = "".getBytes();
        else {
            String body = requestWrapper.getBody();
            contentAsByteArray = SHAUtil.digest(body.getBytes(StandardCharsets.UTF_8), HmacConstants.PAYLOAD_HASHING_ALGORITHM);
        }
        return contentAsByteArray;
    }
}
