package com.theater.api.service;

import com.theater.api.entity.DiscountPolicy;
import com.theater.api.entity.Payment.DiscountType;
import com.theater.api.entity.Seat;
import com.theater.api.entity.SeatTypePrice;
import com.theater.api.entity.Showtime;
import com.theater.api.repository.DiscountPolicyRepository;
import com.theater.api.repository.SeatTypePriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PricingPolicyService {

    private final SeatTypePriceRepository seatTypePriceRepository;
    private final DiscountPolicyRepository discountPolicyRepository;

    public BigDecimal calculateReservationPrice(Showtime showtime, List<Seat> seats) {
        BigDecimal basePrice = showtime.getPrice();
        BigDecimal total = BigDecimal.ZERO;
        for (Seat seat : seats) {
            BigDecimal multiplier = seatTypePriceRepository
                    .findBySeatTypeAndActiveTrue(seat.getSeatType())
                    .map(SeatTypePrice::getPriceMultiplier)
                    .orElse(BigDecimal.ONE);
            total = total.add(basePrice.multiply(multiplier));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public DiscountResolution resolveDiscount(LocalTime startTime, BigDecimal originalAmount) {
        List<DiscountPolicy> policies = discountPolicyRepository.findAllByActiveTrue();
        for (DiscountPolicy policy : policies) {
            if (policy.matches(startTime)) {
                BigDecimal discount = originalAmount
                        .multiply(policy.getDiscountRate())
                        .setScale(2, RoundingMode.HALF_UP);
                return new DiscountResolution(policy.getDiscountType(), discount);
            }
        }
        return new DiscountResolution(DiscountType.none, BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
    }

    public record DiscountResolution(DiscountType discountType, BigDecimal discountAmount) {}
}
