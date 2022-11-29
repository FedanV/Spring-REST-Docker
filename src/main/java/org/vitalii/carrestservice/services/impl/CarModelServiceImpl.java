package org.vitalii.carrestservice.services.impl;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vitalii.carrestservice.database.entities.CarModel;
import org.vitalii.carrestservice.database.entities.QCarModel;
import org.vitalii.carrestservice.database.querydsl.QPredicates;
import org.vitalii.carrestservice.dto.CarModelCreateEditDto;
import org.vitalii.carrestservice.dto.CarModelReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.repositories.CarModelRepository;
import org.vitalii.carrestservice.services.CarModelService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarModelServiceImpl implements CarModelService {

    private final CarModelRepository carModelRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CarModelReadDto> findAll(CarFilter carFilter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(carFilter.model(), QCarModel.carModel.model::containsIgnoreCase)
                .buildAnd();
        return carModelRepository.findAll(predicate, pageable)
                .map(carModel -> modelMapper.map(carModel, CarModelReadDto.class));

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarModelReadDto> findById(Integer id) {
        return carModelRepository.findById(id)
                .map(model -> modelMapper.map(model, CarModelReadDto.class));
    }

    @Override
    @Transactional
    public CarModelReadDto save(CarModelCreateEditDto carModel) {
        return Optional.of(carModel)
                .map(modelDto -> modelMapper.map(modelDto, CarModel.class))
                .map(carModelRepository::save)
                .map(model -> modelMapper.map(model, CarModelReadDto.class))
                .orElseThrow();
    }

    @Override
    public Optional<CarModelReadDto> update(Integer id, CarModelCreateEditDto carModel) {
        return carModelRepository.findById(id)
                .map(model -> {
                    modelMapper.map(carModel, model);
                    return model;
                })
                .map(carModelRepository::saveAndFlush)
                .map(model -> modelMapper.map(model, CarModelReadDto.class));
    }

    @Override
    @Transactional
    public Boolean deleteById(Integer id) {
        return carModelRepository.findById(id)
                .map(model -> {
                    carModelRepository.delete(model);
                    carModelRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<CarModel> findByModel(String model) {
        return carModelRepository.findByModel(model);
    }

}
