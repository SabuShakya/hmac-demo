package com.sabu.hmacdemo.hmac;

import com.sabu.hmacdemo.constants.HmacConstants;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/21
 */
@Slf4j
@NoArgsConstructor
public class HmacSignatureBuilder {

    protected String requestMethod;

    protected String path;

    protected String contentType;

    protected String nonce;

    protected Long timeStamp;

    protected byte[] payload;

    protected String algorithm;

    protected String apiKey;

    protected String apiSecret;

    protected byte DELIMITER = '|';

    public HmacSignatureBuilder algorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public HmacSignatureBuilder apiKey(String key) {
        this.apiKey = key;
        return this;
    }

    public HmacSignatureBuilder nonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    public HmacSignatureBuilder apiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
        return this;
    }

    public HmacSignatureBuilder requestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
        return this;
    }

    public HmacSignatureBuilder path(String path) {
        this.path = path;
        return this;
    }

    public HmacSignatureBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public HmacSignatureBuilder timeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public HmacSignatureBuilder payload(byte[] payloadBytes) {
        this.payload = payloadBytes;
        return this;
    }

    public byte[] build() {
        Objects.requireNonNull(algorithm, "algorithm");
        Objects.requireNonNull(apiKey, "apiKey");
//        Objects.requireNonNull(payload, "payload");

        try {
            final Mac digest = Mac.getInstance(HmacConstants.HMAC_ALGORITHM);
            SecretKeySpec secretKey = new SecretKeySpec(apiSecret.getBytes(), HmacConstants.HMAC_ALGORITHM);
            digest.init(secretKey);
            digest.update(DELIMITER);

            digest.update(requestMethod.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);

            digest.update(path.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);

            digest.update(contentType.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);

            digest.update(nonce.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);

            digest.update(timeStamp.byteValue());
            digest.update(DELIMITER);

            digest.update(payload);
            digest.update(DELIMITER);

            digest.update(algorithm.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);

            digest.update(apiKey.getBytes(StandardCharsets.UTF_8));
            digest.update(DELIMITER);

            final byte[] signatureBytes = digest.doFinal();
            digest.reset();

            return signatureBytes;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't create signature: " + e.getMessage(), e);
        } catch (InvalidKeyException ex) {
            log.error("Invalid Key Exception");
        }

        return null;
    }

    public boolean isHashEquals(byte[] expectedSignature) {
        final byte[] signature = build();

        String base64Signature = Base64.getEncoder().encodeToString(signature);
        String base64Expected = Base64.getEncoder().encodeToString(expectedSignature);
        log.info("Base64 signature : " + base64Signature);
        log.info("Base64 expected : " + base64Expected);
        return base64Signature.equals(base64Expected);
    }

    public String buildAsBase64String() {
        return DatatypeConverter.printBase64Binary(build());
    }

}

