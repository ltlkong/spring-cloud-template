package com.ltech.payment.controller;

import com.ltech.payment.model.dtos.ProductDto;
import com.ltech.payment.service.PaymentService;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpHeaders;

@RestController @AllArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/pay")
    public ResponseEntity<String> pay(@Validated @RequestBody ProductDto product) throws Exception {
        String checkoutUrl = (String)paymentService.pay(product);

        return ResponseEntity.ok().body(checkoutUrl);
    }

    @PostMapping("/pay/status")
    public void paid(HttpEntity<String> httpEntity) throws Exception{
        paymentService.processResult(httpEntity.getBody(), httpEntity.getHeaders().getFirst("Stripe-Signature"));
    }

    @GetMapping("/")
    public String test() {
        return "Payment service";
    }
}
