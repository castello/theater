package com.theater.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "seat_type_prices")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatTypePrice {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type")
    private Seat.SeatType seatType;

    @Column(name = "price_multiplier", nullable = false, precision = 4, scale = 2)
    private BigDecimal priceMultiplier;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
