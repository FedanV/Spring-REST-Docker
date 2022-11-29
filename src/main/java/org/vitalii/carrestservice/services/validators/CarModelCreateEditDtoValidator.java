package org.vitalii.carrestservice.services.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.vitalii.carrestservice.dto.CarModelCreateEditDto;
import org.vitalii.carrestservice.services.CarModelService;

@Component
@RequiredArgsConstructor
public class CarModelCreateEditDtoValidator implements Validator {

    private final CarModelService carModelService;

    @Override
    public boolean supports(Class<?> clazz) {
        return CarModelCreateEditDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CarModelCreateEditDto modelDto = (CarModelCreateEditDto) target;
        if(carModelService.findByModel(modelDto.getModel()).isPresent()) {
            errors.rejectValue("model", "", String.format("Model '%s' exists", modelDto.getModel()));
        }
    }
}
