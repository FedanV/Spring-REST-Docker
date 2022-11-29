package org.vitalii.carrestservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vitalii.carrestservice.dto.CarCreateEditDto;
import org.vitalii.carrestservice.dto.CarReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;

import java.util.Optional;

public interface CarService {

    Page<CarReadDto> findAll(CarFilter carFilter, Pageable pageable);

    Optional<CarReadDto> findById(Integer id);

    CarReadDto save(CarCreateEditDto carDto);

    Optional<CarReadDto> update(Integer id, CarCreateEditDto carDto);

    Boolean deleteById(Integer id);

}
