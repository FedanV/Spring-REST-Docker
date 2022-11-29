package org.vitalii.carrestservice.services.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.vitalii.carrestservice.dto.CarBrandCreateEditDto;
import org.vitalii.carrestservice.services.CarBrandService;

@Component
@RequiredArgsConstructor
public class CarBrandCreateEditDtoValidator implements Validator {
    private final CarBrandService carBrandService;
    @Override
    public boolean supports(Class<?> clazz) {
        return CarBrandCreateEditDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CarBrandCreateEditDto brand = (CarBrandCreateEditDto) target;

        if(carBrandService.findByBrand(brand.getBrand()).isPresent()) {
            errors.rejectValue("brand", "",String.format("Brand '%s' exists", brand.getBrand()));
        }

    }
}
