package com.theater.api.dto;

import com.theater.api.entity.Seat;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatDto {
    private Long id;
    private String rowLabel;
    private Integer seatNumber;
    private String seatType;
    private String seatCode;
    private boolean available;

    public static SeatDto from(Seat seat) {
        return SeatDto.builder()
                .id(seat.getId())
                .rowLabel(seat.getRowLabel())
                .seatNumber(seat.getSeatNumber())
                .seatType(seat.getSeatType().name())
                .seatCode(seat.getSeatCode())
                .available(true)
                .build();
    }

    public static SeatDto from(Seat seat, boolean available) {
        SeatDto dto = from(seat);
        dto.setAvailable(available);
        return dto;
    }
}
