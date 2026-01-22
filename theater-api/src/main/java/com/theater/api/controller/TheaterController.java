package com.theater.api.controller;

import com.theater.api.dto.ScreenDto;
import com.theater.api.dto.TheaterDto;
import com.theater.api.service.TheaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/theaters")
@RequiredArgsConstructor
public class TheaterController {

    private final TheaterService theaterService;

    @GetMapping
    public ResponseEntity<List<TheaterDto>> getAllTheaters() {
        return ResponseEntity.ok(theaterService.getAllTheaters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TheaterDto> getTheater(@PathVariable Long id) {
        return ResponseEntity.ok(theaterService.getTheater(id));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity<List<TheaterDto>> getTheatersByLocation(@PathVariable String location) {
        return ResponseEntity.ok(theaterService.getTheatersByLocation(location));
    }

    @GetMapping("/{id}/screens")
    public ResponseEntity<List<ScreenDto>> getScreens(@PathVariable Long id) {
        return ResponseEntity.ok(theaterService.getScreensByTheater(id));
    }
}
