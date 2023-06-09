package com.ltech.gatewayclient.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ltech.gatewayclient.model.dto.InternalUserInfoDto;
import com.ltech.gatewayclient.service.client.UaaClient;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Order(-1)
@Component
public class AddAuthPreFilter implements GlobalFilter {
    private final UaaClient uaaClient;

    private String getTokenFromRequest(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String token = getTokenFromRequest(request);

        try {
            InternalUserInfoDto internalUserInfoDto = uaaClient.getUserInfo(token);

            request
                    .mutate()
                    .header("username", internalUserInfoDto.getUsername())
                    .header("email", internalUserInfoDto.getEmail())
                    .header("roles", String.join(",", internalUserInfoDto.getRoles()));
        }catch (Exception ex) {
            System.out.println(ex);
        }
        return chain.filter(exchange.mutate().request(request).build());
    }
}