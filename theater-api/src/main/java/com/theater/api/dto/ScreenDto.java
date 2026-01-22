package com.theater.api.dto;

import com.theater.api.entity.Screen;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScreenDto {
    private Long id;
    private String name;
    private Integer totalSeats;
    private Long theaterId;
    private String theaterName;

    public static ScreenDto from(Screen screen) {
        return ScreenDto.builder()
                .id(screen.getId())
                .name(screen.getName())
                .totalSeats(screen.getTotalSeats())
                .theaterId(screen.getTheater().getId())
                .theaterName(screen.getTheater().getName())
                .build();
    }
}
