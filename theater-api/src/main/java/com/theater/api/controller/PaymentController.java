package com.theater.api.controller;

import com.theater.api.dto.PaymentDto;
import com.theater.api.dto.PaymentRequest;
import com.theater.api.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDto> processPayment(
            @Valid @RequestBody PaymentRequest request) {
        PaymentDto payment = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<PaymentDto> getPayment(@PathVariable Long reservationId) {
        return ResponseEntity.ok(paymentService.getPayment(reservationId));
    }

    @PostMapping("/reservation/{reservationId}/refund")
    public ResponseEntity<PaymentDto> refundPayment(@PathVariable Long reservationId) {
        return ResponseEntity.ok(paymentService.refundPayment(reservationId));
    }
}
