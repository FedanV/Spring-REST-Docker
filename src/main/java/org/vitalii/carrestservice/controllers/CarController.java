package org.vitalii.carrestservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.vitalii.carrestservice.dto.CarCreateEditDto;
import org.vitalii.carrestservice.dto.CarReadDto;
import org.vitalii.carrestservice.dto.PageResponse;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.services.CarService;
import org.vitalii.carrestservice.services.validators.CarCreateEditDtoValidator;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarController implements SecuredRestController {

    private final CarService carService;
    private final CarCreateEditDtoValidator carCreateEditDtoValidator;

    @GetMapping
    public PageResponse<CarReadDto> findAllCars(CarFilter carFilter, Pageable pageable) {
        return PageResponse.of(carService.findAll(carFilter, pageable));
    }

    @GetMapping("/{id}")
    public CarReadDto findCarById(@PathVariable("id") Integer id) {
        return carService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarReadDto createCar(@RequestBody @Valid CarCreateEditDto carCreateEditDto,
                                BindingResult result) throws BindException {
        carCreateEditDtoValidator.validate(carCreateEditDto, result);
        if(result.hasErrors()) {
            throw new BindException(result);
        }
        return carService.save(carCreateEditDto);
    }

    @PutMapping("/{id}")
    public CarReadDto updateCar(@PathVariable("id") Integer id,
                                @RequestBody @Valid CarCreateEditDto carCreateEditDto,
                                BindingResult result) throws BindException {
        carCreateEditDtoValidator.validate(carCreateEditDto, result);
        if(result.hasErrors()) {
            throw new BindException(result);
        }
        return carService.update(id, carCreateEditDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable("id") Integer id) {
        if (Boolean.FALSE.equals(carService.deleteById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
