package org.vitalii.carrestservice.services.impl;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vitalii.carrestservice.database.entities.CarYear;
import org.vitalii.carrestservice.database.entities.QCarYear;
import org.vitalii.carrestservice.database.querydsl.QPredicates;
import org.vitalii.carrestservice.dto.CarYearCreateEditDto;
import org.vitalii.carrestservice.dto.CarYearReadDto;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.repositories.CarYearRepository;
import org.vitalii.carrestservice.services.CarYearService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarYearServiceImpl implements CarYearService {

    private final CarYearRepository carYearRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CarYearReadDto> findAll(CarFilter carFilter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(carFilter.maxYear(), QCarYear.carYear.year::lt)
                .add(carFilter.minYear(), QCarYear.carYear.year::gt)
                .buildAnd();
        return carYearRepository.findAll(predicate, pageable)
                .map(year -> modelMapper.map(year, CarYearReadDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarYearReadDto> findById(Integer id) {
        return carYearRepository.findById(id)
                .map(year -> modelMapper.map(year, CarYearReadDto.class));
    }

    @Override
    @Transactional
    public CarYearReadDto save(CarYearCreateEditDto carYear) {
        return Optional.of(carYear)
                .map(yearDto -> modelMapper.map(yearDto, CarYear.class))
                .map(carYearRepository::save)
                .map(year -> modelMapper.map(year, CarYearReadDto.class))
                .orElseThrow();
    }

    @Override
    public Optional<CarYearReadDto> update(Integer id, CarYearCreateEditDto carYear) {
        return carYearRepository.findById(id)
                .map(year -> {
                    modelMapper.map(carYear, year);
                    return year;
                })
                .map(carYearRepository::saveAndFlush)
                .map(year -> modelMapper.map(year, CarYearReadDto.class));
    }

    @Override
    @Transactional
    public Boolean deleteById(Integer id) {
        return carYearRepository.findById(id)
                .map(year -> {
                    carYearRepository.delete(year);
                    carYearRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    @Override
    public Optional<CarYear> findByYear(String year) {
        return carYearRepository.findByYear(year);
    }

}
