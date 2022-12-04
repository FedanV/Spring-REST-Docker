package org.vitalii.carrestservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.vitalii.carrestservice.dto.CarModelCreateEditDto;
import org.vitalii.carrestservice.dto.CarModelReadDto;
import org.vitalii.carrestservice.dto.PageResponse;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.services.CarModelService;
import org.vitalii.carrestservice.services.validators.CarModelCreateEditDtoValidator;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/manufacturers/models")
@RequiredArgsConstructor
public class CarModelController implements SecuredRestController {

    private final CarModelService carModelService;
    private final CarModelCreateEditDtoValidator carModelCreateEditDtoValidator;

    @GetMapping
    public PageResponse<CarModelReadDto> findAllModels(CarFilter filter, Pageable pageable) {
        Page<CarModelReadDto> page = carModelService.findAll(filter, pageable);
        return PageResponse.of(page);
    }

    @GetMapping("/{id}")
    public CarModelReadDto findModelById(@PathVariable("id") Integer id) {
        return carModelService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarModelReadDto createModel(@RequestBody @Valid CarModelCreateEditDto modelDto,
                                       BindingResult result) throws BindException {
        carModelCreateEditDtoValidator.validate(modelDto, result);
        if(result.hasErrors()) {
            throw new BindException(result);
        }
        return carModelService.save(modelDto);
    }

    @PutMapping("/{id}")
    public CarModelReadDto updateModel(@PathVariable("id") Integer id,
                                       @RequestBody @Valid CarModelCreateEditDto modelDto,
                                       BindingResult result) throws BindException {
        carModelCreateEditDtoValidator.validate(modelDto, result);
        if(result.hasErrors()) {
            throw new BindException(result);
        }
        return carModelService.update(id, modelDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteModel(@PathVariable("id") Integer id) {
        if (Boolean.FALSE.equals(carModelService.deleteById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
