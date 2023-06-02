package com.ltech.payment.controller;

import com.ltech.payment.model.dto.ProductDto;
import com.ltech.payment.service.PaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
