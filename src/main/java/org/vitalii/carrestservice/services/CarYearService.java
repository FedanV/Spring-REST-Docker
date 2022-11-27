package org.vitalii.carrestservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vitalii.carrestservice.database.entities.CarYear;
import org.vitalii.carrestservice.dto.CarYearCreateEditDto;
import org.vitalii.carrestservice.dto.CarYearReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;

import java.util.Optional;

public interface CarYearService {

    Page<CarYearReadDto> findAll(CarFilter carFilter, Pageable pageable);

    Optional<CarYearReadDto> findById(Integer id);

    CarYearReadDto save(CarYearCreateEditDto carYear);

    Optional<CarYearReadDto> update(Integer id, CarYearCreateEditDto carYear);

    Boolean deleteById(Integer id);

    Optional<CarYear> findByYear(String year);

}
