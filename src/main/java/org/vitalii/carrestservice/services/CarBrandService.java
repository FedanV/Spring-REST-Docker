package org.vitalii.carrestservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vitalii.carrestservice.database.entities.CarBrand;
import org.vitalii.carrestservice.dto.CarBrandCreateEditDto;
import org.vitalii.carrestservice.dto.CarBrandReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;

import java.util.Optional;

public interface CarBrandService {

    Page<CarBrandReadDto> findAll(CarFilter filter, Pageable pageable);

    Optional<CarBrandReadDto> findById(Integer id);

    CarBrandReadDto save(CarBrandCreateEditDto brandDto);

    Optional<CarBrandReadDto> update(Integer id, CarBrandCreateEditDto brandDto);

    Boolean deleteById(Integer id);

    Optional<CarBrand> findByBrand(String brand);

}
