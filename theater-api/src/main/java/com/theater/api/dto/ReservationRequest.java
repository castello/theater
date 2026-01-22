package com.theater.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {

    @NotNull(message = "사용자 ID는 필수입니다")
    private Long userId;

    @NotNull(message = "상영 ID는 필수입니다")
    private Long showtimeId;

    @NotEmpty(message = "좌석을 선택해주세요")
    private List<Long> seatIds;
}
