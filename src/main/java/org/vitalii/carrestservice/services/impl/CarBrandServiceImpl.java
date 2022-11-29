package org.vitalii.carrestservice.services.impl;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vitalii.carrestservice.database.entities.CarBrand;
import org.vitalii.carrestservice.database.entities.QCarBrand;
import org.vitalii.carrestservice.database.querydsl.QPredicates;
import org.vitalii.carrestservice.dto.CarBrandCreateEditDto;
import org.vitalii.carrestservice.dto.CarBrandReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.repositories.CarBrandRepository;
import org.vitalii.carrestservice.services.CarBrandService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarBrandServiceImpl implements CarBrandService {

    private final CarBrandRepository carBrandRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CarBrandReadDto> findAll(CarFilter filter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(filter.manufacturer(), QCarBrand.carBrand.brand::containsIgnoreCase)
                .buildAnd();
        return carBrandRepository.findAll(predicate, pageable)
                .map(brand -> modelMapper.map(brand, CarBrandReadDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarBrandReadDto> findById(Integer id) {
        return carBrandRepository.findById(id)
                .map(brand -> modelMapper.map(brand, CarBrandReadDto.class));
    }

    @Override
    @Transactional
    public CarBrandReadDto save(CarBrandCreateEditDto brandDto) {
        return Optional.of(brandDto)
                .map(carBrand -> modelMapper.map(carBrand, CarBrand.class))
                .map(carBrandRepository::save)
                .map(carBrand -> modelMapper.map(carBrand, CarBrandReadDto.class))
                .orElseThrow();
    }

    @Override
    public Optional<CarBrandReadDto> update(Integer id, CarBrandCreateEditDto brandDto) {
        return carBrandRepository.findById(id)
                .map(carBrand -> {
                    modelMapper.map(brandDto, carBrand);
                    return carBrand;
                })
                .map(carBrandRepository::saveAndFlush)
                .map(carBrand -> modelMapper.map(carBrand, CarBrandReadDto.class));
    }

    @Override
    @Transactional
    public Boolean deleteById(Integer id) {
        return carBrandRepository.findById(id)
                .map(brand -> {
                    carBrandRepository.delete(brand);
                    carBrandRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<CarBrand> findByBrand(String brand) {
        return carBrandRepository.findByBrand(brand);
    }

}
