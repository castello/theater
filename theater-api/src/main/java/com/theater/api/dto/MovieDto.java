package com.theater.api.dto;

import com.theater.api.entity.Movie;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDto {
    private Long id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private String rating;
    private String posterUrl;
    private LocalDate releaseDate;
    private String status;

    public static MovieDto from(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .description(movie.getDescription())
                .durationMinutes(movie.getDurationMinutes())
                .rating(movie.getRating())
                .posterUrl(movie.getPosterUrl())
                .releaseDate(movie.getReleaseDate())
                .status(movie.getStatus().name())
                .build();
    }
}
