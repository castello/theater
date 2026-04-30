package com.theater.api.repository;

import com.theater.api.entity.DiscountPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountPolicyRepository extends JpaRepository<DiscountPolicy, Long> {
    List<DiscountPolicy> findAllByActiveTrue();
}
