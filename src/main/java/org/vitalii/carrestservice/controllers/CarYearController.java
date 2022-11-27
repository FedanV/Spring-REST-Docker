package org.vitalii.carrestservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.vitalii.carrestservice.dto.CarYearCreateEditDto;
import org.vitalii.carrestservice.dto.CarYearReadDto;
import org.vitalii.carrestservice.dto.PageResponse;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.services.CarYearService;
import org.vitalii.carrestservice.services.validators.CarYearCreateEditDtoValidator;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/manufacturers/models/years")
@RequiredArgsConstructor
public class CarYearController {

    private final CarYearService carYearService;
    private final CarYearCreateEditDtoValidator carYearCreateEditDtoValidator;

    @GetMapping
    public PageResponse<CarYearReadDto> findAllYears(CarFilter carFilter, Pageable pageable) {
        return PageResponse.of(carYearService.findAll(carFilter, pageable));
    }

    @GetMapping("/{id}")
    public CarYearReadDto findYearById(@PathVariable("id") Integer id) {
        return carYearService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarYearReadDto createYear(@RequestBody @Valid CarYearCreateEditDto yearDto,
                                     BindingResult result) throws BindException {
        carYearCreateEditDtoValidator.validate(yearDto, result);
        if(result.hasErrors()) {
            throw new BindException(result);
        }
        return carYearService.save(yearDto);
    }

    @PutMapping("/{id}")
    public CarYearReadDto updateYear(@PathVariable("id") Integer id,
                                     @RequestBody @Valid CarYearCreateEditDto yearDto,
                                     BindingResult result) throws BindException {
        carYearCreateEditDtoValidator.validate(yearDto, result);
        if(result.hasErrors()) {
            throw new BindException(result);
        }
        return carYearService.update(id, yearDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteYear(@PathVariable("id") Integer id) {
        if (Boolean.FALSE.equals(carYearService.deleteById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
