package com.theater.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seats",
       uniqueConstraints = @UniqueConstraint(columnNames = {"screen_id", "row_label", "seat_number"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    @Column(name = "row_label", nullable = false, length = 5)
    private String rowLabel;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "seat_type")
    @Builder.Default
    private SeatType seatType = SeatType.standard;

    public enum SeatType {
        standard, premium, wheelchair
    }

    public String getSeatCode() {
        return rowLabel + seatNumber;
    }
}
