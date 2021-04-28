package com.sabu.hmacdemo.hmac;

import com.sabu.hmacdemo.constants.HmacConstants;
import lombok.Getter;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/21
 */
@Getter
public class AuthHeader {

    private final String algorithm = HmacConstants.HMAC_ALGORITHM;

    private final String apiKey;

    private final String nonce;

    private final byte[] digest;

    public AuthHeader(
            String apiKey,
            String nonce,
            byte[] digest) {
        this.apiKey = apiKey;
        this.nonce = nonce;
        this.digest = digest;
    }
}
