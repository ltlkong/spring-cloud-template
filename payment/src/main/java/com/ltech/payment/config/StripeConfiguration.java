package com.ltech.payment.config;

import com.ltech.payment.service.StripePaymentService;
import com.stripe.Stripe;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StripeConfiguration {
    private static final Logger logger = LogManager.getLogger(StripeConfiguration.class);
    @Value("${stripe.api-key}")
    private String apiKey;
    @EventListener(ApplicationReadyEvent.class)
    public void setUpStripe() {
        Stripe.apiKey = this.apiKey;
        logger.info("Finished setup stripe api");
    }
}
