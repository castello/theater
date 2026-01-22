package com.theater.api.dto;

import com.theater.api.entity.Theater;
import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TheaterDto {
    private Long id;
    private String name;
    private String location;
    private String address;
    private List<ScreenDto> screens;

    public static TheaterDto from(Theater theater) {
        return TheaterDto.builder()
                .id(theater.getId())
                .name(theater.getName())
                .location(theater.getLocation())
                .address(theater.getAddress())
                .build();
    }

    public static TheaterDto fromWithScreens(Theater theater) {
        return TheaterDto.builder()
                .id(theater.getId())
                .name(theater.getName())
                .location(theater.getLocation())
                .address(theater.getAddress())
                .screens(theater.getScreens().stream().map(ScreenDto::from).toList())
                .build();
    }
}
