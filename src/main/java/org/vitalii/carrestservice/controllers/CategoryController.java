package org.vitalii.carrestservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.vitalii.carrestservice.dto.CategoryCreateEditDto;
import org.vitalii.carrestservice.dto.CategoryReadDto;
import org.vitalii.carrestservice.dto.PageResponse;
import org.vitalii.carrestservice.dto.filters.CarFilter;
import org.vitalii.carrestservice.services.CategoryService;
import org.vitalii.carrestservice.services.validators.CategoryCreateEditDtoValidator;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/manufacturers/models/years/categories")
@RequiredArgsConstructor
public class CategoryController implements SecuredRestController {

    private final CategoryService categoryService;
    private final CategoryCreateEditDtoValidator categoryCreateEditDtoValidator;

    @GetMapping
    public PageResponse<CategoryReadDto> findAllCategories(CarFilter carFilter, Pageable pageable) {
        return PageResponse.of(categoryService.finAll(carFilter, pageable));
    }

    @GetMapping("/{id}")
    public CategoryReadDto findCategoryById(@PathVariable("id") Integer id) {
        return categoryService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryReadDto createCategory(@RequestBody @Valid CategoryCreateEditDto categoryDto,
                                          BindingResult result) throws BindException {
        categoryCreateEditDtoValidator.validate(categoryDto, result);
        if(result.hasErrors()) {
            throw new BindException(result);
        }
        return categoryService.save(categoryDto);
    }

    @PutMapping("/{id}")
    public CategoryReadDto updateCategory(@PathVariable("id") Integer id,
                                          @RequestBody @Valid CategoryCreateEditDto categoryDto,
                                          BindingResult result) throws BindException {
        categoryCreateEditDtoValidator.validate(categoryDto, result);
        if(result.hasErrors()) {
            throw new BindException(result);
        }
        return categoryService.update(id, categoryDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("id") Integer id) {
        if (Boolean.FALSE.equals(categoryService.deleteById(id))) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
