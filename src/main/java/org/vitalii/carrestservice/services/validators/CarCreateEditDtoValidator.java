package org.vitalii.carrestservice.services.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.vitalii.carrestservice.dto.CarCreateEditDto;
import org.vitalii.carrestservice.dto.CategoryCreateEditDto;
import org.vitalii.carrestservice.services.CarBrandService;
import org.vitalii.carrestservice.services.CarModelService;
import org.vitalii.carrestservice.services.CarYearService;
import org.vitalii.carrestservice.services.CategoryService;

@Component
@RequiredArgsConstructor
public class CarCreateEditDtoValidator implements Validator {

    private final CarModelService carModelService;
    private final CarBrandService carBrandService;
    private final CarYearService carYearService;
    private final CategoryService categoryService;

    @Override
    public boolean supports(Class<?> clazz) {
        return CarCreateEditDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CarCreateEditDto carDto = (CarCreateEditDto) target;

        if (carModelService.findByModel(carDto.getModel().getModel()).isPresent()) {
            errors.rejectValue("model", "", String.format("Model '%s' exists", carDto.getModel().getModel()));
        }

        if (carBrandService.findByBrand(carDto.getBrand().getBrand()).isPresent()) {
            errors.rejectValue("brand", "", String.format("Brand '%s' exists", carDto.getBrand().getBrand()));
        }

        if (carYearService.findByYear(carDto.getYear().getYear()).isPresent()) {
            errors.rejectValue("year", "", String.format("Year '%s' exists", carDto.getYear().getYear()));
        }

        for (CategoryCreateEditDto categoryDto : carDto.getCategories()) {
            if (categoryService.findByName(categoryDto.getName()).isPresent()) {
                errors.rejectValue("categories", "", String.format("Category '%s' exists", categoryDto.getName()));
            }
        }
    }
}
