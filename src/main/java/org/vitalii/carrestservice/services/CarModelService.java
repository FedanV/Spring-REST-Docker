package org.vitalii.carrestservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vitalii.carrestservice.database.entities.CarModel;
import org.vitalii.carrestservice.dto.CarModelCreateEditDto;
import org.vitalii.carrestservice.dto.CarModelReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;

import java.util.Optional;

public interface CarModelService {

    Page<CarModelReadDto> findAll(CarFilter carFilter, Pageable pageable);

    Optional<CarModelReadDto> findById(Integer id);

    CarModelReadDto save(CarModelCreateEditDto carModel);

    Optional<CarModelReadDto> update(Integer id, CarModelCreateEditDto carModel);

    Boolean deleteById(Integer id);

    Optional<CarModel> findByModel(String model);

}
