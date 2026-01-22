package com.theater.api.service;

import com.theater.api.dto.ScreenDto;
import com.theater.api.dto.TheaterDto;
import com.theater.api.entity.Theater;
import com.theater.api.repository.ScreenRepository;
import com.theater.api.repository.TheaterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TheaterService {

    private final TheaterRepository theaterRepository;
    private final ScreenRepository screenRepository;

    public List<TheaterDto> getAllTheaters() {
        return theaterRepository.findAll().stream()
                .map(TheaterDto::from)
                .toList();
    }

    public TheaterDto getTheater(Long id) {
        Theater theater = theaterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("극장을 찾을 수 없습니다: " + id));
        return TheaterDto.fromWithScreens(theater);
    }

    public List<TheaterDto> getTheatersByLocation(String location) {
        return theaterRepository.findByLocation(location).stream()
                .map(TheaterDto::from)
                .toList();
    }

    public List<ScreenDto> getScreensByTheater(Long theaterId) {
        return screenRepository.findByTheaterId(theaterId).stream()
                .map(ScreenDto::from)
                .toList();
    }
}
