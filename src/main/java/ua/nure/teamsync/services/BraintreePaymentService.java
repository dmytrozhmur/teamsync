package ua.nure.teamsync.services;

import com.braintreegateway.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class BraintreePaymentService {

    private final BraintreeGateway gateway;

    public BraintreePaymentService(BraintreeGateway gateway) {
        this.gateway = gateway;
    }

    public String generateClientToken() {
        return gateway.clientToken().generate();
    }

    public Transaction sale(String nonce, String deviceData) {
        TransactionRequest request = new TransactionRequest()
                .paymentMethodNonce(nonce)
                .deviceData(deviceData)
                .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        if (result.isSuccess() || result.getTransaction() != null) {
            Transaction transaction = null;
            try {
                transaction = result.getTarget();
                transaction = gateway.transaction().find(transaction.getId());
            } catch (Exception e) {
                log.error("Exception {}", e);
            }

            return transaction;
        } else {
            return null;
        }
    }
}
