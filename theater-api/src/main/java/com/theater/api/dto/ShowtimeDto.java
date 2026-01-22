package com.theater.api.dto;

import com.theater.api.entity.Showtime;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeDto {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long screenId;
    private String screenName;
    private Long theaterId;
    private String theaterName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;

    public static ShowtimeDto from(Showtime showtime) {
        return ShowtimeDto.builder()
                .id(showtime.getId())
                .movieId(showtime.getMovie().getId())
                .movieTitle(showtime.getMovie().getTitle())
                .screenId(showtime.getScreen().getId())
                .screenName(showtime.getScreen().getName())
                .theaterId(showtime.getScreen().getTheater().getId())
                .theaterName(showtime.getScreen().getTheater().getName())
                .startTime(showtime.getStartTime())
                .endTime(showtime.getEndTime())
                .price(showtime.getPrice())
                .build();
    }
}
