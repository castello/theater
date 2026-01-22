package com.theater.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull(message = "예약 ID는 필수입니다")
    private Long reservationId;

    @NotNull(message = "결제 수단은 필수입니다")
    private String paymentMethod;  // card, kakao, naver
}
