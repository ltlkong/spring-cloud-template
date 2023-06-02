package com.ltech.payment.service;

import com.ltech.payment.model.Transaction;
import com.ltech.payment.model.dto.ProductDto;
import com.ltech.payment.model.enumeration.TransactionStatus;
import com.ltech.payment.repository.TransactionRepository;
import com.ltech.payment.utils.UrlUtil;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Service
@Transactional
@Qualifier("stripePaymentService")
public class StripePaymentService implements PaymentService{
    @Value("${stripe.webhook-secret}")
    private String webhookSecret;
    private static final String provider = "Stripe";
    private static final Logger logger = LogManager.getLogger(StripePaymentService.class);
    private final TransactionRepository transactionRepository;
    private final UrlUtil urlUtil;

    public StripePaymentService(TransactionRepository transactionRepository, UrlUtil urlUtil) {
        this.transactionRepository = transactionRepository;
        this.urlUtil = urlUtil;
    }

    @Override
    public String pay(ProductDto product) throws Exception{
        String transactionId = UUID.randomUUID().toString();

        // Update url for callback
        product.setSuccessUrl(assignTransactionIdToUrl(product.getSuccessUrl(), transactionId));
        product.setCancelUrl(assignTransactionIdToUrl(product.getCancelUrl(),transactionId));

        Session checkoutSession = createCheckoutSession(product);

        saveTransactionFromSessionAndProduct(checkoutSession,product,transactionId);


        return checkoutSession.getUrl();
    }

    @Override
    public Object subscribe(ProductDto productxs) throws Exception {
        return null;
    }

    private void saveTransactionFromSessionAndProduct(Session checkoutSession, ProductDto product, String id) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setPrice(product.getPrice());
        transaction.setUserKey(product.getCustomerId());
        transaction.setCurrency(product.getCurrency());
        transaction.setThirdPartyId(checkoutSession.getId());
        transaction.setThirdPartyType(this.provider);
        transaction.setStatus(TransactionStatus.PENDING.name());
        transactionRepository.save(transaction);
    }
    private String assignTransactionIdToUrl(String url, String id) throws URISyntaxException {
        URI uri = new URI(url);
        String newUrl = UriComponentsBuilder.fromUri(uri).queryParam("transactionId",id).build().toString();

        return newUrl;

    }

    private Session createCheckoutSession(ProductDto product) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(product.getSuccessUrl())
                .setCancelUrl(product.getCancelUrl())
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(product.getQuantity())
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency(product.getCurrency())
                                                .setUnitAmount(product.getPrice())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(product.getName())
                                                                .setDescription(product.getDescription())
                                                                .addAllImage(product.getImagesUrl())
                                                                .build())
                                                .build())
                                .build())
                .build();

        Session session = Session.create(params);

        return session;
    }

    // TODO: Implement webhook for stripe
    @Override
    public void paidSuccess(String id) {
        transactionRepository.updateStatusByThirdPartyIdAndThirdPartyProvider(id, provider ,TransactionStatus.PAID.name());
    }

    // TODO: Implement webhook for stripe
    @Override
    public void paidFailed(String id) {
        transactionRepository.updateStatusByThirdPartyIdAndThirdPartyProvider(id, provider, TransactionStatus.FAILED.name());
    }

    @Override
    public void processResult(Object payload, String secret) throws Exception {
        Event event = Webhook.constructEvent(
                (String) payload, secret, webhookSecret
        );

        switch (event.getType()) {
            case "checkout.session.async_payment_succeeded": {
                logger.info("Payment succeed");
                paidSuccess(event.getId());
                break;
            }
            case "checkout.session.async_payment_failed": {
                logger.info("Payment failed");
                paidFailed(event.getId());
                break;
            }
            default:
                logger.info("Unhandled event: " + event.getType());
        }
    }
}
