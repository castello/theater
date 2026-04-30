package com.theater.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "discount_policies")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false)
    private Payment.DiscountType discountType;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "discount_rate", nullable = false, precision = 4, scale = 2)
    private BigDecimal discountRate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    public boolean matches(LocalTime time) {
        if (startTime.isBefore(endTime)) {
            return !time.isBefore(startTime) && time.isBefore(endTime);
        }
        return !time.isBefore(startTime) || time.isBefore(endTime);
    }
}
