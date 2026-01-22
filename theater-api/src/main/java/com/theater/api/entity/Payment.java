package com.theater.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus status = PaymentStatus.pending;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public enum PaymentMethod {
        card, kakao, naver
    }

    public enum PaymentStatus {
        pending, completed, refunded
    }

    public void complete() {
        this.status = PaymentStatus.completed;
        this.paidAt = LocalDateTime.now();
    }

    public void refund() {
        this.status = PaymentStatus.refunded;
    }
}
