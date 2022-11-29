package org.vitalii.carrestservice.services.impl;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vitalii.carrestservice.database.entities.Car;
import org.vitalii.carrestservice.database.entities.QCar;
import org.vitalii.carrestservice.database.querydsl.QPredicates;
import org.vitalii.carrestservice.dto.CarCreateEditDto;
import org.vitalii.carrestservice.dto.CarReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.repositories.CarRepository;
import org.vitalii.carrestservice.repositories.CategoryRepository;
import org.vitalii.carrestservice.services.CarService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CarReadDto> findAll(CarFilter carFilter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(carFilter.manufacturer(), QCar.car.brand.brand::eq)
                .add(carFilter.model(), QCar.car.model.model::eq)
                .add(carFilter.maxYear(), QCar.car.year.year::lt)
                .add(carFilter.minYear(), QCar.car.year.year::gt)
                .add(carFilter.category(), c -> {
                    if(categoryRepository.findByName(c).isPresent()) {
                        return QCar.car.categories.contains(categoryRepository.findByName(c).get());
                    } else {
                        return null;
                    }
                })
                .buildAnd();
        return carRepository.findAll(predicate, pageable)
                .map(car -> modelMapper.map(car, CarReadDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarReadDto> findById(Integer id) {
        return carRepository.findById(id)
                .map(car -> modelMapper.map(car, CarReadDto.class));
    }

    @Override
    @Transactional
    public CarReadDto save(CarCreateEditDto carDto) {
        return Optional.of(carDto)
                .map(c -> modelMapper.map(c, Car.class))
                .map(carRepository::save)
                .map(car -> modelMapper.map(car, CarReadDto.class))
                .orElseThrow();
    }

    @Override
    @Transactional
    public Optional<CarReadDto> update(Integer id, CarCreateEditDto carDto) {
        return carRepository.findById(id)
                .map(car -> {
                    modelMapper.map(carDto, car);
                    return car;
                })
                .map(carRepository::saveAndFlush)
                .map(car -> modelMapper.map(car, CarReadDto.class));
    }

    @Override
    @Transactional
    public Boolean deleteById(Integer id) {
        return carRepository.findById(id)
                .map(car -> {
                    carRepository.delete(car);
                    carRepository.flush();
                    return true;
                })
                .orElse(false);
    }

}
