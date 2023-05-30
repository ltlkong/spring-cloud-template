package com.ltech.payment.service;

import com.ltech.payment.model.dtos.ProductDto;
import com.stripe.exception.SignatureVerificationException;
import org.springframework.util.MultiValueMap;

import java.net.http.HttpHeaders;

public interface PaymentService {
    public Object pay(ProductDto product) throws Exception;
    public Object subscribe(ProductDto productxs) throws Exception;
    public void paidSuccess(String id);
    public void paidFailed(String id);
    public void processResult(Object payload, String secret) throws Exception;
}
