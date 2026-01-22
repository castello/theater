package com.theater.api.repository;

import com.theater.api.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TheaterRepository extends JpaRepository<Theater, Long> {
    List<Theater> findByLocation(String location);
}
