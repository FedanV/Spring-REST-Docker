package org.vitalii.carrestservice.services.validators;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.vitalii.carrestservice.dto.CategoryCreateEditDto;
import org.vitalii.carrestservice.services.CategoryService;

@Component
@RequiredArgsConstructor
public class CategoryCreateEditDtoValidator implements Validator {

    private final CategoryService categoryService;

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryCreateEditDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryCreateEditDto categoryDto = (CategoryCreateEditDto) target;
        if (categoryService.findByName(categoryDto.getName()).isPresent()) {
            errors.rejectValue("name", "", String.format("Category '%s' exists", categoryDto.getName()));
        }
    }

}
