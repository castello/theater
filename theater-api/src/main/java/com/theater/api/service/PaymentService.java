package com.theater.api.service;

import com.theater.api.dto.PaymentDto;
import com.theater.api.dto.PaymentRequest;
import com.theater.api.entity.Payment;
import com.theater.api.entity.Payment.PaymentMethod;
import com.theater.api.entity.Reservation;
import com.theater.api.entity.Reservation.ReservationStatus;
import com.theater.api.repository.PaymentRepository;
import com.theater.api.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public PaymentDto processPayment(PaymentRequest request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("예약을 찾을 수 없습니다"));

        if (reservation.getStatus() != ReservationStatus.pending) {
            throw new IllegalStateException("대기 중인 예약만 결제할 수 있습니다");
        }

        if (reservation.getPayment() != null) {
            throw new IllegalStateException("이미 결제가 존재합니다");
        }

        PaymentMethod paymentMethod = PaymentMethod.valueOf(request.getPaymentMethod().toLowerCase());

        Payment payment = Payment.builder()
                .reservation(reservation)
                .amount(reservation.getTotalPrice())
                .paymentMethod(paymentMethod)
                .build();

        // 결제 처리 (실제로는 PG 연동)
        payment.complete();
        paymentRepository.save(payment);

        // 예약 확정
        reservation.setStatus(ReservationStatus.confirmed);
        reservation.setPayment(payment);

        return PaymentDto.from(payment);
    }

    public PaymentDto getPayment(Long reservationId) {
        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다"));
        return PaymentDto.from(payment);
    }

    @Transactional
    public PaymentDto refundPayment(Long reservationId) {
        Payment payment = paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다"));

        if (payment.getStatus() == Payment.PaymentStatus.refunded) {
            throw new IllegalStateException("이미 환불된 결제입니다");
        }

        payment.refund();
        payment.getReservation().setStatus(ReservationStatus.cancelled);

        return PaymentDto.from(payment);
    }
}
