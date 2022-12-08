package org.vitalii.carrestservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.vitalii.carrestservice.dto.CarBrandCreateEditDto;
import org.vitalii.carrestservice.dto.CarBrandReadDto;
import org.vitalii.carrestservice.dto.PageResponse;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.services.CarBrandService;
import org.vitalii.carrestservice.services.validators.CarBrandCreateEditDtoValidator;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/manufacturers")
@RequiredArgsConstructor
public class CarBrandController implements SecuredRestController {

    private final CarBrandService carBrandService;
    private final CarBrandCreateEditDtoValidator carBrandDtoValidator;

    @GetMapping
    public PageResponse<CarBrandReadDto> findAllManufactures(CarFilter filter, Pageable pageable) {
        Page<CarBrandReadDto> page = carBrandService.findAll(filter, pageable);
        return PageResponse.of(page);
    }

    @GetMapping("/{id}")
    public CarBrandReadDto findManufactureById(@PathVariable("id") Integer id) {
        return carBrandService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarBrandReadDto createManufacture(@RequestBody @Valid CarBrandCreateEditDto brandDto,
                                             BindingResult result) throws BindException {
        carBrandDtoValidator.validate(brandDto, result);
        if(result.hasErrors()) {
            throw new BindException(result);
        }
        return carBrandService.save(brandDto);
    }

    @PutMapping("/{id}")
    public CarBrandReadDto updateManufacture(@PathVariable("id") Integer id,
                                             @RequestBody @Valid CarBrandCreateEditDto brandDto,
                                             BindingResult result) throws BindException {
        carBrandDtoValidator.validate(brandDto, result);
        if(result.hasErrors()) {
            throw new BindException(result);
        }
        return carBrandService.update(id, brandDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteManufacture(@PathVariable("id") Integer id) {
        if(Boolean.FALSE.equals(carBrandService.deleteById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
