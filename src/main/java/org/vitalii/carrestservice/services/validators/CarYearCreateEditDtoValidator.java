package org.vitalii.carrestservice.services.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.vitalii.carrestservice.dto.CarYearCreateEditDto;
import org.vitalii.carrestservice.services.CarYearService;

@Component
@RequiredArgsConstructor
public class CarYearCreateEditDtoValidator implements Validator {

    private final CarYearService carYearService;

    @Override
    public boolean supports(Class<?> clazz) {
        return CarYearCreateEditDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CarYearCreateEditDto yearDto = (CarYearCreateEditDto) target;
        if(carYearService.findByYear(yearDto.getYear()).isPresent()) {
            errors.rejectValue("year", "", String.format("Year '%s' exists", yearDto.getYear()));
        }
    }
}
