package com.ltech.payment.service;

import com.ltech.payment.model.dto.ProductDto;

public interface PaymentService {
    Object pay(ProductDto product) throws Exception;
    Object subscribe(ProductDto product) throws Exception;
    void paidSuccess(String id);
    void paidFailed(String id);
    void processResult(Object payload, String secret) throws Exception;
}
