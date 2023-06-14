package ua.nure.teamsync.controllers;

import com.braintreegateway.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.nure.teamsync.payload.Payment;
import ua.nure.teamsync.services.BraintreePaymentService;

import java.math.BigDecimal;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private BraintreePaymentService service;

    @GetMapping("/client_token")
    public ResponseEntity<String> generateClientToken() {
        String token = service.generateClientToken();
        return ResponseEntity.ok(token);
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody Payment paymentForm) {
//        try {
//            amount = new BigDecimal(paymentForm.getChargeAmount());
//        } catch (NumberFormatException e) {
//            log.error("NumberFormatException {}", e);
//        }
        String nonce = paymentForm.getNonce();
        if (nonce != null && !nonce.isBlank()) {
            Transaction result = service.sale(nonce, null);
            return ResponseEntity.ok(result);
        } else
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
    }
}
