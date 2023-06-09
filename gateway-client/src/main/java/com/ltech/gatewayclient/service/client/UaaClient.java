package com.ltech.gatewayclient.service.client;

import com.ltech.gatewayclient.model.dto.InternalUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class UaaClient {
    private final  RestTemplate restTemplate;
    @Value("${uaa.uri}")
    private String uaaUrl;
    @Value("${uaa.access_token}")
    private String accessToken;
    public InternalUserInfoDto getUserInfo(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(uaaUrl + "/user/"+token, HttpMethod.GET, httpEntity, InternalUserInfoDto.class).getBody();
    }
}
