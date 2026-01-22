package com.theater.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(length = 10)
    private String rating;

    @Column(name = "poster_url", length = 500)
    private String posterUrl;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MovieStatus status = MovieStatus.upcoming;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Showtime> showtimes = new ArrayList<>();

    public enum MovieStatus {
        showing, upcoming, ended
    }
}
