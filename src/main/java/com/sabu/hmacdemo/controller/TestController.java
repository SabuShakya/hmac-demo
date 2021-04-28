package com.sabu.hmacdemo.controller;

import com.sabu.hmacdemo.constants.HmacConstants;
import com.sabu.hmacdemo.dto.TestDTO;
import com.sabu.hmacdemo.util.HmacUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Sabu Shakya
 * @email <sabu.shakya@f1soft.com>
 * @createdDate 2021/04/26
 */
@RestController
@AllArgsConstructor
public class TestController {

    private final RestTemplate restTemplate;

    @GetMapping("/testHMAC")
    public ResponseEntity<?> testHMAC() {
        TestDTO testDTO = new TestDTO();
        testDTO.setName("Sabu Shakya");

        long unixTime = System.currentTimeMillis() / 1000L;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HmacConstants.AUTHORIZATION_HEADER, HmacUtil.generateHmacToken(
                HmacUtil.getPayloadInByteArray(testDTO,HttpMethod.POST.name()),
                HttpMethod.POST.name(),
                MediaType.APPLICATION_JSON_VALUE,
                "/connectnpay/hmac",
                unixTime
        ));
        headers.set(HmacConstants.AUTHORIZATION_TIMESTAMP, String.valueOf(unixTime));

        HttpEntity<TestDTO> entity = new HttpEntity(testDTO, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:9090/connectnpay/hmac",
                HttpMethod.POST,
                entity,
                String.class);

        return ResponseEntity.ok("Tested");
    }

}
