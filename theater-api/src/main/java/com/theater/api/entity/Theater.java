package com.theater.api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "theaters")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private String location;

    @Column(columnDefinition = "TEXT")
    private String address;

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Screen> screens = new ArrayList<>();
}
